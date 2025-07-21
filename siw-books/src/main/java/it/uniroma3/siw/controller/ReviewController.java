package it.uniroma3.siw.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
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
	
	@GetMapping("/user/book/{idB}/review/{idR}/remove")
	public String userDeletesReview(@PathVariable("idB") Long idB,@PathVariable("idR") Long idR, Model model) {
		
		User corrente = credentialsService.getCurrentUser();
		Book libro = bookService.getBookById(idB);
		
		
		Review daEliminare = reviewService.getReviewById(idR).get();
		
		

		corrente.getRecensioni().remove(daEliminare);
		libro.getRecensioni().remove(daEliminare);
		
		userService.saveUser(corrente);
		bookService.saveBook(libro);
		
		
		reviewService.deleteReview(daEliminare);
		
		
		return "redirect:/user/book/" + idB;
	}
	
	@GetMapping("/user/book/{idB}/review/{idR}/formEdit")
	public String userFormEditsReview(@PathVariable("idB") Long idB , @PathVariable("idR") Long idR , Model model) {
		
		Review daEditare = reviewService.getReviewById(idR).get();
		Review daRiempire = new Review();
		
		daRiempire.setId(idR);
		daRiempire.setTitolo(daEditare.getTitolo());
		daRiempire.setTesto(daEditare.getTesto());
		daRiempire.setVoto(daEditare.getVoto());
		model.addAttribute("review", daRiempire);
		model.addAttribute("book", bookService.getBookById(idB));
		return "user/formEditReview.html";
	}
	
	@PostMapping("/user/book/{idB}/review/{idR}/edit")
	public String userEditsReview(@Valid @ModelAttribute("review") Review r, BindingResult bindingResult, Model model, @PathVariable("idB") Long idB , @PathVariable("idR") Long idR ) {
		
		if(bindingResult.hasErrors()) {
			r.setId(idR);
			model.addAttribute("review", r);
			model.addAttribute("book", bookService.getBookById(idB));
			return "user/formEditReview.html";
		}
		

		Review veraReview = reviewService.getReviewById(idR).get();
		
		veraReview.setTitolo(r.getTitolo());
		veraReview.setTesto(r.getTesto());
		veraReview.setVoto(r.getVoto());
		
		reviewService.saveReview(veraReview);
		
		
		return "redirect:/user/book/" + idB;
	}
	
	@GetMapping("/admin/book/{idB}/review/{idR}/remove")
	public String adminDeletesReview(@PathVariable("idB") Long idB,@PathVariable("idR") Long idR, Model model) {
		
		Book libro = bookService.getBookById(idB);
		
		
		Review daEliminare = reviewService.getReviewById(idR).get();
		
		User proprietarioRecensione = daEliminare.getUtente();

		proprietarioRecensione.getRecensioni().remove(daEliminare);
		libro.getRecensioni().remove(daEliminare);
		
		userService.saveUser(proprietarioRecensione);
		bookService.saveBook(libro);
		
		
		reviewService.deleteReview(daEliminare);
		
		
		return "redirect:/admin/book/" + idB;
	}
	
}
