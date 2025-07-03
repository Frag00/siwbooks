package it.uniroma3.siw.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Picture;
import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.PictureService;
import jakarta.validation.Valid;

@Controller
public class BookController {

	@Autowired BookService bookService;
	
	@Autowired AuthorService authorService;
	
	@Autowired PictureService pictureService;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setDisallowedFields("immagini");
	}
	
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
		model.addAttribute("authors",authorService.getAllAuthors());
		return "formNewBook.html";
	}
	
	@PostMapping("/pippo")
	public String addBook(@Valid @ModelAttribute("book") Book book,BindingResult bindingResult,@RequestParam(value = "autori", required = false) List<Long> autoriIds,@RequestParam("imageFiles") MultipartFile[] imageFiles,Model model) {
		if(this.bookService.existsByTitoloAndAnno(book)) {
			model.addAttribute("errEsiste","Libro già presente");
			model.addAttribute("authors",authorService.getAllAuthors());
			return "formNewBook.html";
		}
		else if(bindingResult.hasErrors()) {
			model.addAttribute("authors",authorService.getAllAuthors());

			return "formNewBook.html";
		}
		
		else {
	        if (autoriIds != null && !autoriIds.isEmpty()) {
	            Set<Author> autoriSelezionati = new HashSet<>();
	            for (Long autorId : autoriIds) {
	                Author author = authorService.getAuthorById(autorId);
	                if (author != null) {
	                    autoriSelezionati.add(author);     
	                    author.getLibri().add(book);
	                }
	            }
	            book.setAutori(autoriSelezionati);
	        }
	        
	        /* gestione delle immagini */
	        
	        if(imageFiles.length == 0 || (imageFiles.length == 1 && imageFiles[0].isEmpty())) {
	        	model.addAttribute("msgError", "Inserire almeno una foto");
	        	model.addAttribute("book",book);
	        	model.addAttribute("authors",authorService.getAllAuthors());
	            return "formNewBook.html";
	        }

	        try {
	            // Gestione immagini
	            Set<Picture> images = new HashSet<Picture>();
	            for (MultipartFile file : imageFiles) {
	                if (!file.isEmpty()) {
	                	String name = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
	                    Picture img = new Picture(name);
	                    this.pictureService.savePhysicalImage(file.getBytes(), name);
	                    images.add(img);
	                }
	            }

	        // Associamo le immagini al libro
	        book.setImmagini(images);
	        
		this.bookService.saveBook(book);
		model.addAttribute("book",book);
		return "redirect:book/"+book.getId();
	      }
	        
	        catch (Exception e) {
	            model.addAttribute("msgError", "Errore nel salvataggio del libro:\n"+ e.toString());
	            model.addAttribute("authors",authorService.getAllAuthors());
	            return "formNewBook.html";
	        }
		}
	}
}
