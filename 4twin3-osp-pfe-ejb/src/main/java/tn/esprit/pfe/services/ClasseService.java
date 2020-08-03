package tn.esprit.pfe.services;

import java.util.Calendar;
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

import tn.esprit.pfe.entities.Admin;
import tn.esprit.pfe.entities.Classe;
import tn.esprit.pfe.entities.Specialite;
import tn.esprit.pfe.interfaces.ClasseServiceRemote;
import utilities.ValidationError;

@Stateless
@LocalBean
public class ClasseService implements ClasseServiceRemote {

	@PersistenceContext
	EntityManager em;

	@Override
	public Set<ValidationError> addClasse(int idSpecialite, int idAdmin) {
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
			Specialite s=em.find(Specialite.class, idSpecialite);
			if (s==null) {
				ValidationError error = new ValidationError();
				error.setClassName("Classe");
				error.setErrorMessage("Cette spécialité n'éxiste pas");
				error.setPropertyPath("Specialite");
				errors.add(error);
				return errors;
			} else if (s.getDepartement().getSite().getEcole().getAdmin()!=admin) {
				ValidationError error = new ValidationError();
				error.setClassName("Classe");
				error.setErrorMessage("Cette spécialité n'appartient pas à votre école");
				error.setPropertyPath("Spécialité");
				errors.add(error);
				return errors;
			} else {
				Classe c = new Classe();
				int numero=0;
				for (Classe cc : s.getClasses()) {
					if (cc.getAnneeDeDebut()==Calendar.getInstance().get(Calendar.YEAR)) {
						numero++;
					}
				}
				c.setNumero(numero+1);
				c.setAnneeDeDebut(Calendar.getInstance().get(Calendar.YEAR));
				try {
					c.setSpecialite(s);
					em.persist(c);
					em.flush();
					s.getClasses().add(c);
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
	public Set<ValidationError> modifierClasse(Classe c, int idClasse, int idAdmin) {
		Set<ValidationError> errors = new HashSet<>();
		Admin admin = em.find(Admin.class, idAdmin);
		Classe classe = em.find(Classe.class, idClasse);
		if (admin == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Admin");
			error.setErrorMessage("Cet admin n'éxiste pas");
			error.setPropertyPath("Admin");
			errors.add(error);
			return errors;
		}else if (classe == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Classe");
			error.setErrorMessage("Cette Classe n'éxiste pas");
			error.setPropertyPath("Classe");
			errors.add(error);
			return errors;
		} else {
			if (classe.getSpecialite().getDepartement().getSite().getEcole()==admin.getEcole()) {
				classe.setAnneeDeDebut(c.getAnneeDeDebut());
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
	public Set<ValidationError> supprimerClasse(int idSpecialite, int idAdmin) {
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
			error.setClassName("Specialite");
			error.setErrorMessage("Cette Specialite n'éxiste pas");
			error.setPropertyPath("Specialite");
			errors.add(error);
			return errors;
		} else {
			if (specialite.getDepartement().getSite().getEcole()!=admin.getEcole()) {
				ValidationError error = new ValidationError();
				error.setClassName("Departement");
				error.setErrorMessage("Cet admin n'a pas le droit de supprimer ce département");
				error.setPropertyPath("Admin");
				errors.add(error);
				return errors;
			}
			else if (this.getListClasseAnnee(idSpecialite, Calendar.getInstance().get(Calendar.YEAR)).size()==0) {
				ValidationError error = new ValidationError();
				error.setClassName("Specialite");
				error.setErrorMessage("Cette spécialité n'a pas de classe");
				error.setPropertyPath("Specialite");
				errors.add(error);
				return errors;
			}
			else {
				try {
					int max = (int) em.createQuery("select max(c.id) from Classe c where c.specialite=:specialite AND anneeDeDebut=:annee").setParameter("specialite", specialite).setParameter("annee", Calendar.getInstance().get(Calendar.YEAR)).getSingleResult();
					em.createQuery("delete from Classe c where c.id=:max").setParameter("max", max).executeUpdate();
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

	@Override
	public Classe getClasse(int idClasse) {
		Classe classe = em.find(Classe.class, idClasse);
		if (classe == null) {
			return null;
		} else {
			return classe;
		}
	}

	@Override
	public Set<Classe> getListClasse(int idSpecialite) {
		Specialite specialite = em.find(Specialite.class, idSpecialite);
		if (specialite==null) return new HashSet<Classe>();
		return specialite.getClasses();
	}

	@Override
	public Set<Classe> getListClasseAnnee(int idSpecialite,int annee) {
		Specialite specialite = em.find(Specialite.class, idSpecialite);
		List<?> liste = em.createQuery("select c from Classe c where c.specialite=:specialite AND c.anneeDeDebut=:annee")
		.setParameter("specialite", specialite)
		.setParameter("annee", annee).getResultList();
		Set<Classe> classes = new HashSet<>();
		for (Object o : liste) {
			classes.add((Classe) o);
		}
		return classes;
	}

}
