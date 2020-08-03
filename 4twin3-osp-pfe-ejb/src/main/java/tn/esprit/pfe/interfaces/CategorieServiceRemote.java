package tn.esprit.pfe.interfaces;
import java.util.List;

import javax.ejb.Remote;

import tn.esprit.pfe.entities.Categorie;

@Remote
public interface CategorieServiceRemote {
	
	public int addCategorie(Categorie c,int ide);

	public void deleteCategorie(int id);

	public List<Categorie> getAllcategorie();
	
	public void addCategoriecommemodule(String name);

	public List<Categorie> getCategorielesplusdemandées(int idens);

	List<Categorie> getCategoriebyName(String name );

	//public int addCategorie(Categorie c, int ide);
	

}
