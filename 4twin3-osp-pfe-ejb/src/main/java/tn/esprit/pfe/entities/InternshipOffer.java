package tn.esprit.pfe.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name= "InternshipOffer")
public class InternshipOffer implements Serializable {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column
	private int id;
	
	@Column
	private String OffreName;
	
	@Column
	private String Description;

	@Temporal (TemporalType.DATE)
	private Date dateDebut;
	
	@Temporal (TemporalType.DATE)
	private Date dateFin;
	
	@Temporal (TemporalType.DATE)
	private Date datePublier;
	
	@ManyToOne
	Entreprise entreprise;
	
	@ManyToOne
	InternshipCataloge internshipCataloge;
	
	@Column
	private String Sujet;
	
	@Column
	private String Reference;
	
	@Column
	private String Objectifdustage;
	
	@Column
	private String Mission;
	
	@Column
	private String Profilrecherche;
	
	@Column
	private String CompétencesetConnaissances;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOffreName() {
		return OffreName;
	}

	public void setOffreName(String offreName) {
		OffreName = offreName;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}
	@JsonIgnore
	public Entreprise getEntreprise() {
		return entreprise;
	}

	public void setEntreprise(Entreprise entreprise) {
		this.entreprise = entreprise;
	}

	public String getSujet() {
		return Sujet;
	}

	public void setSujet(String sujet) {
		Sujet = sujet;
	}

	public String getReference() {
		return Reference;
	}

	public void setReference(String reference) {
		Reference = reference;
	}

	public String getObjectifdustage() {
		return Objectifdustage;
	}

	public void setObjectifdustage(String objectifdustage) {
		Objectifdustage = objectifdustage;
	}

	public String getMission() {
		return Mission;
	}

	public void setMission(String mission) {
		Mission = mission;
	}

	public String getProfilrecherche() {
		return Profilrecherche;
	}

	public void setProfilrecherche(String profilrecherche) {
		Profilrecherche = profilrecherche;
	}

	public String getCompétencesetConnaissances() {
		return CompétencesetConnaissances;
	}

	public void setCompétencesetConnaissances(String compétencesetConnaissances) {
		CompétencesetConnaissances = compétencesetConnaissances;
	}

	public Date getDatePublier() {
		return datePublier;
	}

	public void setDatePublier(Date datePublier) {
		this.datePublier = datePublier;
	}

	

	public InternshipCataloge getInternshipCataloge() {
		return internshipCataloge;
	}

	public void setInternshipCataloge(InternshipCataloge internshipCataloge) {
		this.internshipCataloge = internshipCataloge;
	}

	public InternshipOffer(String offreName, String description, Date dateDebut, Date dateFin, Date datePublier,
			String sujet, String reference, String objectifdustage, String mission, String profilrecherche,
			String compétencesetConnaissances) {
		super();
		OffreName = offreName;
		Description = description;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.datePublier = datePublier;
		Sujet = sujet;
		Reference = reference;
		Objectifdustage = objectifdustage;
		Mission = mission;
		Profilrecherche = profilrecherche;
		CompétencesetConnaissances = compétencesetConnaissances;
	}

	public InternshipOffer(int id, String offreName, String description, Date dateDebut, Date dateFin, Date datePublier,
			String sujet, String reference, String objectifdustage, String mission, String profilrecherche,
			String compétencesetConnaissances) {
		super();
		this.id = id;
		OffreName = offreName;
		Description = description;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.datePublier = datePublier;
		Sujet = sujet;
		Reference = reference;
		Objectifdustage = objectifdustage;
		Mission = mission;
		Profilrecherche = profilrecherche;
		CompétencesetConnaissances = compétencesetConnaissances;
	}

	public InternshipOffer() {
		super();
	}
	
	
	
}
