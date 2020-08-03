package tn.esprit.pfe.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table
public class Classe implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Column
	private int id;
	
	@Column
	@NotNull
	private int numero;
	
	@Column
	@NotNull
	@Min(value=2019)
	@Max(value=2100)
	private int anneeDeDebut;

	@ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JsonIgnore
	private Specialite specialite;
	
	
	@OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,mappedBy="classe")
	@JsonManagedReference
	private Set<Etudiant> etudiants = new HashSet<>();

	public Classe() {
		super();
	}

	public Classe(int numero, int anneeDeDebut) {
		super();
		this.numero = numero;
		this.anneeDeDebut = anneeDeDebut;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public Set<Etudiant> getEtudiants() {
		return etudiants;
	}

	public void setEtudiants(Set<Etudiant> etudiants) {
		this.etudiants = etudiants;
	}

	public Specialite getSpecialite() {
		return specialite;
	}

	public void setSpecialite(Specialite specialite) {
		this.specialite = specialite;
	}

	public int getAnneeDeDebut() {
		return anneeDeDebut;
	}

	public void setAnneeDeDebut(int anneeDeDebut) {
		this.anneeDeDebut = anneeDeDebut;
	}

	

}
