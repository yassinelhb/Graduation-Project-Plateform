package tn.esprit.pfe.services;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import tn.esprit.pfe.entities.Entreprise;
import tn.esprit.pfe.entities.EntrepriseSupervisor;
import tn.esprit.pfe.entities.InternshipCataloge;
import tn.esprit.pfe.entities.InternshipOffer;
import tn.esprit.pfe.entities.JobOffer;
import tn.esprit.pfe.entities.Packs;
import tn.esprit.pfe.entities.ResponsableEntreprise;
import tn.esprit.pfe.entities.User;
import tn.esprit.pfe.interfaces.EntrepriseServiceRemote;

@Stateless
@LocalBean
public class EntrepriseServices implements EntrepriseServiceRemote {
	@PersistenceContext(unitName = "4twin3-osp-pfe-ejb")
	EntityManager em;
	
	/* Entreprise */
	
	public Long ValidateMail(String Email) {
		TypedQuery<Long> query = em.createQuery("select count(*) from Entreprise e where e.EmailEntreprise=:emailEnt", Long.class);
		query.setParameter("emailEnt",Email);
		Long nbr=query.getSingleResult();
		return nbr;
	}
	
	
	@Override
	public int addEntreprise(Entreprise ent, int id) {
		// TODO Auto-generated method stub
		if(ValidateMail(ent.getEmailEntreprise())==0) {
			ent.setXp(5);
			em.persist(ent);
			return ent.getId();
		}
		return 0;			
	}
	
	@Override
	public void addpacktoEntreprise(int idEnt,int idP) {
		Entreprise es = em.find(Entreprise.class, idEnt);
		Packs p = em.find(Packs.class, idP);
		es.setPacks(p);
	}
	
	@Override
	public void updateEntreprise(Entreprise ent) {
		// TODO Auto-generated method stub
		Entreprise en = em.find(Entreprise.class, ent.getId());
		en.setAdresse(ent.getAdresse());
		en.setEmailEntreprise(ent.getEmailEntreprise());
		en.setNameEntreprise(ent.getNameEntreprise());
		en.setPays(ent.getPays());
		en.setSiteweb(ent.getSiteweb());	
		en.setTelEntreprise(ent.getTelEntreprise());		
		en.setTelResponsable(ent.getTelResponsable());		
	}

	@Override
	public void deleteEntreprise(int id) {
		// TODO Auto-generated method stub
		Entreprise ent = em.find(Entreprise.class, id);	
		em.remove(ent);
	}

	@Override
	public Entreprise getEntrepriseDetails(int idEnt) {
		// TODO Auto-generated method stub
		return em.find(Entreprise.class, idEnt);
	}
	
	@Override
	public void addEntreprisetoResponsable(int idR, int idEnt) {
		// TODO Auto-generated method stub
		//User er = em.find(User.class, idR);
		Entreprise en = em.find(Entreprise.class, idEnt);
		ResponsableEntreprise r=em.find(ResponsableEntreprise.class,idR);
		r.setEntreprise(en);

	}
	
	@Override
	public String Email(int id) {
		User e = em.find(User.class, id);
		System.out.println(e.getEmail());
		return e.getEmail();
	}

	@Override
	public Long nbrOffredestage(int idEnt) {
		TypedQuery<Long> query =
				em.createQuery("select count(A) from InternshipOffre A where A.entreprise="+idEnt, Long.class);
		Long nbr=query.getSingleResult();
		return nbr;
	}
	
	@Override
	public Long nbrOffredetr(int idEnt) {
		TypedQuery<Long> query =
				em.createQuery("select count(A) from JobOffre A where A.entreprise="+idEnt, Long.class);
		Long nbr=query.getSingleResult();
		return nbr;
	}
	
	@Override
	public Long nbrcatalog(int idEnt) {
		TypedQuery<Long> query =
				em.createQuery("select count(A) from InternshipCataloge A where A.entreprise="+idEnt, Long.class);
		Long nbr=query.getSingleResult();
		return nbr;
	}
	
	@Override
	public Long nbrsupervisor(int idEnt) {
		TypedQuery<Long> query =
				em.createQuery("select count(A) from EntrepriseSupervisor A where A.entreprise="+idEnt, Long.class);
		Long nbr=query.getSingleResult();
		return nbr;
	}
	
	/* InternshipOffer */
	
	@Override
	public int addInternshipOffer(InternshipOffer inoff) {
		// TODO Auto-generated method stub
		Date date = new Date();
		inoff.setDatePublier(date);
		em.persist(inoff);
		return inoff.getId();	
	}
	
	@Override
	public void addInternshipOffertoEntreprise(int idEnt, int idIoffer) {
		// TODO Auto-generated method stub
		Entreprise ide = em.find(Entreprise.class, idEnt);
		InternshipOffer ioffer = em.find(InternshipOffer.class, idIoffer);
		int oxp = ide.getXp();
		int nxp = oxp+30;
		ide.setXp(nxp);
		ioffer.setEntreprise(ide);
	}

