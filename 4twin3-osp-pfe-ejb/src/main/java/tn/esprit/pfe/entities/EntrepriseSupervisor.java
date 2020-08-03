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
@Table(name= "EntrepriseSupervisor")
public class EntrepriseSupervisor implements Serializable {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column
	private int id;
	
	@Column
	private String NomPrenom;
	
	@Column
	private String Tel;
	
	@Column
	private String Email;
	
	@Column
	private String Specialite;
	
	@Column
	private String SpecialiteOptionnel;
	
	@ManyToOne
	Entreprise entreprise;

	@OneToMany(mappedBy="entrepriseSupervisor", cascade = {CascadeType.ALL}, 
			fetch=FetchType.EAGER)
	private Set<EntrepriseStudent > entreprisestudents;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNomPrenom() {
		return NomPrenom;
	}

	public void setNomPrenom(String nomPrenom) {
		NomPrenom = nomPrenom;
	}

	public String getTel() {
		return Tel;
	}

	public void setTel(String tel) {
		Tel = tel;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getSpecialite() {
		return Specialite;
	}

	public void setSpecialite(String specialite) {
		Specialite = specialite;
	}

	public String getSpecialiteOptionnel() {
		return SpecialiteOptionnel;
	}

	public void setSpecialiteOptionnel(String specialiteOptionnel) {
		SpecialiteOptionnel = specialiteOptionnel;
	}
	
	@JsonIgnore
	public Entreprise getEntreprise() {
		return entreprise;
	}

	public void setEntreprise(Entreprise entreprise) {
		this.entreprise = entreprise;
	}

	public Set<EntrepriseStudent> getEntrepriseStudents() {
		return entreprisestudents;
	}

	public void setEntrepriseStudents(Set<EntrepriseStudent> entreprisestudents) {
		this.entreprisestudents = entreprisestudents;
	}

	public EntrepriseSupervisor(int id, String nomPrenom, String tel, String email, String specialite,
			String specialiteOptionnel) {
		super();
		this.id = id;
		NomPrenom = nomPrenom;
		Tel = tel;
		Email = email;
		Specialite = specialite;
		SpecialiteOptionnel = specialiteOptionnel;
	}

	public EntrepriseSupervisor(String nomPrenom, String tel, String email, String specialite,
			String specialiteOptionnel) {
		super();
		NomPrenom = nomPrenom;
		Tel = tel;
		Email = email;
		Specialite = specialite;
		SpecialiteOptionnel = specialiteOptionnel;
	}

	public EntrepriseSupervisor() {
		super();
	}

	
	
}
