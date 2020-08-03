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
import tn.esprit.pfe.entities.Specialite;
import tn.esprit.pfe.interfaces.SpecialiteServiceRemote;
import utilities.ValidationError;

@Stateless
@LocalBean
public class SpecialiteService implements SpecialiteServiceRemote {

	@PersistenceContext
	EntityManager em;

	@Override
	public Set<ValidationError> addSpecialite(Specialite s, int idDepartement, int idAdmin) {
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
			Departement d=em.find(Departement.class, idDepartement);
			if (d==null) {
				ValidationError error = new ValidationError();
				error.setClassName("Departement");
				error.setErrorMessage("Ce departement n'éxiste pas");
				error.setPropertyPath("Site");
				errors.add(error);
				return errors;
			} else if (d.getSite().getEcole().getAdmin()!=admin) {
				ValidationError error = new ValidationError();
				error.setClassName("Departement");
				error.setErrorMessage("Ce Département n'appartient pas à votre école");
				error.setPropertyPath("Departement");
				errors.add(error);
				return errors;
			} else {
				try {
					s.setDepartement(d);
					em.persist(d);
					em.flush();
					d.getSpecialites().add(s);
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
	public Set<ValidationError> modifierSpecialite(Specialite s, int idSpecialite, int idAdmin) {
		Set<ValidationError> errors = new HashSet<>();
		Admin admin = em.find(Admin.class, idAdmin);
		Specialite specialite = em.find(Specialite.class, idSpecialite);
		if (admin == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Admin");
			error.setErrorMessage("Cet admin n'éxiste pas");
			error.setPropertyPath("Admin");
			errors.add(error);
			return errors;
		}else if (specialite == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Specialité");
			error.setErrorMessage("Cette spécialité  n'éxiste pas");
			error.setPropertyPath("Spécialité");
			errors.add(error);
			return errors;
		} else {
			if (specialite.getDepartement().getSite().getEcole()==admin.getEcole()) {
				specialite.setNom(s.getNom());
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
				error.setClassName("Specialite");
				error.setErrorMessage("Cet admin n'a pas le droit de modifier cette spécialité");
				error.setPropertyPath("Admin");
				errors.add(error);
				return errors;
			}
		}
	}

	@Override
	public Set<ValidationError> supprimerSpecialite(int idSpecialite, int idAdmin) {
		Set<ValidationError> errors = new HashSet<>();
		Admin admin = em.find(Admin.class, idAdmin);
		Specialite specialite = em.find(Specialite.class, idSpecialite);
		if (admin == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Admin");
			error.setErrorMessage("Cet admin n'éxiste pas");
			error.setPropertyPath("Admin");
			errors.add(error);
			return errors;
		}else if (specialite == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Spécialité");
			error.setErrorMessage("Cette spécialité n'éxiste pas");
			error.setPropertyPath("Spécialité");
			errors.add(error);
			return errors;
		} else {
			if (specialite.getDepartement().getSite().getEcole()==admin.getEcole()) {
				try {
					em.createQuery("delete from Specialite s where s.id=:id").setParameter("id", specialite.getId()).executeUpdate();
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
	public Specialite getSpecialite(int idSpecialite) {
		Specialite specialite = em.find(Specialite.class, idSpecialite);
		if (specialite == null) {
			return null;
		} else {
			return specialite;
		}
	}

	@Override
	public Set<Specialite> getListSpecialite(int idDepartement) {
		Departement departement = em.find(Departement.class, idDepartement);
		if (departement==null) return new HashSet<Specialite>();
		return departement.getSpecialites();
	}

}
