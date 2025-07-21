package it.uniroma3.siw.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Picture;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.PictureService;
import jakarta.validation.Valid;

@Controller
public class AuthorController {

	@Autowired
	private AuthorService authorService;
	@Autowired
	private PictureService pictureService;
	@Autowired 
	private BookService bookService;
	@Autowired 
	private CredentialsService credentialsService;
	
	@GetMapping("/author")
	public String showAuthors(Model model) {
		
		Credentials c = credentialsService.getCredentialsByUser(credentialsService.getCurrentUserM());
		
		if(c!=null) {	
			if(c.getRuolo().equals("ADMIN"))
				return "redirect:/admin/author";
			else if(c.getRuolo().equals("DEFAULT"))
				return "redirect:/user/author";
		}
		
		model.addAttribute("authors",authorService.getAllAuthors());
		return "authors.html";
	}
	
	@GetMapping("/admin/author")
	public String showAuthorsAdmin(Model model) {
		model.addAttribute("authors",authorService.getAllAuthors());
		return "admin/authors.html";
	}
	
	@GetMapping("/user/author")
	public String showAuthorsUser(Model model) {
		model.addAttribute("authors",authorService.getAllAuthors());
		return "user/authors.html";
	}
	
	@GetMapping("/author/{id}")
	public String getAuthor(@PathVariable("id") Long id ,Model model ) {
		Credentials c = credentialsService.getCredentialsByUser(credentialsService.getCurrentUserM());
		
		if(c!=null) {	
			if(c.getRuolo().equals("ADMIN"))
				return "redirect:/admin/author/" + id;
			else if(c.getRuolo().equals("DEFAULT"))
				return "redirect:/user/author/" + id;
		}
		
		
		model.addAttribute("author", authorService.getAuthorById(id));
		return "author.html";
	}
	
	@GetMapping("/admin/author/{id}")
	public String getAuthorAdmin(@PathVariable("id") Long id ,Model model ) {
		model.addAttribute("author", authorService.getAuthorById(id));
		return "admin/author.html";
	}
	
	@GetMapping("/user/author/{id}")
	public String getAuthorUser(@PathVariable("id") Long id ,Model model ) {
		model.addAttribute("author", authorService.getAuthorById(id));
		return "user/author.html";
	}
	
	@GetMapping("/admin/formNewAuthor")
	public String formAuthor(Model model) {
		model.addAttribute("author",new Author());
		return "admin/formNewAuthor.html";
	}
	
	@GetMapping("/admin/author/{idA}/remove")
	public String adminDeletesBook(@PathVariable("idA") Long idA, Model model) {
		
		Author daRimuovere = authorService.getAuthorById(idA);
		
		for(Book libro : daRimuovere.getLibri()) {
			libro.getAutori().remove(daRimuovere);
			bookService.saveBook(libro);
		}
		
		authorService.deleteAuthor(daRimuovere); // a cascata elimina tutte le immagini
		
		
		return "redirect:/admin/author";
	}
	
	@PostMapping("/admin/nuovoAuthor")
	public String addNewAuthor(@Valid @ModelAttribute("author") Author author,BindingResult bindingResult, @RequestParam("imageFile") MultipartFile imageFile ,Model model) {
		if(this.authorService.existsByNomeAndCognomeAndDataNascita(author)) {
			model.addAttribute("errEsiste","Autore già presente");
			return "admin/formNewAuthor.html";
		}
		else if(bindingResult.hasErrors()) {
			return "admin/formNewAuthor.html";
		}
		else {
		
			/* gestione delle immagini */
	        
	        if(imageFile==null) {
	        	model.addAttribute("msgError", "Inserire una foto");
	        	model.addAttribute("author",author);
	            return "admin/formNewAuthor.html";
	        }

	        try {
	            // Gestione immagini
	               	String name = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
	                Picture img = new Picture(name);
	                this.pictureService.savePhysicalImage(imageFile.getBytes(), name);	               	               
	        // Associamo la foto all'autore
	        author.setImmagine(img);
	        
		

		authorService.saveAuthor(author);
		model.addAttribute("author", author);
		return "redirect:/admin/author/"+author.getId();
		}
	        catch (Exception e) {
	            model.addAttribute("msgError", "Errore nel salvataggio dell'autore:\n"+ e.toString());
	            return "admin/formNewAuthor.html";
	        }
		
	}
 }
	
