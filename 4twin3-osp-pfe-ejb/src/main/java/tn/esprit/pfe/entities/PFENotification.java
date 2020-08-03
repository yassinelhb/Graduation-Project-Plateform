package tn.esprit.pfe.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name="PFENotification")
public class PFENotification implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;
	private String title;
	private String note;
	@Temporal (TemporalType.DATE)
	private Date created;
	private int vu;
	private String sendby;
	@Enumerated(EnumType.STRING)
	private TypeNotification type;
	@ManyToOne
	private Enseignant enseignant;
	@ManyToOne
	private Etudiant etudiant;


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
	public int getVu() {
		return vu;
	}
	public void setVu(int vu) {
		this.vu = vu;
	}

	public Enseignant getEnseignant() {
		return enseignant;
	}
	public void setEnseignant(Enseignant enseignant) {
		this.enseignant = enseignant;
	}
	public Etudiant getEtudiant() {
		return etudiant;
	}
	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}
	public String getSendby() {
		return sendby;
	}
	public void setSendby(String sendby) {
		this.sendby = sendby;
	}
	public TypeNotification getType() {
		return type;
	}
	public void setType(TypeNotification type) {
		this.type = type;
	}
	
	
}
