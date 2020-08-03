package tn.esprit.pfe.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="SheetPFEModification")
public class SheetPFEModification implements Serializable  {
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)	
	private int id;
	@JsonIgnore
	@ManyToOne
	private SheetPFE sheetPFE;
	private String title;
	private String description;
	private String problematic;
	@ManyToOne
	private Entreprise entreprise;
	private String features;
	@Enumerated(EnumType.STRING)
	private EtatSheetPFE etat;
	private String note;
	@Temporal (TemporalType.DATE)
	private Date created;
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Categorie> categories = new HashSet<Categorie>();
	
	
	public SheetPFEModification() {
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public SheetPFE getSheetPFE() {
		return sheetPFE;
	}
	public void setSheetPFE(SheetPFE sheetPFE) {
		this.sheetPFE = sheetPFE;
	}
	public String getProblematic() {
		return problematic;
	}
	public void setProblematic(String problematic) {
		this.problematic = problematic;
	}
	public String getFeatures() {
		return features;
	}
	public void setFeatures(String features) {
		this.features = features;
	}
	public EtatSheetPFE getEtat() {
		return etat;
	}
	public void setEtat(EtatSheetPFE etat) {
		this.etat = etat;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Entreprise getEntreprise() {
		return entreprise;
	}

	public void setEntreprise(Entreprise entreprise) {
		this.entreprise = entreprise;
	}


	public Set<Categorie> getCategories() {
		return categories;
	}


	public void setCategories(Set<Categorie> categories) {
		this.categories = categories;
	}


	
	
	
	
	

}
