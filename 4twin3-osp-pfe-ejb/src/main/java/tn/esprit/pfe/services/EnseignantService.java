package tn.esprit.pfe.services;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import tn.esprit.pfe.entities.Admin;
import tn.esprit.pfe.entities.Enseignant;
import tn.esprit.pfe.entities.Site;
import tn.esprit.pfe.entities.User;
import tn.esprit.pfe.interfaces.EnseignantServiceRemote;
import utilities.ValidationError;

@Stateless
@LocalBean
public class EnseignantService implements EnseignantServiceRemote {
	@PersistenceContext(unitName = "4twin3-osp-pfe-ejb")
	EntityManager em;

	@EJB
	UserService us;

	@Override
	public Set<ValidationError> addEnseignant(Enseignant e, int idSite, int idAdmin) {
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
		} else if (admin.getEcole() != site.getEcole()) {
			ValidationError error = new ValidationError();
			error.setClassName("Ecole");
			error.setErrorMessage("Ce n'appartient pas à votre école");
			error.setPropertyPath("Admin");
			errors.add(error);
			return errors;
		} else {
			e.setPassword("testtest");
			e.setEcole(admin.getEcole());
			e.setSite(site);
			errors = us.addUser(e);
			if (errors == null) {
				MailSender mailSender = new MailSender();
				try {
					mailSender.sendMessage("smtp.gmail.com", "esprit.service.pfe@gmail.com", "Esprit2019", "587", "true", "true",
							e.getEmail(), "Votre inscription à la plateforme de PFE à "+e.getEcole().getNom(),
									"Vous pouvez à présent vous connecter à la platerforme via ces"
									+ " informations de connexions: <br>"+ "Login: "+e.getEmail()+"<br>Mot de passe: "+e.getPlainPassword());
				} catch (MessagingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			return errors;
		}
	}

	@Override
	public Set<Enseignant> getListEnseignant(int idUser) {
		User user = em.find(User.class, idUser);
		if (user == null) {
			return null;
		} else if (user.getRole().equals("Admin")) {
			Admin admin = (Admin) user;
			if (admin.getEcole() == null) {
				return new HashSet<>();
			}
			return admin.getEcole().getEnseignants();
		} else {
			return null;
		}
	}

	@Override
	public Set<ValidationError> supprimerEnseignant(int idEnseignant, int idAdmin) {
		Set<ValidationError> errors = new HashSet<>();
		Admin admin = em.find(Admin.class, idAdmin);
		Enseignant enseignant = em.find(Enseignant.class, idEnseignant);
		if (admin == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Admin");
			error.setErrorMessage("Cet admin n'éxiste pas");
			error.setPropertyPath("Admin");
			errors.add(error);
			return errors;
		} else if (enseignant == null) {
			ValidationError error = new ValidationError();
			error.setClassName("Enseignant");
			error.setErrorMessage("Cet enseignant n'éxiste pas");
			error.setPropertyPath("Enseignant");
			errors.add(error);
			return errors;
		} else if (enseignant.getEcole() != admin.getEcole()) {
			ValidationError error = new ValidationError();
			error.setClassName("Enseignant");
			error.setErrorMessage("Cet enseignant ne fait pas partie de votre école");
			error.setPropertyPath("Enseignant");
			errors.add(error);
			return errors;
		} else {
			if (enseignant.getDirecteurDesStages()!=null) {
				enseignant.getDirecteurDesStages().setDirecteurDesStages(null);
				em.flush();
			}
			if (enseignant.getChefDeDepartement()!=null) {
				enseignant.getChefDeDepartement().setChefDeDepartement(null);
				em.flush();
			}
			em.createQuery("delete from User u where u.id=:id").setParameter("id", idEnseignant).executeUpdate();
			em.flush();
		}
		return null;
	}

}
