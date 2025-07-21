package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.repository.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;
	
	public Book getBookById(Long id) {
		return bookRepository.findById(id).get();
	}
	
	public Iterable<Book> getAllBooks() {
		return bookRepository.findAll();
	}
	
	public void saveBook(Book book) {
		this.bookRepository.save(book);
	}
	public boolean existsByTitoloAndAnno(Book book) {
		return this.bookRepository.existsByTitoloAndAnnoPubblicazione(book.getTitolo(), book.getAnnoPubblicazione());
	}

	public void deleteBook(Book daRimuovere) {
		bookRepository.delete(daRimuovere);
		
	}
	
	public boolean existsBookByTitoloAndAnnoAndAutori(Book book) {
		return bookRepository.existsByTitoloAndAnnoPubblicazioneAndAutori(book.getTitolo(),book.getAnnoPubblicazione(),book.getAutori());
	}
	
	public Iterable<Book> searchByTitolo(String titolo){
		return bookRepository.findByTitoloContainingIgnoreCase(titolo);
	}
}