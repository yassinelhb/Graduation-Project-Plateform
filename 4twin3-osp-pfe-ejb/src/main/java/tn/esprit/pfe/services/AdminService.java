package tn.esprit.pfe.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import tn.esprit.pfe.entities.Admin;
import tn.esprit.pfe.entities.User;
import tn.esprit.pfe.interfaces.AdminServiceRemote;
@Stateless
@LocalBean
public class AdminService implements AdminServiceRemote{
	@PersistenceContext
	EntityManager em;

	@Override
	public List<Admin> getListAdmin() {
		List<User> liste = em.createQuery("from User",User.class).getResultList();
		List<Admin> listeAdmin = new ArrayList<>();
		for (User u : liste) {
			if (u instanceof Admin) {
				listeAdmin.add((Admin) u);
			}
		}
		return listeAdmin;
	}

	@Override
	public Admin getAdmin(int id) {
		Admin admin=em.find(Admin.class, id);
		return admin;
	}
	

	



}
