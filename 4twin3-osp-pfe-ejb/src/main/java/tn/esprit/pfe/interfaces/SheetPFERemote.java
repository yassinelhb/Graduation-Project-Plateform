package tn.esprit.pfe.interfaces;

import java.util.List;

import javax.ejb.Remote;

import tn.esprit.pfe.entities.Enseignant;
import tn.esprit.pfe.entities.EtatSheetPFE;
import tn.esprit.pfe.entities.Etudiant;
import tn.esprit.pfe.entities.PFENotification;
import tn.esprit.pfe.entities.RequestCancelInternship;
import tn.esprit.pfe.entities.SheetPFE;
import tn.esprit.pfe.entities.SheetPFEModification;

@Remote
public interface SheetPFERemote {

	public int addSheetPFE(SheetPFE sheetPFE);
	public String exportSheetPFE(int sheet_id);
	public List<Etudiant> getAllStudentNoSheet(int startyear,int toyear);
	public void reminderStudentNoSheet(List<Etudiant> students);
	public List<SheetPFE> getAllSheetPFEFilter(EtatSheetPFE etat, int year, String pays, int id_categorie);
	public List<SheetPFE> getAllSheetPFEDefault();
	public List<SheetPFE> getAllSheetPFEAccepted();
	public SheetPFE getSheetPFEById(int id);
	public SheetPFE getSheetPFEByEtudiant(int user_id);
	public boolean updateSheetPFE(SheetPFE sheetPFE);
	public boolean verificationByDirectorSheetPFE(int sheet_id,EtatSheetPFE etat,int user_id);
	public int requestCancelInternship(int sheet_id,int user_id);
	public List<RequestCancelInternship> getAllRequest();
	public RequestCancelInternship getResquest(int id);
	public boolean updateRequest(int request_id, EtatSheetPFE etat, String note,int user_id);
	public List<SheetPFE> getAllSheetWaitEncadreur();
	public List<SheetPFE> getAllSheetWaitRapporter();
	public List<SheetPFE> getAllSheetValidate();
	public List<Enseignant> getAllValidateur();
	public List<Enseignant> getValidateurByCategories(int sheet_id);
	public boolean affectValidateurToSheetPFE(int sheet_id, int enseignant_id, int user_id);
	public boolean validateSheetPFE(int sheet_id,EtatSheetPFE etat,String note,int user_id);
	public List<Enseignant> getAllEnseignantOrderByEncadrement();
	public List<Enseignant> getEncardeurByCategories(int sheet_id);
	public String affectEncadreurToSheetPFEAuto(int sheet_id,int user_id);
	public boolean affectEncadreurToSheetPFEManual(int sheet_id,int enseignant_id,int user_id);
	public boolean updateEncadreurSheetPFE(int sheetPFE_id,int enseignant_id,int user_id);
	public List<Enseignant> getRapporteurByCategories(int sheet_id);
	public String affectRapporteurToSheetPFEAuto(int sheet_id,int user_id);
	public boolean affectRapporteurToSheetPFEManual(int sheet_id,int enseignant_id,int user_id);
	public boolean updateRapporteurSheetPFE(int sheetPFE_id,int enseignant_id,int user_id);
	public List<SheetPFE> getAllSheetPFEWaitNote();
	public List<SheetPFE> getAllSheetPFEWaitPlaning();
	public boolean addNoteEncadreur(int note,int sheet_id);
	public boolean addNoteRapporteur(int note,int sheet_id);
	public List<SheetPFE> getAllSheetByEnseignant(int startyear,int toyear, String type, int user_id);
	public List<SheetPFE> getAllSheetByEncadreur(int user_id);
	public List<SheetPFE> getAllSheetByRapporteur(int user_id);
	public List<SheetPFE> getAllSheetByValidateur(int user_id);
	public boolean accepteSheetModify(int sheet_id,EtatSheetPFE etat,String note,int user_id);
	public List<SheetPFEModification> getALLSheetModifyDefault();
	public SheetPFEModification getSheetModify(int sheet_id);
	public List<PFENotification> getAllNotificationByEnseignant(int enseignant_id);
	public List<PFENotification> getAllNotificationByEtudiant(int etudiant_id);
	public boolean uploadExcel(String file);
	public List<Object> dashboard();
	public boolean changeVu(int id,String role);

}
