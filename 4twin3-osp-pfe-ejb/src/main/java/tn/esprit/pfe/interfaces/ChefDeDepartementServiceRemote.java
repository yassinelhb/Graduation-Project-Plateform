package tn.esprit.pfe.interfaces;

import java.util.Set;

import javax.ejb.Remote;

import utilities.ValidationError;

@Remote
public interface ChefDeDepartementServiceRemote {
	public Set<ValidationError> affecterChefDeDepartement(int idEnseignant, int idDepartement, int idAdmin);

}
