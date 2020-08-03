package tn.esprit.pfe.entities;

/*<<<<<<< HEAD
=======*/

import java.util.HashSet;
import java.util.Set;
//>>>>>>> 8610228526b0e04c6b937d08f0a91a08c6a69bb3

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Etudiant", uniqueConstraints = { @UniqueConstraint(columnNames = "identifiant", name = "uniqueIdentifiantConstraint") })
@PrimaryKeyJoinColumn(name = "id")
public class Etudiant extends User {

	private static final long serialVersionUID = 1L;
	
	@OneToOne
	private Soutenance S;
	


	@Column
	@NotBlank
	private String identifiant;

	@JsonIgnore
	@OneToOne(mappedBy="etudiant")
	private InternshipAgreemen internshipAgreemen;
	
/*<<<<<<< HEAD
	 @OneToMany(mappedBy="etudiant", cascade = {CascadeType.ALL}, 
		fetch=FetchType.EAGER)
      Set<ForumQuestion> fq;
=======*/
	@JsonIgnore
 	@OneToOne(mappedBy="etudiant")
	private SheetPFE sheetPFE;

	 //@OneToMany(mappedBy="etudiant", cascade = {CascadeType.ALL}, 
	//	fetch=FetchType.EAGER)
	//private List<ForumQuestion> fq=new ArrayList<>();
//>>>>>>> 8610228526b0e04c6b937d08f0a91a08c6a69bb3
	
	
	@JsonIgnore
	@OneToMany(mappedBy="etudiant",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<PFENotification> pfeNotifications = new HashSet<PFENotification>();
	

	//@OneToMany(mappedBy = "etudiant", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	// private List<ForumQuestion> fq=new ArrayList<>();

	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JsonBackReference
	private Classe classe;
	
	

	public Etudiant() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Etudiant(String lastname, String firstname, String email, String plainPassword, String identifiant) {
		super(lastname, firstname, email, plainPassword);
		this.identifiant = identifiant;
		// TODO Auto-generated constructor stub
	}



	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

	public InternshipAgreemen getInternshipAgreemen() {
		return internshipAgreemen;
	}

	public void setInternshipAgreemen(InternshipAgreemen internshipAgreemen) {
		this.internshipAgreemen = internshipAgreemen;
	}

	public SheetPFE getSheetPFE() {
		return sheetPFE;
	}

	public void setSheetPFE(SheetPFE sheetPFE) {
		this.sheetPFE = sheetPFE;
	}

/*<<<<<<< HEAD
	public Set<ForumQuestion> getFq() {
		return fq;
	}

	public void setFq(Set<ForumQuestion> fq) {
		this.fq = fq;
	}	
=======*/
	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	public Set<PFENotification> getPfeNotifications() {
		return pfeNotifications;
	}

	public void setPfeNotifications(Set<PFENotification> pfeNotifications) {
		this.pfeNotifications = pfeNotifications;
	}
	
	
	
	

	/*
	 * public List<ForumQuestion> getFq() { return fq; }
	 * 
	 * public void setFq(List<ForumQuestion> fq) { this.fq = fq; }
	 * 
	 */

//>>>>>>> 8610228526b0e04c6b937d08f0a91a08c6a69bb3
}
