package tn.esprit.pfe.services;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import tn.esprit.pfe.entities.Admin;
import tn.esprit.pfe.entities.Classe;
import tn.esprit.pfe.entities.Departement;
import tn.esprit.pfe.entities.Etudiant;
import tn.esprit.pfe.entities.Site;
import tn.esprit.pfe.entities.Specialite;
import tn.esprit.pfe.interfaces.EtudiantServiceRemote;
import utilities.ValidationError;

@Stateless
@LocalBean
public class EtudiantService implements EtudiantServiceRemote {

	@PersistenceContext
	EntityManager em;
	
	@EJB
	UserService us;

	@Override
	public Set<ValidationError> addEtudiant(Etudiant e, int idClasse, int idAdmin) {
		Set<ValidationError> errors = new HashSet<>();
		Admin admin = em.find(Admin.class, idAdmin);
		if ((long) em.createQuery("select count(e) from Etudiant e where e.identifiant=:identifiant").setParameter("identifiant", e.getIdentifiant()).getSingleResult()>0) {
			ValidationError error=new ValidationError();
			error.setClassName(e.getClass().getSimpleName());
			error.setErrorMessage("Cet Identifiant éxiste déjà");
			error.setPropertyPath("identifiant");
			errors.add(error);
		}
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
			Classe c = em.find(Classe.class, idClasse);
			if (c == null) {
				ValidationError error = new ValidationError();
				error.setClassName("Etudiant");
				error.setErrorMessage("Cette classe n'éxiste pas");
				error.setPropertyPath("Classe");
				errors.add(error);
				return errors;
			} else if (c.getSpecialite().getDepartement().getSite().getEcole().getAdmin() != admin) {
				ValidationError error = new ValidationError();
				error.setClassName("Etudiant");
				error.setErrorMessage("Cette classe n'appartient pas à votre école");
				error.setPropertyPath("Classe");
				errors.add(error);
				return errors;
			} else {
				try {
					e.setClasse(c);
					e.setPlainPassword(e.getIdentifiant());
					return us.addUser(e);
				} catch (ConstraintViolationException ex) {
					Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
					errors.clear();
					errors.addAll(ValidationError.fromViolations(violations));
					return errors;
				} catch (Exception ex) {
					return errors;
				}
			}

		}
	}

	@Override
	public Set<ValidationError> modifierEtudiant(Etudiant e, int idEtudiant, int idAdmin) {
		Set<ValidationError> errors = new HashSet<>();
		Admin admin = em.find(Admin.class, idAdmin);
		Etudiant etudiant = em.find(Etudiant.class, idEtudiant);
		if (admin == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Admin");
			error.setErrorMessage("Cet admin n'éxiste pas");
			error.setPropertyPath("Admin");
			errors.add(error);
			return errors;
		} else if (etudiant == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Etudiant");
			error.setErrorMessage("Cet Etudiant n'éxiste pas");
			error.setPropertyPath("Etudiant");
			errors.add(error);
			return errors;
		} else if (admin.getEcole() == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Admin");
			error.setErrorMessage("Cet Admin n'a pas d'école");
			error.setPropertyPath("Etudiant");
			errors.add(error);
			return errors;
		} else {
			if (etudiant.getClasse().getSpecialite().getDepartement().getSite().getEcole() == admin.getEcole()) {
				e.setId(etudiant.getId());
				if (e.getPlainPassword() != null) {
					e.setPassword(e.getPlainPassword());
				}
				if (e.getNom() != null) {
					etudiant.setNom(e.getNom());
				}
				if (e.getPrenom() != null) {
					etudiant.setPrenom(e.getPrenom());
				}
				try {
					em.merge(etudiant);
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
				error.setClassName("Specialite");
				error.setErrorMessage("Cet admin n'a pas le droit de modifier cette spécialité");
				error.setPropertyPath("Admin");
				errors.add(error);
				return errors;
			}
		}
	}

	@Override
	public Set<ValidationError> supprimerEtudiant(int idEtudiant, int idAdmin) {
		Set<ValidationError> errors = new HashSet<>();
		Admin admin = em.find(Admin.class, idAdmin);
		Etudiant etudiant = em.find(Etudiant.class, idEtudiant);
		if (admin == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Admin");
			error.setErrorMessage("Cet admin n'éxiste pas");
			error.setPropertyPath("Admin");
			errors.add(error);
			return errors;
		} else if (etudiant == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Etudiant");
			error.setErrorMessage("Cet Etudiant n'éxiste pas");
			error.setPropertyPath("Etudiant");
			errors.add(error);
			return errors;
		} else {
			if (etudiant.getClasse().getSpecialite().getDepartement().getSite().getEcole().getAdmin() == admin) {
				try {
					em.createQuery("delete from Etudiant e where e.id=:id").setParameter("id", etudiant.getId())
							.executeUpdate();
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
				error.setClassName("Etudiant");
				error.setErrorMessage("Cet admin n'a pas le droit de supprimer cet Etudiant");
				error.setPropertyPath("Admin");
				errors.add(error);
				return errors;
			}
		}
	}

	@Override
	public Etudiant getEtudiant(int idEtudiant) {
		Etudiant etudiant = em.find(Etudiant.class, idEtudiant);
		if (etudiant == null) {
			return null;
		} else {
			return etudiant;
		}
	}

	@Override
	public Set<Etudiant> getListEtudiantParDepartement(int idDepartement, int idAdmin) {
		Set<Etudiant> etudiants = new HashSet<>();
		Departement departement = em.find(Departement.class, idDepartement);
		Admin admin = em.find(Admin.class, idAdmin);
		if (admin == null) {
			return etudiants;
		} else if (admin.getEcole() == null) {
			return etudiants;
		} else if (departement == null) {
			return etudiants;
		} else if (admin.getEcole() != departement.getSite().getEcole()) {
			return etudiants;
		} else if (departement.getSpecialites().size() < 1) {
			return etudiants;
		} else {
			for (Specialite s : departement.getSpecialites()) {
				for (Classe c : s.getClasses()) {
					etudiants.addAll(c.getEtudiants());
				}
			}
			return etudiants;
		}
	}

	@Override
	public Set<Etudiant> getListEtudiantParSite(int idSite, int idAdmin) {
		Set<Etudiant> etudiants = new HashSet<>();
		Site site = em.find(Site.class, idSite);
		Admin admin = em.find(Admin.class, idAdmin);
		if (admin == null) {
			return etudiants;
		}  else if (site == null) {
			return etudiants;
		}  else if (admin.getEcole() == null) {
			return etudiants;
		} else if (admin.getEcole() != site.getEcole()) {
			return etudiants;
		} else {
			for (Departement d : site.getDepartements()) {
				for (Specialite s : d.getSpecialites()) {
					for (Classe c : s.getClasses()) {
						etudiants.addAll(c.getEtudiants());
					}
				}

			}
			return etudiants;
		}
	}

	@Override
	public Set<Etudiant> getListEtudiantParEcole(int idAdmin) {
		Set<Etudiant> etudiants = new HashSet<>();
		Admin admin = em.find(Admin.class, idAdmin);
		if (admin == null) {
			return etudiants;
		} else if (admin.getEcole() == null) {
			return etudiants;
		} else {
			for (Site site : admin.getEcole().getSites()) {
				for (Departement d : site.getDepartements()) {
					for (Specialite s : d.getSpecialites()) {
						for (Classe c : s.getClasses()) {
							etudiants.addAll(c.getEtudiants());
						}
					}

				}
			}
			return etudiants;
		}
	}

	@Override
	public Set<Etudiant> getListEtudiantParClasse(int idClasse, int idAdmin) {
		Classe classe = em.find(Classe.class, idClasse);
		Admin admin = em.find(Admin.class, idAdmin);
		Set<Etudiant> liste = new HashSet<>();
		if (admin == null) {
			return liste;
		}  else if (classe == null) {
			return liste;
		}  else if (admin.getEcole() == null) {
			return liste;
		} else if (admin.getEcole() != classe.getSpecialite().getDepartement().getSite().getEcole()) {
			return liste;
		}else {
			liste.addAll(classe.getEtudiants());
			return liste;
		}
	}

	@Override
	public Set<Etudiant> getListEtudiantParSpecialite(int idSpecialite, int idAdmin) {
		Set<Etudiant> etudiants = new HashSet<>();
		Specialite specialite = em.find(Specialite.class, idSpecialite);
		Admin admin = em.find(Admin.class, idAdmin);
		if (admin == null) {
			return etudiants;
		}  else if (specialite == null) {
			return etudiants;
		}  else if (admin.getEcole() == null) {
			return etudiants;
		} else if (admin.getEcole() != specialite.getDepartement().getSite().getEcole()) {
			return etudiants;
		} else {
					for (Classe c : specialite.getClasses()) {
						etudiants.addAll(c.getEtudiants());
					}
			return etudiants;
		}
	}

}
