package it.uniroma3.siw.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.repository.AuthorRepository;
import jakarta.validation.Valid;
import it.uniroma3.siw.model.*;

@Service
public class AuthorService {

	@Autowired AuthorRepository authorRepository;
	
	public Iterable<Author> getAllAuthors(){
		return authorRepository.findAll();
	}

	public Iterable<Author> findAllById(List<Long> autoriIds) {
		return authorRepository.findAllById(autoriIds);
	}

	public Author getAuthorById(Long autorId) {
		return authorRepository.findById(autorId).orElse(null);
	}

	public boolean existsByNomeAndCognomeAndDataNascita(@Valid Author author) {
		return authorRepository.existsByNomeAndCognomeAndDataNascita(author.getNome(),author.getCognome(),author.getDataNascita());
	}

	public void saveAuthor(@Valid Author author) {
		authorRepository.save(author);
	}

	public void deleteAuthor(Author daRimuovere) {
		authorRepository.delete(daRimuovere);
		
	}
}
