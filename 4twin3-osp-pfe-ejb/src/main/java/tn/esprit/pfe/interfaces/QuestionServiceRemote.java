package tn.esprit.pfe.interfaces;

import java.util.List;

import javax.ejb.Remote;

import tn.esprit.pfe.entities.Etudiant;
import tn.esprit.pfe.entities.ForumQuestion;
@Remote
public interface QuestionServiceRemote {
	public String addQuestion(ForumQuestion Q,int ide);
	public void MetreajourQuestion(int id_Question);
	public void deleteQuestion(int id_Question);
	public ForumQuestion getQuestionid(int id);
	public List<ForumQuestion> getAllquestion();
	}
