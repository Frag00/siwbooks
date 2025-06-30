package it.uniroma3.siw.model;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
@Entity
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	private Integer annoPubblicazione;
	
	@NotBlank
	private String titolo;
	
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Picture> immagini;
	
	//@NotEmpty
	@ManyToMany(mappedBy = "libri")
	private Set<Author> autori;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "libro")
	private Set<Review> recensioni;
	
	public Book() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAnnoPubblicazione() {
		return annoPubblicazione;
	}

	public void setAnnoPubblicazione(Integer annoPubblicazione) {
		this.annoPubblicazione = annoPubblicazione;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public Set<Picture> getImmagini() {
		return immagini;
	}

	public void setImmagini(Set<Picture> immagini) {
		this.immagini = immagini;
	}

	public Set<Author> getAutori() {
		return autori;
	}

	public void setAutori(Set<Author> autori) {
		this.autori = autori;
	}

	public Set<Review> getRecensioni() {
		return recensioni;
	}

	public void setRecensioni(Set<Review> recensioni) {
		this.recensioni = recensioni;
	}

	@Override
	public int hashCode() {
		return Objects.hash(annoPubblicazione, autori, titolo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		return Objects.equals(annoPubblicazione, other.annoPubblicazione) && Objects.equals(autori, other.autori)
				&& Objects.equals(titolo, other.titolo);
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", annoPubblicazione=" + annoPubblicazione + ", titolo=" + titolo + "]";
	}
	
	
}
