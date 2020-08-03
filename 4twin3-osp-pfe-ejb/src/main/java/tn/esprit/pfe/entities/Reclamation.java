package tn.esprit.pfe.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;






@Entity
public class Reclamation implements Serializable{
	
	private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idReclamation;
	
	private String textRec;
	
	@OneToOne
	private Etudiant etudiant;
	
	@OneToOne
	private Soutenance soutenance;
	
	@Temporal (TemporalType.DATE)
	private Date dateAjout;
	

	public Reclamation(int idReclamation, String textRec, Etudiant etudiant, Date dateAjout) {
		super();
		this.idReclamation = idReclamation;
		this.textRec = textRec;
		this.etudiant = etudiant;
		this.dateAjout = dateAjout;
	}




	public Date getDateAjout() {
		return dateAjout;
	}




	public void setDateAjout(Date dateAjout) {
		this.dateAjout = dateAjout;
	}




	public int getIdReclamation() {
		return idReclamation;
	}
	

	

	public Reclamation() {
		super();
	}




	public String getTextRec() {
		return textRec;
	}

	public void setTextRec(String textRec) {
		this.textRec = textRec;
	}

	public Etudiant getEtudiant() {
		return etudiant;
	}

	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}



	public void setIdReclamation(int idReclamation) {
		this.idReclamation = idReclamation;
	}

    

	public Reclamation(int idReclamation, String textRec, Etudiant etudiant) {
		super();
		this.idReclamation = idReclamation;
		this.textRec = textRec;
		this.etudiant = etudiant;
	}
	



	




	@Override
	public String toString() {
		return "Reclamation [idReclamation=" + idReclamation + ", textRec=" + textRec + ", etudiant=" + etudiant
				+ ", soutenance=" + soutenance + ", dateAjout=" + dateAjout + "]";
	}




	public Soutenance getSoutenance() {
		return soutenance;
	}




	public void setSoutenance(Soutenance soutenance) {
		this.soutenance = soutenance;
	}




	public Reclamation(int idReclamation, String textRec, Etudiant etudiant, Soutenance soutenance, Date dateAjout) {
		super();
		this.idReclamation = idReclamation;
		this.textRec = textRec;
		this.etudiant = etudiant;
		this.soutenance = soutenance;
		this.dateAjout = dateAjout;
	}
	
	
	
	
	
	

	

	
	
}
