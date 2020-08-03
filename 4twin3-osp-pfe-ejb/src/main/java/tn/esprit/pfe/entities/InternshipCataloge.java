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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name= "InternshipCataloge")
public class InternshipCataloge implements Serializable{
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column
	private int id;
	
	@Column
	private String CatalogName;
	
	@Column
	private String Description;
	
	@OneToMany(mappedBy="internshipCataloge", cascade = {CascadeType.ALL}, 
			fetch=FetchType.EAGER)
	private Set<JobOffer> joboffers;

	@OneToMany(mappedBy="internshipCataloge", cascade = {CascadeType.ALL}, 
			fetch=FetchType.EAGER)
	private Set<InternshipOffer> internshipoffers;
	
	@ManyToOne
	Entreprise entreprise;
	
	@JsonIgnore
	public Entreprise getEntreprise() {
		return entreprise;
	}

	public void setEntreprise(Entreprise entreprise) {
		this.entreprise = entreprise;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCatalogName() {
		return CatalogName;
	}

	public void setCatalogName(String catalogName) {
		CatalogName = catalogName;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}
	
	@JsonIgnore
	public Set<JobOffer> getJobOffers() {
		return joboffers;
	}

	public void setJobOffers(Set<JobOffer> jobOffers) {
		this.joboffers = jobOffers;
	}

	@JsonIgnore
	public Set<InternshipOffer> getInternshipOffers() {
		return internshipoffers;
	}

	public void setInternshipOffers(Set<InternshipOffer> internshipOffers) {
		this.internshipoffers = internshipOffers;
	}

	public InternshipCataloge(int id, String catalogName, String description) {
		super();
		this.id = id;
		CatalogName = catalogName;
		Description = description;
	}

	public InternshipCataloge(String catalogName, String description) {
		super();
		CatalogName = catalogName;
		Description = description;
	}

	public InternshipCataloge() {
		super();
	}

	
	
	
}
