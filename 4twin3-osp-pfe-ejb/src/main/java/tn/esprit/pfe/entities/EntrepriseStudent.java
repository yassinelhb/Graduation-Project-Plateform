package tn.esprit.pfe.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name= "EntrepriseStudent")
public class EntrepriseStudent implements Serializable {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column
	private int id;
	
	@Column
	private int idStudent;
	
	@ManyToOne
	Entreprise entreprise ;
	
	@ManyToOne
	EntrepriseSupervisor entrepriseSupervisor ;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdStudent() {
		return idStudent;
	}
	public void setIdStudent(int idStudent) {
		this.idStudent = idStudent;
	}
	public Entreprise getEntreprise() {
		return entreprise;
	}
	public void setEntreprise(Entreprise entreprise) {
		entreprise = entreprise;
	}
	public EntrepriseSupervisor getEntrepriseSupervisor() {
		return entrepriseSupervisor;
	}
	public void setEntrepriseSupervisor(EntrepriseSupervisor entrepriseSupervisor) {
		entrepriseSupervisor = entrepriseSupervisor;
	}
	public EntrepriseStudent(int id, int idStudent) {
		super();
		this.id = id;
		this.idStudent = idStudent;
	}
	public EntrepriseStudent(int idStudent) {
		super();
		this.idStudent = idStudent;
	}
	public EntrepriseStudent() {
		super();
	}
	
	
}
