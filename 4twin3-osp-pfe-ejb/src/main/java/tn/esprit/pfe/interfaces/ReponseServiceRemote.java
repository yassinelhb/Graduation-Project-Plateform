package tn.esprit.pfe.interfaces;

import java.util.List;

import javax.ejb.Remote;

import tn.esprit.pfe.entities.ForumQuestion;
import tn.esprit.pfe.entities.ForumReponse;

@Remote
public interface ReponseServiceRemote {
	public String addReponse(ForumReponse reponse, int idq,int ide);
	public void MettreajourReponse(int id_Reponse/*,ForumReponse rep*/, String Conetnu_Reponse);
	public int getNombreReponseJPQL(int idq);
	public boolean updaterep(ForumReponse frep,int idq);
	public List<String> notifier(int idq);
	public List<ForumReponse> getAllReponse(int idq);
	

}
