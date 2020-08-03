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
import tn.esprit.pfe.entities.Departement;
import tn.esprit.pfe.entities.Site;
import tn.esprit.pfe.interfaces.DepartementServiceRemote;
import utilities.ValidationError;

@Stateless
@LocalBean
public class DepartementService implements DepartementServiceRemote {

	@PersistenceContext
	EntityManager em;

	@Override
	public Set<ValidationError> addDepartement(Departement d, int idSite, int idAdmin) {
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
			} else {
				try {
					d.setSite(s);
					em.persist(d);
					em.flush();
					s.getDepartements().add(d);
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

	@Override
	public Set<ValidationError> modifierDepartement(Departement d, int idDepartement, int idAdmin) {
		Set<ValidationError> errors = new HashSet<>();
		Admin admin = em.find(Admin.class, idAdmin);
		Departement departement = em.find(Departement.class, idDepartement);
		if (admin == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Admin");
			error.setErrorMessage("Cet admin n'éxiste pas");
			error.setPropertyPath("Admin");
			errors.add(error);
			return errors;
		}else if (departement == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Departement");
			error.setErrorMessage("Ce département  n'éxiste pas");
			error.setPropertyPath("Site");
			errors.add(error);
			return errors;
		} else {
			if (departement.getSite().getEcole()==admin.getEcole()) {
				departement.setNom(d.getNom());
				try {
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
			else {
				ValidationError error = new ValidationError();
				error.setClassName("Departement");
				error.setErrorMessage("Cet admin n'a pas le droit de modifier ce département");
				error.setPropertyPath("Admin");
				errors.add(error);
				return errors;
			}
		}
	}

	@Override
	public Set<ValidationError> supprimerDepartement(int idDepartement, int idAdmin) {
		Set<ValidationError> errors = new HashSet<>();
		Admin admin = em.find(Admin.class, idAdmin);
		Departement departement = em.find(Departement.class, idDepartement);
		if (admin == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Admin");
			error.setErrorMessage("Cet admin n'éxiste pas");
			error.setPropertyPath("Admin");
			errors.add(error);
			return errors;
		}else if (departement == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Site");
			error.setErrorMessage("Ce site  n'éxiste pas");
			error.setPropertyPath("Site");
			errors.add(error);
			return errors;
		} else {
			if (departement.getSite().getEcole()==admin.getEcole()) {
				try {
					em.createQuery("delete from Departement d where d.id=:id").setParameter("id", departement.getId()).executeUpdate();
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
			else {
				ValidationError error = new ValidationError();
				error.setClassName("Departement");
				error.setErrorMessage("Cet admin n'a pas le droit de supprimer ce département");
				error.setPropertyPath("Admin");
				errors.add(error);
				return errors;
			}
		}
	}

	@Override
	public Departement getDepartement(int idDepartement) {
		Departement departement = em.find(Departement.class, idDepartement);
		if (departement == null) {
			return null;
		} else {
			return departement;
		}
	}

	@Override
	public Set<Departement> getListDepartement(int idSite) {
		Site site = em.find(Site.class, idSite);
		if (site==null) return new HashSet<Departement>();
		return site.getDepartements();
	}

}
