package tn.esprit.pfe.interfaces;

import java.util.List;
import java.util.Set;

import javax.ejb.Remote;
import tn.esprit.pfe.entities.Entreprise;
import tn.esprit.pfe.entities.EntrepriseStudent;
import tn.esprit.pfe.entities.EntrepriseSupervisor;
import tn.esprit.pfe.entities.InternshipCataloge;
import tn.esprit.pfe.entities.InternshipOffer;
import tn.esprit.pfe.entities.JobOffer;
import tn.esprit.pfe.entities.Packs;
import tn.esprit.pfe.entities.ResponsableEntreprise;
import tn.esprit.pfe.entities.User;
import utilities.ValidationError;


@Remote
public interface EntrepriseServiceRemote {
	

	
	/* Entreprise */
	public int addEntreprise(Entreprise ent, int id);
	public void addEntreprisetoResponsable(int idR, int idEnt);
	public void updateEntreprise(Entreprise ent);
	public void deleteEntreprise(int id);
	public Entreprise getEntrepriseDetails(int idEnt);
	public List<Entreprise> getallEntreprises();
	public String Email(int id);
	public void addpacktoEntreprise(int idEnt,int idP);
	public Long nbrOffredestage(int idEnt);
	public Long nbrOffredetr(int idEnt);
	public Long nbrcatalog(int idEnt);
	public Long nbrsupervisor(int idEnt);
	
	
	/* InternshipOffer */
	public int addInternshipOffer(InternshipOffer inoff);
	public void addInternshipOffertoEntreprise(int idEnt, int idIoffer);
	public void updateInternshipOffer(InternshipOffer inoff);
	public void deleteInternshipOffer(int inoff);
	public InternshipOffer getInternshipOfferDetails(int idioff);
	public void addInternshipOffertoCataloge(int idCat, int idJoffer);
	public List<InternshipOffer> getAllIntershipOfferByEntreprise(int idE);
	public List<InternshipOffer> getAllIntershipOfferToday() ;
	
	/* Supervisor */
	public int addSupervisor(EntrepriseSupervisor es);
	public void addSupervisortoEntreprise(int idEnt, int idSuper);
	public void updateSupervisor(EntrepriseSupervisor es);
	public void deleteSupervisor(int ides);
	public EntrepriseSupervisor getEntrepriseSupervisor(int ides);
	public List<EntrepriseSupervisor> getAllEntrepriseSupervisorByEntreprise(int idE);
	
	/* JobOffre */
	public int addJobOffre(JobOffer jo);
	public void addJobOffretoEntreprise(int idEnt, int idJo);
	public void updateJobOffre(JobOffer jo);
	public void deleteJobOffre(int idjo);
	public JobOffer getJobOfferDetails(int idJo);
	public void addJobOffretoCataloge(int idCat, int idJo);
	public List<JobOffer> getAllJobOfferToday();
	public List<JobOffer> getAllJobOfferByEntreprise(int idE);
	
	/* InternshipCatalog */
	public int addInternshipCatalog(InternshipCataloge ic);
	public void addInternshipCatalogtoEntreprise(int idEnt, int ic);
	public void updateInternshipCatalog(InternshipCataloge ic);
	public InternshipCataloge getInternshipCatalaogeDetails(int idCat);
	public List<InternshipCataloge> getAllInternshipCatalogeByEntreprise(int idE);
	
	/* Student */
	public void affectStudenttoEntreprise(int idEnt, int ids);
	
	public String updatepack(int idEnt,int idP);
	public Packs getpacksDetails(int idEnt);
	public int getpacks(int idEnt);
	
}
