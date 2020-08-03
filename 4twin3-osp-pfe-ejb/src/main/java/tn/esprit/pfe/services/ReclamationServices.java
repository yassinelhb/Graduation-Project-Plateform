package tn.esprit.pfe.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import tn.esprit.pfe.entities.Etudiant;
import tn.esprit.pfe.entities.Notifications;
import tn.esprit.pfe.entities.Reclamation;
import tn.esprit.pfe.entities.User;
import tn.esprit.pfe.interfaces.ReclamationServiceRemote;


@Stateless
@LocalBean
public class ReclamationServices implements ReclamationServiceRemote {

	
	@PersistenceContext
	EntityManager em;
	
	
	public int addReclamation(Reclamation rec) {
	
	
		em.persist(rec);
		
		Notifications n = new Notifications();
		
			String text = "l'etudiant "+rec.getEtudiant().getId()+" a ajotuer une reclamation a propos de la note de soutenance :"+rec.getSoutenance().getId();
			n.setText(text);	
		Etudiant u =	em.find(Etudiant.class, 1);
		    n.setUser(u);
		    em.persist(n);
		return rec.getIdReclamation();

		
	}
	

	@Override
	public void updateReclamation(Reclamation rec) {
		try
		{
			int query = em.createQuery("update Reclamation rec set rec.textRec =  :text where rec.idReclamation = :id").
					setParameter("text",rec.getTextRec()).setParameter("id",rec.getIdReclamation())
					.executeUpdate();
		}
		catch (Exception e )
		{
			System.out.println(e);
		}
	
		
	}


	@Override
	public void deleteReclamation(int idReclamation) {
		
		try {
		Reclamation r = em.find(Reclamation.class, idReclamation);
		System.out.println(r);
		em.remove(r);
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
	}


	@Override
	public List<Reclamation> getAllReclamation() {
		Query query = em.createQuery("select   r.dateAjout ,r.textRec ,r.etudiant.nom , r.etudiant.prenom ,  r.soutenance.Description  from Reclamation r ORDER BY r.dateAjout desc");
		return query.getResultList();
	}
	
	@Override
	public List<Reclamation> getReclamationByEtudiant(String nom , String prenom ) {
		Query query = em.createQuery("select  r.textRec , r.dateAjout, r.soutenance.Titre from Reclamation r where r.etudiant.nom = :nom AND r.etudiant.prenom = :prenom ").setParameter("nom",nom)
				.setParameter("prenom", prenom);
		return query.getResultList();
	}
	
	
	
	@Override
	public List<Reclamation> getReclamationBySoutenance(int id ) {
		Query query = em.createQuery("select  r.textRec , r.dateAjout, r.soutenance.Titre from Reclamation r where r.etudiant.id = :id ").setParameter("id",id);
		return query.getResultList();
	}
	
	
	//stat
	
	@Override
	public List<Float> nombreDeReclamationSelonDateAjout() {
		// TODO Auto-generated method stub
		List<Date> reclamation = em.createQuery("select  dateAjout  from Reclamation group by  dateAjout", Date.class).getResultList();
		Date dateajout = reclamation.get(0);
	
		 System.out.println("before formatting: " + dateajout);
			    
		    SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy");
		    Date date = new Date(System.currentTimeMillis());
		
	
		   float count = 0; 
		
		if (dateajout.before(date) )
		{
		   count = count+1;
		}
		else 
		{
			count =0;
		}
		    
		   List <Float> d = new ArrayList<Float>();
		   d.add(count);
		return d;
	}
	
	@Override
	public List<Object[]> nombreDeReclamationParMois() {
		// TODO Auto-generated method stub
		List<Object[]> reclamation = em.createQuery("select  dateAjout , count(*)  from Reclamation group by Month (dateAjout) ", Object[].class).getResultList();
		return reclamation;
	}
	
   
	


	
	
	

}
