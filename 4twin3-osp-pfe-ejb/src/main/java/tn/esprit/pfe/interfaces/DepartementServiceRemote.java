package tn.esprit.pfe.interfaces;

import java.util.Set;

import javax.ejb.Remote;

import tn.esprit.pfe.entities.Departement;
import utilities.ValidationError;

@Remote
public interface DepartementServiceRemote {
	public Set<ValidationError> addDepartement(Departement d,int idSite, int idAdmin);
	public Set<ValidationError> modifierDepartement(Departement d,int idDepartement, int idAdmin);
	public Set<ValidationError> supprimerDepartement(int idDepartement, int idAdmin);
	public Departement getDepartement(int idDepartement);
	public Set<Departement> getListDepartement(int idSite);
}
