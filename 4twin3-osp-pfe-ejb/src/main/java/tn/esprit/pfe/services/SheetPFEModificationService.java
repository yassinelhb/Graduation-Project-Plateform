package tn.esprit.pfe.services;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import tn.esprit.pfe.entities.EtatSheetPFE;
import tn.esprit.pfe.entities.InternshipAgreemen;
import tn.esprit.pfe.entities.SheetPFE;
import tn.esprit.pfe.entities.SheetPFEModification;
import tn.esprit.pfe.interfaces.SheetPFEModificationRemote;

@Stateless
@LocalBean
public class SheetPFEModificationService implements SheetPFEModificationRemote{

	@PersistenceContext
    EntityManager em;
	
	@Override
	public boolean removeSheetPFEModification(int id) {
		
		SheetPFEModification sheetPFEModification = em.find(SheetPFEModification.class, id);
   		
   		try {
   			em.remove(sheetPFEModification);
   			return true;
   		} catch (Exception e) {
   			return false;
   		}
	}

	@Override
	public List<SheetPFEModification> getAllRefuseSheetPFEModifications() {
		return em.createQuery("select sm from SheetPFEModification sm join sm.sheetPFE s where sm.etat='DEFAULT' and s.etat='REFUSE' ", SheetPFEModification.class).getResultList();
	}

	
	@Override
	public SheetPFEModification getSheetPFEModification(int id) {
		return em.find(SheetPFEModification.class, id);
	}

	@Override
	public boolean validateSheetPFEModification(SheetPFEModification sheetPFEModification) {
		

		try {
			
			if(sheetPFEModification.getEtat().equals(EtatSheetPFE.ACCEPTED)) {
				String problematic = sheetPFEModification.getProblematic();
				String feature = sheetPFEModification.getFeatures();
				
				SheetPFE sheetPFE = em.find(SheetPFE.class, sheetPFEModification.getSheetPFE().getId());
				sheetPFEModification.setProblematic(sheetPFE.getProblematic());
				sheetPFEModification.setFeatures(sheetPFE.getFeatures());
				
				
				sheetPFE.setProblematic(problematic);
				sheetPFE.setFeatures(feature);
				
				
				em.merge(sheetPFE);
				
			}
				em.merge(sheetPFEModification);
			
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	

}
