package tn.esprit.pfe.interfaces;

import java.util.Set;

import javax.ejb.Remote;

import utilities.ValidationError;

@Remote
public interface DirecteurDeStagesServiceRemote {
	public Set<ValidationError> affecterDirecteurDeStage(int idEnseignant, int idSite, int idAdmin);

}
