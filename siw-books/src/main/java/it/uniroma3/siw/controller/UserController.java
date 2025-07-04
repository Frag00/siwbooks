package it.uniroma3.siw.controller;

import org.springframework.stereotype.Controller;

@Controller
public class UserController {

	
	private boolean verifyId(Long idUrl, Long idUser) {
		return idUser!= null && idUrl == idUser;
	}
	
	// per l'admin 
	/*
	private boolean verifyAdmin(Long idUrl, User user) {
		return user!= null && idUrl == user.getId() && this.credentialsService.getCredentialsByUser(user).getRole().equals(ADMIN_ROLE);
	}*/
}