	@GetMapping("/admin/author/{idA}/formEditAuthor")
	public String showAdminFormEditAuthor(@PathVariable("idA") Long idA ,Model model) {
		
		Author daModificare = authorService.getAuthorById(idA);
		Author fittizio = new Author();
		
		fittizio.setNome(daModificare.getNome());
		fittizio.setCognome(daModificare.getCognome());
		fittizio.setDataNascita(daModificare.getDataNascita());
		fittizio.setDataMorte(daModificare.getDataMorte());
		fittizio.setId(daModificare.getId());
		fittizio.setImmagine(daModificare.getImmagine());
		fittizio.setNazionalita(daModificare.getNazionalita());
		
		model.addAttribute("author",fittizio);
		
		return "admin/formEditAuthor";
	}
	
	@PostMapping("/admin/author/{idA}/edit")
	public String adminEditsAuthor(@Valid @ModelAttribute("author") Author author,BindingResult bindingResult, @RequestParam("imageFile") MultipartFile imageFile ,@PathVariable("idA") Long idA,Model model) {
		if(this.authorService.existsByNomeAndCognomeAndDataNascita(author)) {
			model.addAttribute("errEsiste","Autore già presente");
			author.setId(idA);
			model.addAttribute("author", author);
			return "admin/formEditAuthor.html";
		}
		else if(bindingResult.hasErrors()) {
			author.setId(idA);
			model.addAttribute("author", author);
			return "admin/formEditAuthor.html";
		}
		else {
		
			/* gestione delle immagini */
	        
	        if(imageFile==null) {
	        	model.addAttribute("msgError", "Inserire una foto");
	        	author.setId(idA);
	        	model.addAttribute("author",author);
	            return "admin/formEditAuthor.html";
	        }

	        try {
	            // Gestione immagini
	               	String name = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
	                Picture img = new Picture(name);
	                this.pictureService.savePhysicalImage(imageFile.getBytes(), name);	               	               
	        // Associamo la foto all'autore
	        author.setImmagine(img);
	        
		Author veroA = authorService.getAuthorById(idA);
		
		veroA.setNome(author.getNome());
		veroA.setCognome(author.getCognome());
		veroA.setDataMorte(author.getDataMorte());
		veroA.setDataNascita(author.getDataNascita());
		veroA.setImmagine(author.getImmagine());
		veroA.setNazionalita(author.getNazionalita());
		

		authorService.saveAuthor(veroA);
		model.addAttribute("author", veroA);
		return "redirect:/admin/author/"+ idA;
		}
	        catch (Exception e) {
	            model.addAttribute("msgError", "Errore nel salvataggio dell'autore:\n"+ e.toString());
	            author.setId(idA);
	            model.addAttribute("author", author);
	            return "admin/formNewAuthor.html";
	        }
		
	}
 }
	
	
	/* implementazione barra di ricerca per titolo */ 
	
	@GetMapping("/author/search")
	public String searchAuthor(@RequestParam String ricerca, Model model) {
		
		//controllo coerenza ruoli
		Credentials c = credentialsService.getCredentialsByUser(credentialsService.getCurrentUserM());
		
		if(c!=null) {	
			if(c.getRuolo().equals("ADMIN"))
				return "redirect:/admin/author";
			else if(c.getRuolo().equals("DEFAULT"))
				return "redirect:/user/author";
		}
		///////////////////////////////
		
		if(ricerca==null || ricerca.isBlank() || ricerca.isEmpty()) {
			model.addAttribute("authors", authorService.getAllAuthors());
			return "authors.html";
		}
		
		Iterable<Author> risultato = authorService.searchAuthorByNomeOrCognome(ricerca, ricerca);
		model.addAttribute("authors", risultato);
		model.addAttribute("ricerca", ricerca);
		return "authors.html";
		
	}
	
	@GetMapping("/user/author/search")
	public String userSearchAuthor(@RequestParam String ricerca, Model model) {
		if(ricerca==null || ricerca.isBlank() || ricerca.isEmpty()) {
			model.addAttribute("authors", authorService.getAllAuthors());
			return "user/authors.html";
		}
		
		Iterable<Author> risultato = authorService.searchAuthorByNomeOrCognome(ricerca, ricerca);
		model.addAttribute("authors", risultato);
		model.addAttribute("ricerca", ricerca);
		return "user/authors.html";
		
	}
	
	@GetMapping("/admin/author/search")
	public String adminSearchAuthor(@RequestParam String ricerca, Model model) {
		if(ricerca==null || ricerca.isBlank() || ricerca.isEmpty()) {
			model.addAttribute("authors", authorService.getAllAuthors());
			return "admin/authors.html";
		}
		
		Iterable<Author> risultato = authorService.searchAuthorByNomeOrCognome(ricerca, ricerca);
		model.addAttribute("authors", risultato);
		model.addAttribute("ricerca", ricerca);
		return "admin/authors.html";
		
	}
	
	
}
