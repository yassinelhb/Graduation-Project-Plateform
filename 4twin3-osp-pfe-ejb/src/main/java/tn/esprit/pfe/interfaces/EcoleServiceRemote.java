package tn.esprit.pfe.interfaces;

import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import tn.esprit.pfe.entities.Ecole;
import utilities.ValidationError;

@Remote
public interface EcoleServiceRemote {
	public Set<ValidationError> addEcole(Ecole e,int idAdmin);
	public Set<ValidationError> modifierEcole(Ecole ecole, int idAdmin, int idEcole);
	public Set<ValidationError> supprimerEcole(int idEcole, int idAdmin);
	public Ecole getEcole(int idEcole, int idAdmin);
	public List<Ecole> getListEcole();
	public Set<ValidationError> addImage(String image, int idAdmin);
	public Ecole getEcoleAdmin(int idAdmin);
}
