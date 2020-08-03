package tn.esprit.pfe.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name= "Packs")
public class Packs implements Serializable {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column
	private int id;
	
	@Column
	private String Nom;
	
	@Column
	private String Description;
	
	@OneToMany(mappedBy="Packs", cascade = {CascadeType.ALL}, 
			fetch=FetchType.EAGER)
	private Set<Entreprise> entreprise;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return Nom;
	}

	public void setNom(String nom) {
		Nom = nom;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	
	public Set<Entreprise> getEntreprise() {
		return entreprise;
	}

	public void setEntreprise(Set<Entreprise> entreprise) {
		this.entreprise = entreprise;
	}

	public Packs(int id, String nom, String description) {
		super();
		this.id = id;
		Nom = nom;
		Description = description;
	}

	public Packs(String nom, String description) {
		super();
		Nom = nom;
		Description = description;
		
	}

	public Packs() {
		super();
	}

	
	
	
}
