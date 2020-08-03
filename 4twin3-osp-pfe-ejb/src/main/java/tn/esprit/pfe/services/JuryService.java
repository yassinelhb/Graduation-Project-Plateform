package tn.esprit.pfe.services;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import tn.esprit.pfe.entities.Entreprise;
import tn.esprit.pfe.entities.Jury;
import tn.esprit.pfe.interfaces.JuryServiceRemote;


@Stateless
@LocalBean
public class JuryService  implements JuryServiceRemote {
	EntityManager em;

	@Override
	public int addJury(Jury jury) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateJury(Jury jury) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteJury(int id) {
		// TODO Auto-generated method stub
		
		Jury jury = em.find(Jury.class, id);	
		em.remove(jury);
		
	}
	
}
