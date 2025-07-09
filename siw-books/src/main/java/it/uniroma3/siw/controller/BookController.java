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
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.PictureService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;

@Controller
public class BookController {

	@Autowired BookService bookService;
	
	@Autowired AuthorService authorService;
	
	@Autowired PictureService pictureService;
	
	@Autowired UserService userService;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setDisallowedFields("immagini");
	}
	
	@GetMapping("/book")
	public String showBooks(Model model) {
		model.addAttribute("books", this.bookService.getAllBooks());
		return "books.html";
	}
	///////////////////////// cancella 
	@GetMapping("/user/book")
	public String showsBooks(Model model) {
		model.addAttribute("books", this.bookService.getAllBooks());
		return "user/books.html";
	}
	@GetMapping("/admin/book")
	public String showsBooksAdmin(Model model) {
		model.addAttribute("books", this.bookService.getAllBooks());
		return "admin/books.html";
	}
	
	@GetMapping("/book/{id}")
	public String getBook(@PathVariable("id") Long id, Model model) {
		model.addAttribute("book",this.bookService.getBookById(id));
		return "book.html";
	}
	
	@GetMapping("/admin/book/{id}")
	public String getBookAdmin(@PathVariable("id") Long id, Model model) {
		model.addAttribute("book",this.bookService.getBookById(id));
		return "admin/book.html";
	}
	
	@GetMapping("/user/book/{id}")
	public String getBookUser(@PathVariable("id") Long id, Model model) {
		model.addAttribute("book",this.bookService.getBookById(id));
		return "user/book.html";
	}
	
	
	@GetMapping("/admin/formNewBook")
	public String getForm(Model model) {
		model.addAttribute("book", new Book());
		model.addAttribute("authors",authorService.getAllAuthors());
		return "admin/formNewBook.html";
	}
	
	@GetMapping("/admin/book/{idB}/remove")
	public String adminDeletesBook(@PathVariable("idB") Long idB, Model model) {
		
		Book daRimuovere = bookService.getBookById(idB);
		
		for(Author autore : daRimuovere.getAutori()) {
			autore.getLibri().remove(daRimuovere);
			authorService.saveAuthor(autore);
		}
		
		for(Review r : daRimuovere.getRecensioni()) {
			r.getUtente().getRecensioni().remove(r);
			userService.saveUser(r.getUtente());
		}
		
		bookService.deleteBook(daRimuovere); // a  cascata vengono rimosse tutte le immagini del libro e le recensioni
		
		return "redirect:/admin/book";
	}
	
	
	
	@PostMapping("/pippo")
	public String addBook(@Valid @ModelAttribute("book") Book book,BindingResult bindingResult,@RequestParam(value = "autori", required = false) List<Long> autoriIds,@RequestParam("imageFiles") MultipartFile[] imageFiles,Model model) {
		if(this.bookService.existsByTitoloAndAnno(book)) {
			model.addAttribute("errEsiste","Libro già presente");
			model.addAttribute("authors",authorService.getAllAuthors());
			return "admin/formNewBook.html";
		}
		else if(bindingResult.hasErrors()) {
			model.addAttribute("authors",authorService.getAllAuthors());

			return "admin/formNewBook.html";
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
	            return "admin/formNewBook.html";
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
		return "redirect:/admin/book/"+book.getId();
	      }
	        
	        catch (Exception e) {
	            model.addAttribute("msgError", "Errore nel salvataggio del libro:\n"+ e.toString());
	            model.addAttribute("authors",authorService.getAllAuthors());
	            return "admin/formNewBook.html";
	        }
		}
	}
	
	@GetMapping("/admin/book/{idB}/formEditBook")
	public String showAdminFormEditBook(@PathVariable("idB") Long idB,Model model) {
		
		Book dM = bookService.getBookById(idB);
		Book dR = new Book();
		
		dR.setId(idB);
		dR.setAnnoPubblicazione(dM.getAnnoPubblicazione());
		dR.setImmagini(dM.getImmagini());
		dR.setTitolo(dM.getTitolo());
		dR.setAutori(dM.getAutori());
		
		model.addAttribute("authors",authorService.getAllAuthors());
		model.addAttribute("book",dR);
		return "admin/formEditBook.html";
	}
	
	@PostMapping("/admin/book/{idB}/edit")
	public String adminEditsBook(@Valid @ModelAttribute("book") Book book,BindingResult bindingResult,@RequestParam(value = "autori", required = false) List<Long> autoriIds,@RequestParam("imageFiles") MultipartFile[] imageFiles,@PathVariable("idB") Long idB,Model model) {
		if(this.bookService.existsByTitoloAndAnno(book)) {
			model.addAttribute("errEsiste","Libro già presente");
			model.addAttribute("authors",authorService.getAllAuthors());
			return "admin/formEditBook.html";
		}
		else if(bindingResult.hasErrors()) {
			model.addAttribute("authors",authorService.getAllAuthors());
			book.setId(idB);
			model.addAttribute("book",book);
			return "admin/formEditBook.html";
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
	        	book.setId(idB);
	        	model.addAttribute("book",book);
	        	model.addAttribute("authors",authorService.getAllAuthors());
	            return "admin/formEditBook.html";
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
	      
	    Book b = bookService.getBookById(idB);
	    
	    b.setAnnoPubblicazione(book.getAnnoPubblicazione());
		b.setImmagini(book.getImmagini());
		b.setTitolo(book.getTitolo());
		b.setAutori(book.getAutori());
	        
		this.bookService.saveBook(b);
		model.addAttribute("book",b);
		return "redirect:/admin/book/"+ idB;
	      }
	        
	        catch (Exception e) {
	            model.addAttribute("msgError", "Errore nel salvataggio del libro:\n"+ e.toString());
	            model.addAttribute("authors",authorService.getAllAuthors());
	            book.setId(idB);
	            model.addAttribute("book",book);
	            return "admin/formEditBook.html";
	        }
		}
	}
	
}
