package tn.esprit.pfe.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import tn.esprit.pfe.entities.Etudiant;
import tn.esprit.pfe.entities.ForumQuestion;
import tn.esprit.pfe.entities.ForumReponse;
import tn.esprit.pfe.interfaces.ReponseServiceRemote;

@Stateless
@LocalBean
public class ReponseServices implements ReponseServiceRemote {
	@PersistenceContext
	EntityManager em;
	List<ForumReponse> r=new ArrayList<ForumReponse>();
	
	
	@Override
	public String addReponse(ForumReponse reponse,int idq,int ide) {
		reponse.setForumquestion(em.find(ForumQuestion.class, idq));
		 TypedQuery<String> query = em.createQuery("select m.word from Motindesirable m",String.class);
		 List<String>ls=new ArrayList<>();
		 Etudiant etudiant = em.find(Etudiant.class, ide);
		 reponse.setEtudiant(etudiant);
		 ls=query.getResultList();
		 String c=reponse.getConetnu_Reponse();
		 String[] str = c.split("\\s+");
		 Boolean b =false;
		 for (String a : str) {
             for (int i=0;i<ls.size();i++){
                 if (ls.get(i).equals(a)){
                 b=true;
                 break;
                 }}}
		 if (b==false){
			 em.persist(reponse);
			 return "ajout avec sucées";}
		 else {
			 return"il y a des mots indesirable";}
		
		
	}

	

	@Override
	public void MettreajourReponse(int id_Reponse,String Conetnu_Reponse) {
		ForumReponse reponse = em.find(ForumReponse.class, id_Reponse);
		reponse.setConetnu_Reponse(Conetnu_Reponse);
		
	}
	@Override
	public boolean updaterep(ForumReponse frep, int idq) {
		ForumReponse reponse = em.find(ForumReponse.class,idq);
		System.out.println("id-reponse:"+reponse.getId_Reponse());
		reponse.setConetnu_Reponse(frep.getConetnu_Reponse());
		return true;
		
	}
	/*
	public void MettreajourReponse(int id_Reponse,ForumReponse rep) {
		ForumReponse reponse = em.find(ForumReponse.class, id_Reponse);
		
		//reponse.setConetnu_Reponse();
		int i=0;
		for (ForumReponse re :r) {
			if(re.getId_Reponse()==id_Reponse)
			{i=r.indexOf(re);
			r.set(i, rep);
		}}
		
	}*/
	@Override
	public int getNombreReponseJPQL(int idq) {
		
		List<ForumReponse> c=em.find(ForumQuestion.class,idq).getForumReponse();
		return c.size();

	}
	public List<ForumReponse> getAllReponse() {

		System.out.println("In findAllquestion : ");
		List<ForumReponse> c=em.createQuery("from ForumReponse", ForumReponse.class).getResultList();
		return c;
	}
	@Override
	public List<String> notifier(int idq){
		List<ForumReponse> fr=this.getAllReponse(idq);
		List<String> forum=new ArrayList<>();
		for(int i=0;i<fr.size();i++) {
						String rep=fr.get(i).getConetnu_Reponse();
			String rep1=fr.get(i).getForumquestion().getConetnu_Question();
			String rep2=fr.get(i).getForumquestion().getEtudiant().getNom();
			String rep3="votre question est commenté par monsieur"+rep2+" ";
		forum.add(rep3);
		}
		
		return forum;
	}
	
	public List<ForumReponse> getAllReponse(int idq) {
		System.out.println("In findAllReponse : ");
		List<ForumReponse> c=em.find(ForumQuestion.class,idq).getForumReponse();
		return c;
		
	}


	

}
