package it.uniroma3.siw.model;

import jakarta.persistence.*;
@Entity
public class Libro {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Integer annoPubblicazione;
	
	private String titolo;
	
	// qui lista di immagini
	
	// qui lista di autori 
	
	public Libro() {}
	
	
}
