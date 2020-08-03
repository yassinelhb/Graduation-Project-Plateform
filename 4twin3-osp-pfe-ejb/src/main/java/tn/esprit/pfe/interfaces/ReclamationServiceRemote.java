package tn.esprit.pfe.interfaces;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import tn.esprit.pfe.entities.Etudiant;
import tn.esprit.pfe.entities.Reclamation;
import tn.esprit.pfe.entities.User;


@Remote
public interface ReclamationServiceRemote {

	
	public int addReclamation(Reclamation rec);
	public void updateReclamation(Reclamation rec);
	public void deleteReclamation(int id);
     public List<Reclamation> getAllReclamation();
     public List<Reclamation> getReclamationByEtudiant(String nom , String prenom);
     public List<Reclamation> getReclamationBySoutenance(int id);
     public  List<Float> nombreDeReclamationSelonDateAjout();
     public  List<Object[]> nombreDeReclamationParMois();
	//afficher les soutenances non noté (done ) + notifier l'enseignant affecte
	//afficher les soutenances qui attend un rapporteur
	//notifier l'etudiant lors de l'ajout d'une note //table notifiacation 
     //stat note (moyenne note , nbrsoutenance note et non note ,  etudiant qui ont eu une mension tres bien ) 
  
	
	
}
