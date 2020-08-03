package tn.esprit.pfe.interfaces;

import java.util.Set;

import javax.ejb.Remote;

import tn.esprit.pfe.entities.Classe;
import utilities.ValidationError;

@Remote
public interface ClasseServiceRemote {
	public Set<ValidationError> addClasse(int idSpecialite, int idAdmin);
	public Set<ValidationError> modifierClasse(Classe c,int idClasse, int idAdmin);
	public Set<ValidationError> supprimerClasse(int idClasse, int idAdmin);
	public Classe getClasse(int idClasse);
	public Set<Classe> getListClasse(int idSpecialite);
	Set<Classe> getListClasseAnnee(int idSpecialite, int annee);
}