	@Override
	public void updateInternshipOffer(InternshipOffer inoff) {
		// TODO Auto-generated method stub
		InternshipOffer io = em.find(InternshipOffer.class, inoff.getId());
		io.setOffreName(inoff.getOffreName());
		io.setCompétencesetConnaissances(inoff.getCompétencesetConnaissances());
		io.setDescription(inoff.getDescription());
		io.setMission(inoff.getMission());
		io.setObjectifdustage(inoff.getObjectifdustage());
		io.setProfilrecherche(inoff.getProfilrecherche());
		io.setReference(inoff.getReference());
		io.setSujet(inoff.getSujet());
		io.setDateDebut(inoff.getDateDebut());
		io.setDateFin(inoff.getDateFin());	
	}

	@Override
	public void deleteInternshipOffer(int inoff) {
		// TODO Auto-generated method stub
		InternshipOffer io = em.find(InternshipOffer.class, inoff);
		em.remove(io);
	}

	@Override
	public InternshipOffer getInternshipOfferDetails(int idioff) {
		// TODO Auto-generated method stub
		return em.find(InternshipOffer.class, idioff);
	}
	
	@Override
	public void addInternshipOffertoCataloge(int idCat, int idJoffer) {
		InternshipCataloge ic = em.find(InternshipCataloge.class, idCat);
		InternshipOffer io = em.find(InternshipOffer.class, idJoffer);
		io.setInternshipCataloge(ic);
	}
	
	@Override
	public List<InternshipOffer> getAllIntershipOfferToday() {
		Date date= new Date();
		SimpleDateFormat formatter=new SimpleDateFormat("YYYY-MM-dd");
		TypedQuery<InternshipOffer> Q =em.createQuery("Select I from InternshipOffer I where MONTH(I.datePublier)=MONTH(:date)", InternshipOffer.class);
		Q.setParameter("date", formatter.format(date));
		
		List<InternshipOffer> E=Q.getResultList();
		return E;
	}
	
	@Override
	public List<InternshipOffer> getAllIntershipOfferByEntreprise(int idE) {
		TypedQuery<InternshipOffer> Q =em.createQuery("Select I from InternshipOffer I where I.entreprise="+idE, InternshipOffer.class);
		List<InternshipOffer> E=Q.getResultList();
		return E;
	}
	
	/* Supervisor */
	
	@Override
	public int addSupervisor(EntrepriseSupervisor es) {
		// TODO Auto-generated method stub
		if(ValidateMail(es.getEmail())==0) {
			em.persist(es);
			return es.getId();
		}
		return 0;
	}

	@Override
	public void addSupervisortoEntreprise(int idEnt, int idSuper) {
		// TODO Auto-generated method stub
		Entreprise ide = em.find(Entreprise.class, idEnt);
		EntrepriseSupervisor es = em.find(EntrepriseSupervisor.class, idSuper);
		int oxp = ide.getXp();
		int nxp = oxp+5;
		ide.setXp(nxp);
		es.setEntreprise(ide);
	}

	@Override
	public void updateSupervisor(EntrepriseSupervisor es) {
		// TODO Auto-generated method stub
		EntrepriseSupervisor se = em.find(EntrepriseSupervisor.class, es.getId());
		se.setEmail(es.getEmail());
		se.setNomPrenom(es.getNomPrenom());
		se.setSpecialite(es.getSpecialite());
		se.setSpecialiteOptionnel(es.getSpecialiteOptionnel());
		se.setTel(es.getTel());
	}

	@Override
	public void deleteSupervisor(int ides) {
		// TODO Auto-generated method stub
		EntrepriseSupervisor es = em.find(EntrepriseSupervisor.class, ides);
		em.remove(es);		
	}
	
	@Override
	public EntrepriseSupervisor getEntrepriseSupervisor(int ides) {
		return em.find(EntrepriseSupervisor.class, ides);
	}
	
	public List<EntrepriseSupervisor> getAllEntrepriseSupervisorByEntreprise(int idE){
		TypedQuery<EntrepriseSupervisor> Q =em.createQuery("Select I from EntrepriseSupervisor I where I.entreprise="+idE, EntrepriseSupervisor.class);
		List<EntrepriseSupervisor> E = Q.getResultList();
		return E;
		
	}
	
	
	/* JobOffre */
	
	@Override
	public int addJobOffre(JobOffer jo) {
		// TODO Auto-generated method stub
		Date date = new Date();
		jo.setDatePublier(date);
		em.persist(jo);		
		return jo.getId();
	}
	
	@Override
	public void addJobOffretoEntreprise(int idEnt, int idJo) {
		// TODO Auto-generated method stub
		Entreprise ide = em.find(Entreprise.class, idEnt);
		JobOffer idjo = em.find(JobOffer.class, idJo);
		int oxp = ide.getXp();
		int nxp = oxp+25;
		ide.setXp(nxp);
		idjo.setEntreprise(ide);
	}

