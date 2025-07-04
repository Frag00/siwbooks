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
import it.uniroma3.siw.model.Picture;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.PictureService;
import jakarta.validation.Valid;

@Controller
public class AuthorController {

	@Autowired
	private AuthorService authorService;
	@Autowired
	private PictureService pictureService;
	
	@GetMapping("/author")
	public String showAuthors(Model model) {
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
	
	@PostMapping("/admin/nuovoAuthor")
	public String addNewAuthor(@Valid @ModelAttribute("author") Author author,BindingResult bindingResult, @RequestParam("imageFile") MultipartFile imageFile ,Model model) {
		if(this.authorService.existsByNomeAndCognomeAndDataNascita(author)) {
			model.addAttribute("errEsiste","Autore già presente");
			return "admin/formNewAuthor.html";
		}
		else if(bindingResult.hasErrors()) {
			model.addAttribute("errEsiste","Autore già presente");
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
	
}
