package tn.esprit.pfe.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import tn.esprit.pfe.interfaces.SoutenanceServiceRemote;
import tn.esprit.pfe.entities.Etudiant;
import tn.esprit.pfe.entities.Message;
import tn.esprit.pfe.entities.Notifications;
import tn.esprit.pfe.entities.Soutenance;
import tn.esprit.pfe.entities.User;

@Stateless
@LocalBean
public class SoutenanceServices implements SoutenanceServiceRemote {
	@PersistenceContext
	EntityManager em;

	@Override
	public void addSoutenance(Soutenance s) {
		em.persist(s);
		
	}

	@Override
	public void updateSoutenance(Soutenance s) {
		// TODO Auto-generated method stub
		Soutenance st = em.find(Soutenance.class, s.getId());
		st.setTitre(s.getTitre());
		st.setSalle(s.getSalle());
		st.setNoteSoutenance(s.getNoteSoutenance());
		st.setHeureSoutenance(s.getHeureSoutenance());
		st.setDescription(s.getDescription());
		st.setDateSoutenance(s.getDateSoutenance());
	}

	@Override
	public void deleteSoutenance(int id) {
		// TODO Auto-generated method stub
		Soutenance s = em.find(Soutenance.class, id);	
		em.remove(s);
	}

	
	
	
	
	@Override
	public void testNote(int ids , float notee , float note)
	{
		Soutenance s = em.find(Soutenance.class, ids);
		float not = (notee+note)/2;
		
		
		System.out.println(not);
		if ( (notee - note) <= 3)
		{
			
			s.setId(ids);
			s.setNoteSoutenance(not);
			em.persist(s);
		}
		if ((notee-note) > 3 )
		{
			Notifications n = new Notifications();
		String titre = 	s.getTitre();
			String text = "il ya un conflit dans la note de la soutenance :"+titre;
			n.setText(text);	
		Etudiant u =	em.find(Etudiant.class, 1);
		    n.setUser(u);
		    em.persist(n);
		}
		
	}

	@Override
	public List<Soutenance> afficherSoutenanceNonNote() {
		float n = 0;
		Query query = em.createQuery("select   s.Titre 	, s.Description , s.dateSoutenance , s.Salle from Soutenance s where s.NoteSoutenance = :n ORDER BY s.dateSoutenance desc").setParameter("n", n);
		return query.getResultList();
	}
	
	@Override
	public List<Soutenance> afficherSoutenanceSelonEtudiant(String titre) {
		Query query = em.createQuery("select  s.NoteSoutenance from Soutenance s where s.Titre = :titre").setParameter("titre", titre);
		return query.getResultList();
	}


	

	
	@Override
	public List<Float> MoyenneNote() {
		
		List<Float> soutenance = em.createQuery("select  s.NoteSoutenance from Soutenance s where s.NoteSoutenance IS NOT NULL ", Float.class).getResultList();
	
		DoubleSummaryStatistics isn = new DoubleSummaryStatistics();
		isn.accept(soutenance.get(0));
		isn.accept(soutenance.get(1));
		isn.accept(soutenance.get(2));
		isn.accept(soutenance.get(3));
		 System.out.println("Count Note : " + isn.getCount());
	        System.out.println("Sum Note :  " + isn.getSum());
	        System.out.println("Min Note :  " + isn.getMin());
	        System.out.println("Max Note :  " + isn.getMax());
	        System.out.println("Average Note :  " + isn.getAverage());
	       List <Float> a = new ArrayList<Float>();
	       a.add((float) isn.getCount());
	       a.add((float) isn.getSum());
	       a.add((float) isn.getMin());
	       a.add((float) isn.getMax());
	       a.add((float) isn.getAverage());
		return a;
	}
	


}
