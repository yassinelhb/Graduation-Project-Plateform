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

import tn.esprit.pfe.entities.Departement;
import tn.esprit.pfe.entities.Enseignant;
import tn.esprit.pfe.interfaces.ChefDeDepartementServiceRemote;
import utilities.ValidationError;
@Stateless
@LocalBean
public class ChefDeDepartementService implements ChefDeDepartementServiceRemote{
	@PersistenceContext
	EntityManager em;

	@Override
	public Set<ValidationError> affecterChefDeDepartement(int idEnseignant, int idDepartement, int idAdmin) {
		Set<ValidationError> errors = new HashSet<>();
		Enseignant enseignant = em.find(Enseignant.class, idAdmin);
		if (enseignant == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Enseignant");
			error.setErrorMessage("Cet Enseignant n'éxiste pas");
			error.setPropertyPath("Enseignant");
			errors.add(error);
			return errors;
		} else if (enseignant.getEcole()==null) {
			ValidationError error = new ValidationError();
			error.setClassName("Enseignant");
			error.setErrorMessage("Cet admin n'a pas d'école, vous devez tout d'abord créer une école");
			error.setPropertyPath("Ecole");
			errors.add(error);
			return errors;
		}
		else {
			Departement d=em.find(Departement.class, idDepartement);
			Enseignant e=em.find(Enseignant.class, idEnseignant);
			if (d==null) {
				ValidationError error = new ValidationError();
				error.setClassName("Département");
				error.setErrorMessage("Ce Département n'éxiste pas");
				error.setPropertyPath("Département");
				errors.add(error);
				return errors;
			} else if (!enseignant.getSite().getDepartements().contains(d)) {
				ValidationError error = new ValidationError();
				error.setClassName("Departement");
				error.setErrorMessage("Ce Departement n'appartient pas à votre site");
				error.setPropertyPath("Departement");
				errors.add(error);
				return errors;
			} else if (e==null) {
				ValidationError error = new ValidationError();
				error.setClassName("Enseignant");
				error.setErrorMessage("Cet Enseignant n'éxiste pas");
				error.setPropertyPath("Enseignant");
				errors.add(error);
				return errors;
			}else if (e.getSite()!=enseignant.getSite()) {
				ValidationError error = new ValidationError();
				error.setClassName("Enseignant");
				error.setErrorMessage("Cet Enseignant n'appartient pas à ce site");
				error.setPropertyPath("Site");
				errors.add(error);
				return errors;
			} else {
				try {
					d.setChefDeDepartement(e);
					e.setChefDeDepartement(d);
					em.merge(d);
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
