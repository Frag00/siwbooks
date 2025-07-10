package it.uniroma3.siw.repository;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Picture;


public interface BookRepository extends CrudRepository<Book,Long>{
	public boolean existsByTitoloAndAnnoPubblicazione(String titolo, Integer annoPubblicazione);
	public boolean existsByTitoloAndAnnoPubblicazioneAndAutori(String titolo, Integer annoPubblicazione, Set<Author> autori);
}
