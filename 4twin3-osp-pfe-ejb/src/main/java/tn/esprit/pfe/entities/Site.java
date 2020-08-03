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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table
public class Site implements Serializable {

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
	@NotBlank
	private String nom;

	@Column
	@NotNull
	@NotBlank
	private String adresse;
	
	@NotNull
	@Min(value=0)
	@Max(value=100)
	@Column
	private int maxPreValidateur = 5;
	
	@NotNull
	@Min(value=0)
	@Max(value=100)
	@Column
	private int maxEncadrant = 5;
	
	@NotNull
	@Min(value=0)
	@Max(value=100)
	@Column
	private int maxRapporteur = 5;
	
	@NotNull
	@Min(value=0)
	@Max(value=100)
	@Column
	private int maxPresident = 5;

	@OneToOne
	private Enseignant directeurDesStages;

	@ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JsonIgnore
	private Ecole ecole;
	
	@OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,mappedBy="site")
	private Set<Departement> departements = new HashSet<>();

	public Site() {
		super();
	}

	public Site(String nom, String adresse, int maxPreValidateur, int maxEncadrant, int maxRapporteur, int maxPresident,
			Enseignant directeurDesStages, Ecole ecole, Set<Departement> departements) {
		super();
		this.nom = nom;
		this.adresse = adresse;
		this.maxPreValidateur = maxPreValidateur;
		this.maxEncadrant = maxEncadrant;
		this.maxRapporteur = maxRapporteur;
		this.maxPresident = maxPresident;
		this.directeurDesStages = directeurDesStages;
		this.ecole = ecole;
		this.departements = departements;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public Enseignant getDirecteurDesStages() {
		return directeurDesStages;
	}

	public void setDirecteurDesStages(Enseignant directeurDesStages) {
		this.directeurDesStages = directeurDesStages;
	}

	public Ecole getEcole() {
		return ecole;
	}

	public void setEcole(Ecole ecole) {
		this.ecole = ecole;
	}

	public Set<Departement> getDepartements() {
		return departements;
	}

	public void setDepartements(Set<Departement> departements) {
		this.departements = departements;
	}



	public int getMaxPreValidateur() {
		return maxPreValidateur;
	}



	public void setMaxPreValidateur(int maxPreValidateur) {
		this.maxPreValidateur = maxPreValidateur;
	}



	public int getMaxEncadrant() {
		return maxEncadrant;
	}



	public void setMaxEncadrant(int maxEncadrant) {
		this.maxEncadrant = maxEncadrant;
	}



	public int getMaxRapporteur() {
		return maxRapporteur;
	}



	public void setMaxRapporteur(int maxRapporteur) {
		this.maxRapporteur = maxRapporteur;
	}



	public int getMaxPresident() {
		return maxPresident;
	}



	public void setMaxPresident(int maxPresident) {
		this.maxPresident = maxPresident;
	}
	
	
	

}