	@Override
	public void updateJobOffre(JobOffer jo) {
		// TODO Auto-generated method stub
		JobOffer job = em.find(JobOffer.class, jo.getId());
		job.setCompétencesetConnaissances(jo.getCompétencesetConnaissances());
		job.setDescription(jo.getDescription());
		job.setPoste(jo.getPoste());
		job.setProfilrecherche(jo.getProfilrecherche());
		job.setReference(jo.getReference());
		job.setSujet(jo.getSujet());		
	}

	@Override
	public void deleteJobOffre(int idjo) {
		// TODO Auto-generated method stub
		JobOffer jo = em.find(JobOffer.class, idjo);
		em.remove(jo);
	}

	@Override
	public JobOffer getJobOfferDetails(int idJo) {
		return em.find(JobOffer.class, idJo);
	}
	
	public void addJobOffretoCataloge(int idCat, int idJo) {
		InternshipCataloge ic = em.find(InternshipCataloge.class, idJo);
		JobOffer jo = em.find(JobOffer.class, idJo);
		jo.setInternshipCataloge(ic);
	}
	
	@Override
	public List<JobOffer> getAllJobOfferToday() {
		Date date= new Date();
		SimpleDateFormat formatter=new SimpleDateFormat("YYYY-MM-dd");
		TypedQuery<JobOffer> Q =em.createQuery("Select I from JobOffer I where MONTH(I.datePublier)=MONTH(:date)", JobOffer.class);
		Q.setParameter("date", formatter.format(date));
		List<JobOffer> E = Q.getResultList();
		return E;
	}
	
	@Override
	public List<JobOffer> getAllJobOfferByEntreprise(int idE) {
		TypedQuery<JobOffer> Q =em.createQuery("Select I from JobOffer I where I.entreprise="+idE, JobOffer.class);
		List<JobOffer> E=Q.getResultList();
		return E;
	}
	
	/* InternshipCatalog */
	
	@Override
	public int addInternshipCatalog(InternshipCataloge ic) {
		// TODO Auto-generated method stub
		em.persist(ic);
		return ic.getId();
	}

	@Override
	public void addInternshipCatalogtoEntreprise(int idEnt, int ic) {
		// TODO Auto-generated method stub
		Entreprise es = em.find(Entreprise.class, idEnt);
		InternshipCataloge inc = em.find(InternshipCataloge.class, ic);
		int oxp = es.getXp();
		int nxp = oxp+10;
		es.setXp(nxp);
		inc.setEntreprise(es);
	}

	@Override
	public void updateInternshipCatalog(InternshipCataloge ic) {
		// TODO Auto-generated method stub
		InternshipCataloge nic = em.find(InternshipCataloge.class, ic.getId());
		nic.setCatalogName(ic.getCatalogName());
		nic.setDescription(ic.getDescription());
	}
		
	@Override
	public InternshipCataloge getInternshipCatalaogeDetails(int idCat) {
		return em.find(InternshipCataloge.class, idCat);
	}
	
	public List<InternshipCataloge> getAllInternshipCatalogeByEntreprise(int idE){
		TypedQuery<InternshipCataloge> Q =em.createQuery("Select I from InternshipCataloge I where I.entreprise="+idE, InternshipCataloge.class);
		List<InternshipCataloge> E= Q.getResultList();
		return E;
		
		
	}
	
	/* Student */
	
	@Override
	public void affectStudenttoEntreprise(int idEnt, int ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Entreprise> getallEntreprises() {
		TypedQuery<Entreprise> Q =em.createQuery("Select e from Entreprise e", Entreprise.class)
			;
		List<Entreprise> E=Q.getResultList();
		return E;
	}

	/* Packs */
	
	public String updatepack(int idEnt,int idP) {
		
		Entreprise ent = em.find(Entreprise.class, idEnt);
		Packs pak = em.find(Packs.class, idP);
		if (idP == 2) {
			if (ent.getXp()>= 50) {
				ent.setPacks(pak);
				return "Your pack now is Standard";
			}else {
				return "your Xp doesn't allow you to get this pack";
			}
		}
		if (idP == 3) {
			if (ent.getXp()>= 100) {
				ent.setPacks(pak);
				return "Your pack now is Premier";
				
			}else {
				return "your Xp doesn't allow you to get this pack";
			}
		}
		return null;
		
	}

	public int getpacks(int idEnt) {
		Entreprise ent = em.find(Entreprise.class, idEnt);
		return ent.getPacks().getId();
	}
	
	public Packs getpacksDetails(int idEnt) {
		Entreprise ent = em.find(Entreprise.class, idEnt);
		return ent.getPacks();
	}
	
	public List<Packs>  getallpacks() {
		
		TypedQuery<Packs> Q =em.createQuery("Select e from Packs e", Packs.class)
				;
			List<Packs> E=Q.getResultList();
			return E;	}
}
