package tn.esprit.pfe.interfaces;

import java.util.Set;

import javax.ejb.Remote;

import tn.esprit.pfe.entities.Specialite;
import utilities.ValidationError;

@Remote
public interface SpecialiteServiceRemote {
	public Set<ValidationError> addSpecialite(Specialite s,int idDepartement, int idAdmin);
	public Set<ValidationError> modifierSpecialite(Specialite s,int idSpecialite, int idAdmin);
	public Set<ValidationError> supprimerSpecialite(int idSpecialite, int idAdmin);
	public Specialite getSpecialite(int idSpecialite);
	public Set<Specialite> getListSpecialite(int idDepartement);
}
