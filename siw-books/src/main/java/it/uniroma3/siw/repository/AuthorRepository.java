package it.uniroma3.siw.repository;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Author;
import jakarta.validation.Valid;


public interface AuthorRepository extends CrudRepository<Author,Long>{

	boolean existsByNomeAndCognomeAndDataNascita(String nome,String cognome,LocalDate dataNascita);

}
