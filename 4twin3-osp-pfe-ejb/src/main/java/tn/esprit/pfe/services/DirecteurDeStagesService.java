package tn.esprit.pfe.services;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import tn.esprit.pfe.entities.Admin;
import tn.esprit.pfe.entities.Enseignant;
import tn.esprit.pfe.entities.Site;
import tn.esprit.pfe.interfaces.DirecteurDeStagesServiceRemote;
import utilities.ValidationError;
@Stateless
@LocalBean
public class DirecteurDeStagesService implements DirecteurDeStagesServiceRemote{
	@PersistenceContext
	EntityManager em;

	@Override
	public Set<ValidationError> affecterDirecteurDeStage(int idEnseignant, int idSite, int idAdmin) {
		Set<ValidationError> errors = new HashSet<>();
		Admin admin = em.find(Admin.class, idAdmin);
		if (admin == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Admin");
			error.setErrorMessage("Cet admin n'éxiste pas");
			error.setPropertyPath("Admin");
			errors.add(error);
			return errors;
		} else if (admin.getEcole()==null) {
			ValidationError error = new ValidationError();
			error.setClassName("Admin");
			error.setErrorMessage("Cet admin n'a pas d'école, vous devez tout d'abord créer une école");
			error.setPropertyPath("Ecole");
			errors.add(error);
			return errors;
		}
		else {
			Site s=em.find(Site.class, idSite);
			Enseignant e=em.find(Enseignant.class, idEnseignant);
			if (s==null) {
				ValidationError error = new ValidationError();
				error.setClassName("Site");
				error.setErrorMessage("Ce Site n'éxiste pas");
				error.setPropertyPath("Site");
				errors.add(error);
				return errors;
			} else if (!admin.getEcole().getSites().contains(s)) {
				ValidationError error = new ValidationError();
				error.setClassName("Site");
				error.setErrorMessage("Ce Site n'appartient pas à votre école");
				error.setPropertyPath("Site");
				errors.add(error);
				return errors;
			} else if (e==null) {
				ValidationError error = new ValidationError();
				error.setClassName("Enseignant");
				error.setErrorMessage("Cet Enseignant n'éxiste pas");
				error.setPropertyPath("Enseignant");
				errors.add(error);
				return errors;
			}else if (e.getSite()!=s) {
				ValidationError error = new ValidationError();
				error.setClassName("Enseignant");
				error.setErrorMessage("Cet Enseignant n'appartient pas à ce site");
				error.setPropertyPath("Site");
				errors.add(error);
				return errors;
			} else {
				try {
					e.setDirecteurDesStages(s);
					s.setDirecteurDesStages(e);
					em.merge(s);
					em.merge(e);
					em.flush();
					return null;
				}catch (ConstraintViolationException ex) {
					Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
					errors.clear();
					errors.addAll(ValidationError.fromViolations(violations));
					return errors;
				}catch (PersistenceException ex) {
					return errors;
				}
			}
			
		}
	}

	

	



}
