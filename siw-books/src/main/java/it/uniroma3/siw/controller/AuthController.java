package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;

@Controller
public class AuthController {

	@Autowired CredentialsService credentialsService;
	@Autowired UserService userService;
	@Autowired BookService bookService;
	
	@GetMapping(value = "/login")
	public String getFormLogin(Model model) {
		return "login.html";
	}
	
	@GetMapping(value = "/register")
	public String getFormRegister(Model model) {
		model.addAttribute("utente",new User());
		model.addAttribute("credentials",new Credentials());
		return "register.html";
	}
	
	@PostMapping("/register")
	public String register(@ModelAttribute("utente") User u,@ModelAttribute("credentials") Credentials c,Model model) {
		if(credentialsService.existsByUsername(c.getUsername())) {
			model.addAttribute("esiste", "utente già presente nel db");
			return "redirect:/register";
		}
		else {
		userService.saveUser(u);
		c.setUtente(u);
		credentialsService.saveCredentials(c);
		if(credentialsService.getCredentialsByUser(u).equals(c.ruoloAdmin)) {
			model.addAttribute("books", this.bookService.getAllBooks());
			return "admin/books.html";
		}
		else
			model.addAttribute("books", this.bookService.getAllBooks());
			return "user/books.html";
		}
	}
}
