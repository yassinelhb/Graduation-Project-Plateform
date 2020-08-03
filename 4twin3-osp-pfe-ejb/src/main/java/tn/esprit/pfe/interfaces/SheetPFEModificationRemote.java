package tn.esprit.pfe.interfaces;

import java.util.List;

import javax.ejb.Remote;

import tn.esprit.pfe.entities.EtatSheetPFE;
import tn.esprit.pfe.entities.SheetPFEModification;

@Remote
public interface SheetPFEModificationRemote {

	public boolean removeSheetPFEModification(int id);
	public List<SheetPFEModification> getAllRefuseSheetPFEModifications();
	public SheetPFEModification getSheetPFEModification(int id);
	public boolean validateSheetPFEModification(SheetPFEModification sheetPFEModification);
	
}
