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
import tn.esprit.pfe.entities.Ecole;
import tn.esprit.pfe.entities.Enseignant;
import tn.esprit.pfe.entities.Site;
import tn.esprit.pfe.interfaces.SiteServiceRemote;
import utilities.ValidationError;

@Stateless
@LocalBean
public class SiteService implements SiteServiceRemote {

	@PersistenceContext
	EntityManager em;

	@Override
	public Set<ValidationError> addSite(Site s, int idAdmin) {
		Set<ValidationError> errors = new HashSet<>();
		Admin admin = em.find(Admin.class, idAdmin);
		if (admin == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Admin");
			error.setErrorMessage("Cet admin n'éxiste pas");
			error.setPropertyPath("Admin");
			errors.add(error);
			return errors;
		} else if (admin.getEcole() == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Admin");
			error.setErrorMessage("Cet admin n'a pas d'école, vous devez tout d'abord créer une école");
			error.setPropertyPath("Ecole");
			errors.add(error);
			return errors;
		} else {
			try {
				s.setEcole(admin.getEcole());
				em.persist(s);
				em.flush();
				admin.getEcole().getSites().add(s);
				return null;
			} catch (ConstraintViolationException ex) {
				Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
				errors.clear();
				errors.addAll(ValidationError.fromViolations(violations));
				return errors;
			} catch (PersistenceException ex) {
				return errors;
			}
		}
	}

	@Override
	public Set<ValidationError> modifierSite(Site s, int idAdmin, int idSite) {
		Set<ValidationError> errors = new HashSet<>();
		Admin admin = em.find(Admin.class, idAdmin);
		Site site = em.find(Site.class, idSite);
		if (admin == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Admin");
			error.setErrorMessage("Cet admin n'éxiste pas");
			error.setPropertyPath("Admin");
			errors.add(error);
			return errors;
		} else if (site == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Site");
			error.setErrorMessage("Ce site  n'éxiste pas");
			error.setPropertyPath("Site");
			errors.add(error);
			return errors;
		} else {
			if (site.getEcole().getAdmin() == admin) {
				site.setAdresse(s.getAdresse());
				site.setNom(s.getNom());
				try {
					em.flush();
					return null;
				} catch (ConstraintViolationException ex) {
					Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
					errors.clear();
					errors.addAll(ValidationError.fromViolations(violations));
					return errors;
				} catch (PersistenceException ex) {
					return errors;
				}
			} else {
				ValidationError error = new ValidationError();
				error.setClassName("Site");
				error.setErrorMessage("Cet admin n'a pas le droit de modifier ce site");
				error.setPropertyPath("Admin");
				errors.add(error);
				return errors;
			}
		}
	}

	@Override
	public Set<ValidationError> supprimerSite(int idSite, int idAdmin) {
		Set<ValidationError> errors = new HashSet<>();
		Admin admin = em.find(Admin.class, idAdmin);
		Site site = em.find(Site.class, idSite);
		if (admin == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Admin");
			error.setErrorMessage("Cet admin n'éxiste pas");
			error.setPropertyPath("Admin");
			errors.add(error);
			return errors;
		} else if (site == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Site");
			error.setErrorMessage("Ce site n'éxiste pas");
			error.setPropertyPath("Site");
			errors.add(error);
			return errors;
		} else {
			if (site.getEcole().getAdmin() == admin) {
				admin.getEcole().getSites().remove(site);
				em.remove(site);
			} else {
				ValidationError error = new ValidationError();
				error.setClassName("Site");
				error.setErrorMessage("Cet admin n'a pas le droit de supprimer ce site");
				error.setPropertyPath("Admin");
				errors.add(error);
				return errors;
			}
		}
		return null;
	}

	@Override
	public Site getSite(int idSite) {
		Site site = em.find(Site.class, idSite);
		if (site == null) {
			return null;
		} else {
			return site;
		}
	}

	@Override
	public Set<Site> getListSite(int idEcole) {
		Ecole ecole = em.find(Ecole.class, idEcole);
		if (ecole == null) {
			return null;
		}
		return ecole.getSites();
	}

	@Override
	public Set<ValidationError> modifierSiteDirecteurDesStages(Site s, int idDirecteur) {
		Set<ValidationError> errors = new HashSet<>();
		Enseignant enseignant = em.find(Enseignant.class, idDirecteur);
		if (enseignant == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Enseignant");
			error.setErrorMessage("Cet enseignant n'éxiste pas");
			error.setPropertyPath("Enseignant");
			errors.add(error);
			return errors;
		} else if (enseignant.getDirecteurDesStages() == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Directeur des stages");
			error.setErrorMessage("Cet enseignant n'est pas un directeur des stages");
			error.setPropertyPath("Directeur des stages");
			errors.add(error);
			return errors;
		} else {
			if (s.getMaxEncadrant() != 0) enseignant.getDirecteurDesStages().setMaxEncadrant(s.getMaxEncadrant());
			if (s.getMaxPresident() != 0) enseignant.getDirecteurDesStages().setMaxPresident(s.getMaxPresident());
			if (s.getMaxPreValidateur() != 0) enseignant.getDirecteurDesStages().setMaxPreValidateur(s.getMaxPreValidateur());
			if (s.getMaxRapporteur() != 0) enseignant.getDirecteurDesStages().setMaxRapporteur(s.getMaxRapporteur());
			try {
				em.flush();
				return null;
			} catch (ConstraintViolationException ex) {
				Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
				errors.clear();
				errors.addAll(ValidationError.fromViolations(violations));
				return errors;
			} catch (PersistenceException ex) {
				return errors;
			}
		}
	}

}
