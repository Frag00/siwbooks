package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String nome;
	
	private String cognome;
	
	private LocalDate dataNascita;
	
	private LocalDate dataMorte;
	
	private String nazionalità;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Picture immagine;
	
	@ManyToMany
	private List<Book> libri;

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

	public String getNazionalità() {
		return nazionalità;
	}

	public void setNazionalità(String nazionalità) {
		this.nazionalità = nazionalità;
	}

	public Picture getImmagine() {
		return immagine;
	}

	public void setImmagine(Picture immagine) {
		this.immagine = immagine;
	}

	public List<Book> getLibri() {
		return libri;
	}

	public void setLibri(List<Book> libri) {
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
				+ dataMorte + ", nazionalità=" + nazionalità + "]";
	}
	
	
	
}
