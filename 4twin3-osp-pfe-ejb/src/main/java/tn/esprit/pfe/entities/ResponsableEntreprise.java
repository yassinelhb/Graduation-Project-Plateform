package tn.esprit.pfe.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name= "ResponsableEntreprise")
public class ResponsableEntreprise extends User{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Entreprise entreprise;

	public ResponsableEntreprise() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResponsableEntreprise(String nom, String prenom, String email, String password, String role) {
		super(nom, prenom, email, password, role);
		// TODO Auto-generated constructor stub
	}

	public ResponsableEntreprise(String nom, String prenom, String email, String plainPassword) {
		super(nom, prenom, email, plainPassword);
		// TODO Auto-generated constructor stub
	}

	public Entreprise getEntreprise() {
		return entreprise;
	}

	public void setEntreprise(Entreprise entreprise) {
		this.entreprise = entreprise;
	}

	
	
}
