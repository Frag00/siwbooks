package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.service.BookService;

@Controller
public class BookController {

	@Autowired BookService bookService;
	
	@GetMapping("/book")
	public String showBooks(Model model) {
		model.addAttribute("books", this.bookService.getAllBooks());
		return "books.html";
	}
	
	@GetMapping("/book/{id}")
	public String getBook(@PathVariable("id") Long id, Model model) {
		model.addAttribute("book",this.bookService.getBookById(id));
		return "book.html";
	}
	
	@GetMapping("/")
	public String getIndex() {
		return "index.html";
	}
	
	@GetMapping("/form")
	public String getForm(Model model) {
		model.addAttribute("book", new Book());
		return "formNewBook.html";
	}
	
	@PostMapping("/pippo")
	public String addBook(@ModelAttribute("book") Book book,Model model) {
		this.bookService.saveBook(book);
		return "redirect:book/"+book.getId();
	}
}
