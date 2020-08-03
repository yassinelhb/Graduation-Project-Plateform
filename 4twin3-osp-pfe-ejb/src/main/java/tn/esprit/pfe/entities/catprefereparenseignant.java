/*package tn.esprit.pfe.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;



@Entity

public class catprefereparenseignant implements Serializable {
	
	private static final long serialVersionUID = 3876346912862238239L;
	@ManyToOne
    @JoinColumn(name = "idEnseignant", referencedColumnName = "id", insertable=false, updatable=false)
	private Enseignant enseignant;
	
	@ManyToOne
    @JoinColumn(name = "idCategorie", referencedColumnName = "id", insertable=false, updatable=false)
	private Categorie categorie;
	
	private boolean prefere;

	public Enseignant getEnseignant() {
		return enseignant;
	}

	public void setEnseignant(Enseignant enseignant) {
		this.enseignant = enseignant;
	}

	public Categorie getCategorie() {
		return categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	public boolean isPrefere() {
		return prefere;
	}

	public void setPrefere(boolean prefere) {
		this.prefere = prefere;
	}
	
}
*/