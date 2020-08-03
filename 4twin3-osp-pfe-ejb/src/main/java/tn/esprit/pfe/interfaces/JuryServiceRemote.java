package tn.esprit.pfe.interfaces;

import javax.ejb.Remote;

import tn.esprit.pfe.entities.Jury;

@Remote
public interface JuryServiceRemote {
	public int addJury(Jury jury);
	public void updateJury(Jury jury);
	public void deleteJury(int id);
}
