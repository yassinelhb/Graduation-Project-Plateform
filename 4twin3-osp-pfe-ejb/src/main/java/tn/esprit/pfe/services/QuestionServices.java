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
import tn.esprit.pfe.interfaces.QuestionServiceRemote;
@Stateless
@LocalBean
public class QuestionServices implements QuestionServiceRemote{
	@PersistenceContext
	EntityManager em;

	
 
	public String addQuestion(ForumQuestion Q, int ide) {
		//em.persist(Q);
		 TypedQuery<String> query = em.createQuery("select m.word from Motindesirable m",String.class);
		 List<String>ls=new ArrayList<>();
		 ls=query.getResultList();
		 Etudiant etudiant = em.find(Etudiant.class, ide);
		 Q.setEtudiant(etudiant);
		String c=Q.getConetnu_Question();
		 String[] str = c.split("\\s+");
		 Boolean b =false;
		 for (String a : str) {
             for (int i=0;i<ls.size();i++){
                 if (ls.get(i).equals(a)){
                 b=true;
                 break;
                 }}}
		 if (b==false){
			 em.persist(Q);
			 return "ajout avec sucées";}
		 else {
			 return"il y a des mots indesirable";}
		
		
	}

	/*@Override
	public void MetreajourQuestion(boolean question_resolu, int id_Question) {
		ForumQuestion q = em.find(ForumQuestion.class, id_Question);
		q.setQuestion_resolu(question_resolu);
		
		
	}*/
	public void MetreajourQuestion(int id_Question) {
		ForumQuestion q = em.find(ForumQuestion.class, id_Question);
		q.setQuestion_resolu(true);
	}

	@Override
	public void deleteQuestion(int id_Question) {
		ForumQuestion q = em.find(ForumQuestion.class, id_Question);
		em.remove(q);
		
	}
	
	@Override
	public ForumQuestion getQuestionid(int id) {
		TypedQuery<ForumQuestion> query =
				em.createQuery("SELECT q FROM ForumQuestion q WHERE q.id_Question=:id_Question ",
						ForumQuestion.class);
		query.setParameter("id_Question", id);
		
		ForumQuestion question = null;
		try { question = query.getSingleResult(); }
		catch (Exception e) { System.out.println("Erreur : " + e); }
		return question;
	}
	
	@Override
	public List<ForumQuestion> getAllquestion() {

		System.out.println("In findAllquestion : ");
		List<ForumQuestion> c=em.createQuery("from ForumQuestion", ForumQuestion.class).getResultList();
		return c;
	}
	
	

	
	

}
