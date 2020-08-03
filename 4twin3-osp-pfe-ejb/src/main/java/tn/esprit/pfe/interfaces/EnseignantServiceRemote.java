package tn.esprit.pfe.interfaces;
import java.util.Set;

import javax.ejb.Remote;

import tn.esprit.pfe.entities.Enseignant;
import utilities.ValidationError;

@Remote
public interface EnseignantServiceRemote {
	public Set<ValidationError> addEnseignant(Enseignant e, int idSite, int idAdmin);
	public Set<Enseignant> getListEnseignant(int idUser);
	public Set<ValidationError> supprimerEnseignant(int idEnseignant, int idAdmin);
}
