package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;

@Entity
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;
	
	@NotBlank
	private String cognome;
	
	@Past
	@NotNull
	private LocalDate dataNascita;
	
	@PastOrPresent
	private LocalDate dataMorte;
	
	@NotBlank
	private String nazionalita;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Picture immagine;
	
	@ManyToMany(mappedBy = "autori") // se voglio che eliminato l'autore vengano eliminati anche i suoi libri devo mettere il cascade, nel progetto suppongo di no
	private Set<Book> libri;

	public Author() {}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}

	public LocalDate getDataMorte() {
		return dataMorte;
	}

	public void setDataMorte(LocalDate dataMorte) {
		this.dataMorte = dataMorte;
	}

	public String getNazionalita() {
		return nazionalita;
	}

	public void setNazionalita(String nazionalita) {
		this.nazionalita = nazionalita;
	}

	public Picture getImmagine() {
		return immagine;
	}

	public void setImmagine(Picture immagine) {
		this.immagine = immagine;
	}

	public Set<Book> getLibri() {
		return libri;
	}

	public void setLibri(Set<Book> libri) {
		this.libri = libri;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cognome, dataNascita, nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Author other = (Author) obj;
		return Objects.equals(cognome, other.cognome) && Objects.equals(dataNascita, other.dataNascita)
				&& Objects.equals(nome, other.nome);
	}

	@Override
	public String toString() {
		return "Author [nome=" + nome + ", cognome=" + cognome + ", dataNascita=" + dataNascita + ", dataMorte="
				+ dataMorte + ", nazionalità=" + nazionalita + "]";
	}
	
	
	
}
