package tn.esprit.pfe.services;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.HibernateException;

import tn.esprit.pfe.entities.Admin;
import tn.esprit.pfe.entities.Ecole;
import tn.esprit.pfe.interfaces.EcoleServiceRemote;
import utilities.ValidationError;

@Stateless
@LocalBean
public class EcoleService implements EcoleServiceRemote {

	@PersistenceContext
	EntityManager em;

	@Override
	public Set<ValidationError> addEcole(Ecole e, int idAdmin) {
		Set<ValidationError> errors = new HashSet<>();
		Admin admin = em.find(Admin.class, idAdmin);

		if (admin == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Admin");
			error.setErrorMessage("Cet admin n'éxiste pas");
			error.setPropertyPath("Admin");
			errors.add(error);
			return errors;
		} else if (admin.getEcole()!=null) {
			ValidationError error = new ValidationError();
			error.setClassName("Admin");
			error.setErrorMessage("Cet admin a déjà une école, modifiez l'école qui existe déjà ou supprimez la");
			error.setPropertyPath("Ecole");
			errors.add(error);
			return errors;
		}
		else {
			try {
				e.setAdmin(admin);
				em.persist(e);
				em.flush();
				admin.setEcole(e);
				return null;
			}catch (ConstraintViolationException ex) {
				Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
				errors.clear();
				errors.addAll(ValidationError.fromViolations(violations));
				return errors;
			}catch (HibernateException ex) {
				ValidationError error = new ValidationError();
				error.setClassName("Ecole");
				error.setErrorMessage(ex.getLocalizedMessage());
				error.setPropertyPath("Ecole");
				errors.add(error);
				return errors;
			}catch (PersistenceException ex) {
				org.hibernate.exception.ConstraintViolationException ee = (org.hibernate.exception.ConstraintViolationException) ex.getCause();
				
				ValidationError error = new ValidationError();
				error.setClassName("Ecole");
				error.setErrorMessage(ee.getSQLException().getLocalizedMessage());
				error.setPropertyPath("Ecole");
				errors.add(error);
				return errors;
			}
		}
	}

	@Override
	public Set<ValidationError> modifierEcole(Ecole ecole, int idAdmin, int idEcole) {
		Set<ValidationError> errors = new HashSet<>();
		Admin admin = em.find(Admin.class, idAdmin);
		Ecole e = em.find(Ecole.class, idEcole);
		if (admin == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Admin");
			error.setErrorMessage("Cet admin n'éxiste pas");
			error.setPropertyPath("Admin");
			errors.add(error);
			return errors;
		}else if (e == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Ecole");
			error.setErrorMessage("Cette école n'éxiste pas");
			error.setPropertyPath("Ecole");
			errors.add(error);
			return errors;
		} else {
			if (e.getAdmin()==admin) {
				e.setAdresse(ecole.getAdresse());
				e.setNom(ecole.getNom());
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
				error.setClassName("Ecole");
				error.setErrorMessage("Cet admin n'a pas le droit de modifier cette école");
				error.setPropertyPath("Admin");
				errors.add(error);
				return errors;
			}
		}
	}

	@Override
	public Set<ValidationError> supprimerEcole(int idEcole, int idAdmin) {
		Set<ValidationError> errors = new HashSet<>();
		Admin admin = em.find(Admin.class, idAdmin);
		Ecole e = em.find(Ecole.class, idEcole);
		if (admin == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Admin");
			error.setErrorMessage("Cet admin n'éxiste pas");
			error.setPropertyPath("Admin");
			errors.add(error);
			return errors;
		} else if (e == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Ecole");
			error.setErrorMessage("Cette école n'éxiste pas");
			error.setPropertyPath("Ecole");
			errors.add(error);
			return errors;
		} else {
			if (e.getAdmin()==admin) {
				admin.setEcole(null);
				em.remove(e);
			}
			else {
				ValidationError error = new ValidationError();
				error.setClassName("Ecole");
				error.setErrorMessage("Cet admin n'a pas le droit de supprimer cette école");
				error.setPropertyPath("Admin");
				errors.add(error);
				return errors;
			}
		}
		return null;
	}

	@Override
	public Ecole getEcole(int idEcole, int idAdmin) {
		Admin admin = em.find(Admin.class, idAdmin);
		if (admin == null) {
			return null;
		} else if (admin.getEcole().getId()==idEcole) {
			return admin.getEcole();
		} else {
			return null;
		}
	}

	@Override
	public Ecole getEcoleAdmin(int idAdmin) {
		Admin admin = em.find(Admin.class, idAdmin);
		if (admin == null) {
			return null;
		} else if (admin.getEcole() != null) {
			return admin.getEcole();
		} else {
			return null;
		}
	}

	@Override
	public List<Ecole> getListEcole() {
		return em.createQuery("from Ecole",Ecole.class).getResultList();
	}

	@Override
	public Set<ValidationError> addImage(String image, int idAdmin) {
		Set<ValidationError> errors = new HashSet<>();
		Admin admin = em.find(Admin.class, idAdmin);
		if (admin == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Admin");
			error.setErrorMessage("Cet admin n'éxiste pas");
			error.setPropertyPath("Admin");
			errors.add(error);
			return errors;
		} else {
			Ecole e=admin.getEcole();
			if (e.getLogo()!=null) {
				File file = new File(e.getLogo()); 
				file.delete();
			}
			e.setLogo(image);
			try {
				em.merge(e);
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
