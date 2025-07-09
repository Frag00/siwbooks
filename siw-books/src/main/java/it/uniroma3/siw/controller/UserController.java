package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.service.UserService;

@Controller
public class UserController {

	@Autowired UserService userService;
	@Autowired BookService bookService;
	@Autowired ReviewService reviewService;
	
	private boolean verifyId(Long idUrl, Long idUser) {
		return idUser!= null && idUrl == idUser;
	}
	
	@GetMapping("/user/profile")
	public String profiloUtente(Model model) {
			model.addAttribute("recensioni",userService.getCurrentUser().getRecensioni());
			return "user/profile.html";
	}
}
