package tn.esprit.pfe.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Soutenance")
public class Soutenance implements Serializable {
	
	
	
	
	
	@OneToOne(mappedBy="S")
	private Etudiant E;

	

	public List<Jury> getJury() {
		return jury;
	}

	public void setJury(List<Jury> jury) {
		this.jury = jury;
	}

	/**
	 * 
	 */
	
	@OneToMany(mappedBy="soutenance")
	private List<Jury> jury = new ArrayList<Jury>();
	private static final long serialVersionUID = 1L;

	public Soutenance(String titre, String description, Date dateSoutenance, String salle, Date heureSoutenance,int noteSoutenance,List<Jury> jury) {
		Titre = titre;
		Description = description;
		this.dateSoutenance = dateSoutenance;
		Salle = salle;
		HeureSoutenance = heureSoutenance;
		NoteSoutenance = noteSoutenance;
		
	}
	public Soutenance(String titre, String description, Date dateSoutenance, String salle, Date heureSoutenance) {
		Titre = titre;
		Description = description;
		this.dateSoutenance = dateSoutenance;
		Salle = salle;
		HeureSoutenance = heureSoutenance;
	
	}
	
	public Soutenance(int id, float noteSoutenance) {
		super();
		this.id = id;
		NoteSoutenance = noteSoutenance;
	}

	public Soutenance() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private int id;

	@Column
	private String Titre;

	@Column
	private String Description;

	@Temporal(TemporalType.DATE)
	private Date dateSoutenance;

	@Column
	private String Salle;
	@Temporal(TemporalType.DATE)
	private Date HeureSoutenance;
	@Column
	private float NoteSoutenance;
	
	



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitre() {
		return Titre;
	}

	public void setTitre(String titre) {
		Titre = titre;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public Date getDateSoutenance() {
		return dateSoutenance;
	}

	public void setDateSoutenance(Date dateSoutenance) {
		this.dateSoutenance = dateSoutenance;
	}

	public String getSalle() {
		return Salle;
	}

	public void setSalle(String salle) {
		Salle = salle;
	}

	public Date getHeureSoutenance() {
		return HeureSoutenance;
	}

	public void setHeureSoutenance(Date heureSoutenance) {
		HeureSoutenance = heureSoutenance;
	}

	public float getNoteSoutenance() {
		return NoteSoutenance;
	}

	public void setNoteSoutenance(float noteSoutenance) {
		NoteSoutenance = noteSoutenance;
	}



}
