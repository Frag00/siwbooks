package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
public class ReviewController {
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private BookService bookService;
	@Autowired
	private CredentialsService credentialsService;
	@Autowired
	private UserService userService;

	@GetMapping("/user/book/{id}/formNewReview")
	public String showUserFormReview(@PathVariable("id") Long id,Model model) {
		if(!reviewService.existsReviewByBookAndUser(bookService.getBookById(id), credentialsService.getCurrentUser())) {
		model.addAttribute("review", new Review());
		model.addAttribute("book", bookService.getBookById(id));
		return "user/formNewReview.html";
		}
		else {
			model.addAttribute("giaEsiste", "Hai già scritto una recensione per questo libro");
			model.addAttribute("book", bookService.getBookById(id));
			return "user/formNewReview.html";
		}
		
	}
	
	@PostMapping("/user/book/{id}/addReview")
	public String addReviewToBookUser(@Valid @ModelAttribute("review") Review r, BindingResult bindingResult, Model model,@PathVariable("id") Long id) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("review", r);
			model.addAttribute("book", bookService.getBookById(id));
			return "user/formNewReview.html";
		}
		
		else {
			
			Review newReview = new Review();
		    newReview.setTitolo(r.getTitolo());
		    newReview.setVoto(r.getVoto());
		    newReview.setTesto(r.getTesto());
		    
		    User corrente = credentialsService.getCurrentUser();
			Book libro = bookService.getBookById(id);
			
			newReview.setLibro(libro);
			newReview.setUtente(corrente);
			
			reviewService.saveReview(newReview);
			
			//libro.getRecensioni().add(r);
			//corrente.getRecensioni().add(r);
			
			//bookService.saveBook(libro);
			//userService.saveUser(corrente);
			
	
			return "redirect:/user/book/" + libro.getId();
		}
	}
	
	
}
