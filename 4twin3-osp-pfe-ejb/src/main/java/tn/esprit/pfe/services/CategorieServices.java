package tn.esprit.pfe.services;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import tn.esprit.pfe.entities.Categorie;
import tn.esprit.pfe.entities.Enseignant;
import tn.esprit.pfe.entities.Etudiant;
import tn.esprit.pfe.interfaces.CategorieServiceRemote;

@Stateless
@LocalBean
public class CategorieServices implements CategorieServiceRemote{
	@PersistenceContext
	EntityManager em;

	@Override
	public int addCategorie(Categorie c,int ide) {
		 Enseignant enseignant = em.find(Enseignant.class, ide);
		 c.setEnseignant(enseignant);
		em.persist(c);
		return c.getId();

	}

	@Override
	public void deleteCategorie(int idc) {
		Query q=em.createNativeQuery("DELETE FROM Categorie WHERE id=" + idc);
		q.executeUpdate();

	}

	@Override
	public List<Categorie> getAllcategorie() {

		System.out.println("In findAllCategorie : ");
		List<Categorie> c=em.createQuery("from Categorie", Categorie.class).getResultList();
		
		return c;
	}

	@Override
	public List<Categorie> getCategoriebyName(String name) {
		Query q = em.createQuery("select c from Categorie c  where c.name=:name ", Categorie.class);
		q.setParameter("name", name);
		return q.getResultList();
		
	}

	@Override
	public void addCategoriecommemodule(String name) {
		int var=1;
		TypedQuery<Categorie> query  =em.createQuery("select c from Categorie c where c.name=name",Categorie.class);
		List<Categorie>ls=new ArrayList<>();
		ls=query.getResultList();
		if (ls.size() >var) {
			List<Categorie> cats = em.createQuery("select c from Categorie c ", Categorie.class).getResultList();
			for(Categorie categorie : cats){
				categorie.setExixtecommemodule(true);
			}
		}	else {System.out.println("la categorie n'est plus utliseé pour etre module");		}
	}

	@Override
	public List<Categorie> getCategorielesplusdemandées(int idens) {
		TypedQuery<Categorie> q = em.createQuery("select c from Categorie c  where c.enseignant.id=:idens ", Categorie.class);
		q.setParameter("idens", idens);
		
		return q.getResultList();
	
	}

	


}
