package tn.esprit.pfe.interfaces;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import tn.esprit.pfe.entities.Soutenance;

@Remote
public interface SoutenanceServiceRemote {
	public void addSoutenance(Soutenance s);
	public void updateSoutenance(Soutenance s);
	public void deleteSoutenance(int id);
	
	
	
	//mohamed

	public List<Soutenance> afficherSoutenanceNonNote();
    public List<Float> MoyenneNote();
	void testNote(int ids, float notee, float note);
	List<Soutenance> afficherSoutenanceSelonEtudiant(String titre);
	
}
