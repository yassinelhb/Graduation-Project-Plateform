package tn.esprit.pfe.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity(name = "SheetPFE")
public class SheetPFE implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String title;
	private String description;
	private String problematic;
	private String features;
	@ManyToMany(fetch = FetchType.EAGER)
/*<<<<<<< HEAD
	private Set<Categorie> categories;
	
=======*/
	private Set<Categorie> categories = new HashSet<Categorie>();
//>>>>>>> 8610228526b0e04c6b937d08f0a91a08c6a69bb3
	private String qrcode;
	@Enumerated(EnumType.STRING)
	private EtatSheetPFE etat;
	private String note;
	private float noteEncadreur;
	private float noteRapporteur;
	private String pdf;
	
	@ManyToOne
	private Entreprise entreprise;
	
	@OneToOne
	private Etudiant etudiant;
	
	@OneToMany(mappedBy="sheetPFE",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<EnseignantSheetPFE> enseignantsheet = new HashSet<EnseignantSheetPFE>();
	
	@OneToMany(mappedBy="sheetPFE",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<SheetPFEModification> sheetPFEModifications = new HashSet<SheetPFEModification>();
	
	@JsonIgnore
	@OneToOne(mappedBy="sheetPFE")
	private RequestCancelInternship request;

	public SheetPFE() {
	}

	public SheetPFE(String title, String description, String problematic, String features) {

		this.title = title;
		this.description = description;
		this.problematic = problematic;
		this.features = features;
/*<<<<<<< HEAD
		
	}
=======*/
	}

//>>>>>>> 8610228526b0e04c6b937d08f0a91a08c6a69bb3
	public SheetPFE(int id, String title, String description, String problematic, String features) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.problematic = problematic;
		this.features = features;
/*<<<<<<< HEAD
		
=======*/
//>>>>>>> 8610228526b0e04c6b937d08f0a91a08c6a69bb3
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

/*<<<<<<< HEAD
	public Set<Categorie> getCategories() {
		return categories;
	}

	public void setCategories(Set<Categorie> categories) {
		this.categories = categories;
	}

=======*/
//>>>>>>> 8610228526b0e04c6b937d08f0a91a08c6a69bb3
	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
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

	public Entreprise getEntreprise() {
		return entreprise;
	}

	public void setEntreprise(Entreprise entreprise) {
		this.entreprise = entreprise;
	}

	public Etudiant getEtudiant() {
		return etudiant;
	}

	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}

	public Set<Categorie> getCategories() {
		return categories;
	}

	public void setCategories(Set<Categorie> set) {
		this.categories = set;
	}

	public Set<EnseignantSheetPFE> getEnseignantsheet() {
		return enseignantsheet;
	}

	public void setEnseignantsheet(Set<EnseignantSheetPFE> enseignantsheet) {
		this.enseignantsheet = enseignantsheet;
	}


	public RequestCancelInternship getRequest() {
		return request;
	}

	public void setRequest(RequestCancelInternship request) {
		this.request = request;
	}

	public Set<SheetPFEModification> getSheetPFEModifications() {
		return sheetPFEModifications;
	}

	public void setSheetPFEModifications(Set<SheetPFEModification> sheetPFEModifications) {
		this.sheetPFEModifications = sheetPFEModifications;
	}

	public float getNoteEncadreur() {
		return noteEncadreur;
	}

	public void setNoteEncadreur(float noteEncadreur) {
		this.noteEncadreur = noteEncadreur;
	}

	public float getNoteRapporteur() {
		return noteRapporteur;
	}

	public void setNoteRapporteur(float noteRapporteur) {
		this.noteRapporteur = noteRapporteur;
	}

	public String getPdf() {
		return pdf;
	}

	public void setPdf(String pdf) {
		this.pdf = pdf;
	}

	
	
	
	
	

	
	
}
