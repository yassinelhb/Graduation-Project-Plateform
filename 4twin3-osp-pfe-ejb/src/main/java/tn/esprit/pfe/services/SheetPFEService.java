package tn.esprit.pfe.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.zxing.WriterException;

import org.apache.poi.ss.usermodel.Cell;
import net.sf.jasperreports.engine.JRException;
import tn.esprit.pfe.email.Email;
import tn.esprit.pfe.entities.Categorie;
import tn.esprit.pfe.entities.Enseignant;
import tn.esprit.pfe.entities.EnseignantSheetPFE;
import tn.esprit.pfe.entities.Entreprise;
import tn.esprit.pfe.entities.EtatSheetPFE;
import tn.esprit.pfe.entities.Etudiant;
import tn.esprit.pfe.entities.PFENotification;
import tn.esprit.pfe.entities.RequestCancelInternship;
import tn.esprit.pfe.entities.SheetPFE;
import tn.esprit.pfe.entities.SheetPFEModification;
import tn.esprit.pfe.entities.TypeNotification;
import tn.esprit.pfe.interfaces.SheetPFERemote;
import tn.esprit.pfe.pdf.PDF;
import tn.esprit.pfe.qrcode.QRCode;

@Stateless
@LocalBean
public class SheetPFEService implements SheetPFERemote {

	@PersistenceContext
	EntityManager em;

	//test
	@Override
	public int addSheetPFE(SheetPFE sheetPFE) {
		String code = generateRandomCode();
		sheetPFE.setQrcode(code);
		sheetPFE.setNoteEncadreur(-1);
		sheetPFE.setNoteRapporteur(-1);
		sheetPFE.setEtat(EtatSheetPFE.DEFAULT);
		em.persist(sheetPFE);

		try {
			new QRCode().writeQRCode(sheetPFE);
			new Email().sendQRCodeSheetPFE(sheetPFE);

		} catch (WriterException | IOException | NamingException | MessagingException e) {
			e.printStackTrace();
		}

		return sheetPFE.getId();

	}


	//test
	@Override
	public List<Etudiant> getAllStudentNoSheet(int startyear, int toyear) {

		if(toyear == 0 && startyear == 0) {
			
			return em.createQuery(
					"select e from Etudiant e LEFT JOIN e.sheetPFE s ON s.etudiant.id = e.id where s.etudiant.id IS NULL",
					Etudiant.class).getResultList();
			
		} else if (toyear == 0) {

			return em.createQuery(
					"select e from Etudiant e LEFT JOIN e.sheetPFE s ON s.etudiant.id = e.id where e.classe.anneeDeDebut = :year   and s.etudiant.id IS NULL ",
					Etudiant.class).setParameter("year", startyear).getResultList();
		} else {

			return em.createQuery(
					"select e from Etudiant e LEFT JOIN e.sheetPFE s ON s.etudiant.id = e.id where e.classe.anneeDeDebut BETWEEN :startyear and :toyear   and s.etudiant.id IS NULL ",
					Etudiant.class).setParameter("startyear", startyear).setParameter("toyear", toyear).getResultList();
		}

	}

	//test
	@Override
	public void reminderStudentNoSheet(List<Etudiant> students) {

		for (Etudiant etudiant : students) {
			try {
				new Email().reminderStudent(etudiant);
			} catch (NamingException | MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	//test
	@Override
	public List<SheetPFE> getAllSheetPFEFilter(EtatSheetPFE etat, int year, String pays, int id_categorie) {

		if (etat.equals(EtatSheetPFE.ALL)) {

			if (year == 0) {

				if (pays.equals("ALL")) {

					if (id_categorie == 0) {

						return em.createQuery("select s from SheetPFE s", SheetPFE.class).getResultList();

					} else {

						return em.createQuery("select s from SheetPFE s join s.categories c where c.id= :idcategorie",
								SheetPFE.class).setParameter("idcategorie", id_categorie).getResultList();
					}

				} else {

					if (id_categorie == 0) {

						return em.createQuery("select s from SheetPFE s join s.entreprise p where p.Pays= :pays",
								SheetPFE.class).setParameter("pays", pays).getResultList();

					} else {

						return em.createQuery(
								"select s from SheetPFE s join s.entreprise p join s.categories c where p.Pays= :pays and c.id= :idcategorie",
								SheetPFE.class).setParameter("pays", pays).setParameter("idcategorie", id_categorie)
								.getResultList();
					}

				}

			} else {

				if (pays.equals("ALL")) {

					if (id_categorie == 0) {

						return em.createQuery(
								"select s from SheetPFE s join s.etudiant e where e.classe.anneeDeDebut = :year",
								SheetPFE.class).setParameter("year", year).getResultList();

					} else {

						return em.createQuery(
								"select s from SheetPFE s join s.etudiant e join s.categories c where c.id= :idcategorie  and e.classe.anneeDeDebut = :year",
								SheetPFE.class).setParameter("year", year).setParameter("idcategorie", id_categorie)
								.getResultList();
					}

				} else {

					if (id_categorie == 0) {

						return em.createQuery(
								"select s from SheetPFE s join s.etudiant e join s.entreprise p where p.Pays= :pays and e.classe.anneeDeDebut = :year",
								SheetPFE.class).setParameter("year", year).setParameter("pays", pays).getResultList();

					} else {

						return em.createQuery(
								"select s from SheetPFE s join s.etudiant e join s.entreprise p join s.categories c where p.Pays= :pays and  c.id= :idcategorie  and e.classe.anneeDeDebut = :year",
								SheetPFE.class).setParameter("year", year).setParameter("idcategorie", id_categorie)
								.setParameter("pays", pays).getResultList();

					}

				}

			}

		} else {

			if (year == 0) {

				if (pays.equals("ALL")) {

					if (id_categorie == 0) {

						return em.createQuery("select s from SheetPFE s where s.etat = :etat ", SheetPFE.class)
								.setParameter("etat", etat).getResultList();

					} else {

						return em.createQuery(
								"select s from SheetPFE s join s.categories c where s.etat = :etat and  c.id= :idcategorie",
								SheetPFE.class).setParameter("etat", etat).setParameter("idcategorie", id_categorie)
								.getResultList();
					}

				} else {

					if (id_categorie == 0) {

						return em.createQuery(
								"select s from SheetPFE s join s.entreprise p where s.etat = :etat and  p.Pays= :pays",
								SheetPFE.class).setParameter("etat", etat).setParameter("pays", pays).getResultList();

					} else {

						return em.createQuery(
								"select s from SheetPFE s join s.entreprise p join s.categories c where s.etat = :etat and  p.Pays= :pays and c.id= :idcategorie",
								SheetPFE.class).setParameter("etat", etat).setParameter("pays", pays)
								.setParameter("idcategorie", id_categorie).getResultList();
					}

				}

			} else {

				if (pays.equals("ALL")) {

					if (id_categorie == 0) {

						return em.createQuery(
								"select s from SheetPFE s join s.etudiant e where s.etat = :etat and  e.classe.anneeDeDebut = :year",
								SheetPFE.class).setParameter("etat", etat).setParameter("year", year).getResultList();

					} else {

						return em.createQuery(
								"select s from SheetPFE s join s.etudiant e join s.categories c where s.etat = :etat and  c.id= :idcategorie  and e.classe.anneeDeDebut = :year",
								SheetPFE.class).setParameter("etat", etat).setParameter("year", year)
								.setParameter("idcategorie", id_categorie).getResultList();
					}

				} else {

					if (id_categorie == 0) {

						return em.createQuery(
								"select s from SheetPFE s join s.etudiant e join s.entreprise p where s.etat = :etat and  p.Pays= :pays and e.classe.anneeDeDebut = :year",
								SheetPFE.class).setParameter("etat", etat).setParameter("year", year)
								.setParameter("pays", pays).getResultList();

					} else {

						return em.createQuery(
								"select s from SheetPFE s join s.etudiant e join s.entreprise p join s.categories c where s.etat = :etat and  p.Pays= :pays and  c.id= :idcategorie  and e.classe.anneeDeDebut = :year",
								SheetPFE.class).setParameter("etat", etat).setParameter("year", year)
								.setParameter("idcategorie", id_categorie).setParameter("pays", pays).getResultList();

					}

				}

			}

		}

	}

	//test
	@Override
	public List<SheetPFE> getAllSheetPFEDefault() {

		Date date = new Date();

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;

		if (1 <= month && month <= 5) {
			year = year - 1;
		}

		return em.createQuery(
				"select s from SheetPFE s join s.etudiant e where s.etat ='DEFAULT' and e.classe.anneeDeDebut = :year order by(s.id)",
				SheetPFE.class).setParameter("year", year).getResultList();
	}

	//test
	@Override
	public boolean verificationByDirectorSheetPFE(int sheet_id, EtatSheetPFE etat, int user_id) {

		Enseignant directeur = em.find(Enseignant.class, user_id);
		
		try {
			SheetPFE sheetPFE = em.find(SheetPFE.class, sheet_id);
			
			
			if (etat.equals(EtatSheetPFE.REFUSE)) {
				sheetPFE.setEtat(etat);
				sheetPFE.setNote("Entreprise does not exist");
				em.merge(sheetPFE);
				
				PFENotification notification = new PFENotification();
				notification.setCreated(new Date());
				notification.setEnseignant(directeur);
				notification.setEtudiant(sheetPFE.getEtudiant());
				notification.setSendby("DirecteurDesStages");
				notification.setType(TypeNotification.REFUSE);
				notification.setTitle("Refuse sheet PFE");
				notification.setNote("Entreprise does not exist");
				
	
				em.persist(notification);
				
				new Email().entrepriseNotExist(sheetPFE);

			}else {
				sheetPFE.setEtat(etat);
				sheetPFE.setNote("Entreprise exist");
				em.merge(sheetPFE);
			}

				

			return true;

		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public int requestCancelInternship(int sheet_id, int user_id) {

		Etudiant etudiant = em.find(Etudiant.class, user_id);

		List<PFENotification> listnotify = em
				.createQuery("select n from PFENotification n join n.etudiant e where e.id= :id and sendby='DIRECTEUR'",
						PFENotification.class)
				.setParameter("id", etudiant.getId()).getResultList();

		PFENotification notifyby = listnotify.stream().findFirst().get();

		SheetPFE sheetPFE = em.find(SheetPFE.class, sheet_id);

		RequestCancelInternship req = new RequestCancelInternship();
		req.setCreated(new Date());
		req.setSheetPFE(sheetPFE);
		req.setEtat(EtatSheetPFE.DEFAULT);
		em.persist(req);

		PFENotification notification = new PFENotification();
		notification.setCreated(new Date());
		notification.setEnseignant(notifyby.getEnseignant());
		notification.setEtudiant(sheetPFE.getEtudiant());
		notification.setSendby("Etudiant");
		notification.setType(TypeNotification.ACCEPTED);
		notification.setTitle("Request Cancel Internship");
		notification.setNote(sheetPFE.getEtudiant().getPrenom() + " " + sheetPFE.getEtudiant().getNom()
				+ " send request cancel internship");

		em.persist(notification);

		return req.getId();
	}

	@Override
	public List<RequestCancelInternship> getAllRequest() {
		return em.createQuery("select r from RequestCancelInternship r where r.etat= 'DEFAULT'",
				RequestCancelInternship.class).getResultList();

	}

	@Override
	public RequestCancelInternship getResquest(int id) {
		return em.find(RequestCancelInternship.class, id);
	}

	@Override
	public boolean updateRequest(int request_id, EtatSheetPFE etat, String note,int user_id) {

		Enseignant directeur = em.find(Enseignant.class, user_id);

		try {

			RequestCancelInternship req = em.find(RequestCancelInternship.class, request_id);
			req.setEtat(etat);
			req.setNote(note);
			em.merge(req);

			SheetPFE sheetPFE = em.find(SheetPFE.class, req.getSheetPFE().getId());

			if (etat.equals(EtatSheetPFE.ACCEPTED)) {
				note = "Your request to cancel the internship accepted.";
				sheetPFE.setEtat(EtatSheetPFE.CANCEL);
				em.merge(sheetPFE);
				
				PFENotification notification = new PFENotification();
				notification.setCreated(new Date());
				notification.setEnseignant(directeur);
				notification.setEtudiant(sheetPFE.getEtudiant());
				notification.setTitle("Internship Cancel");
				notification.setSendby("DirecteurDesStages");
				notification.setType(TypeNotification.ACCEPTED);
				notification.setNote(note);

				em.persist(notification);

			} else if (etat.equals(EtatSheetPFE.REFUSE)) {

				PFENotification notification = new PFENotification();
				notification.setCreated(new Date());
				notification.setEnseignant(directeur);
				notification.setEtudiant(sheetPFE.getEtudiant());
				notification.setSendby("DirecteurDesStages");
				notification.setType(TypeNotification.REFUSE);
				notification.setTitle("Request Refuse");
				notification
						.setNote("Your request to cancel the internship has been refused. Problem of refusing " + note);

				em.persist(notification);

			}

			new Email().requestCancelInternship(sheetPFE, note);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<SheetPFE> getAllSheetValidate() {
		return em.createQuery("select s from SheetPFE s where s.etat = 'VALIDATE' ", SheetPFE.class).getResultList();
	}

	//test
	@Override
	public List<SheetPFE> getAllSheetPFEAccepted() {
		return em.createQuery("select s from SheetPFE s where s.etat = 'ACCEPTED' ", SheetPFE.class).getResultList();
	}

	//test
	@Override
	public List<Enseignant> getValidateurByCategories(int sheet_id) {


		List<Enseignant> listEnseignant = new ArrayList<Enseignant>();
		List<Enseignant> listEnseignantOrderByCategories = em.createQuery(
				"select e from Enseignant e join  e.categories c join c.sheetPFEs s where e.role='Enseignant' and s.id=:id group by(e) order by(count(e)) desc",
				 Enseignant.class).setParameter("id", sheet_id).getResultList();

		for (Enseignant enseignant : listEnseignantOrderByCategories) {
			Long count = (Long) em.createQuery(
					"select count(e) from  Enseignant e join e.enseignantsheet es where es.type= 'VALIDATEUR' and e.id = :id")
					.setParameter("id", enseignant.getId()).getSingleResult();

			if (enseignant.getSite().getMaxPreValidateur() > count.intValue()) {
				listEnseignant.add(enseignant);
			}
		}

		return listEnseignant;

	}

	//test
	@Override
	public boolean affectValidateurToSheetPFE(int sheet_id,int enseignant_id, int user_id) {

		Enseignant chef = em.find(Enseignant.class, user_id);
		try {
			
			SheetPFE sheetPFE = em.find(SheetPFE.class, sheet_id);
			Enseignant enseignant = em.find(Enseignant.class, enseignant_id);

			EnseignantSheetPFE enseignantSheetPFE = new EnseignantSheetPFE();
			enseignantSheetPFE.setEnseignant(enseignant);
			enseignantSheetPFE.setSheetPFE(sheetPFE);
			enseignantSheetPFE.setType("VALIDATEUR");
			em.merge(enseignantSheetPFE);

			sheetPFE.setEtat(EtatSheetPFE.PRE_VALIDATE);
			em.merge(sheetPFE);

			PFENotification notification = new PFENotification();
			notification.setCreated(new Date());
			notification.setEnseignant(chef);
			notification.setEtudiant(sheetPFE.getEtudiant());
			notification.setSendby("ChefDeDepartement");
			notification.setType(TypeNotification.AFFECT);
			notification.setTitle("Affect Validateur");
			notification.setNote("Chef Department affect validateur '" + enseignant.getNom() + " "
					+ enseignant.getPrenom() + "' to your sheet PFE");

			em.persist(notification);

			new Email().affectEnseignantToSheetPFE(sheetPFE, enseignant, "VALIDATEUR");

			return true;

		} catch (Exception e) {
			return false;
		}
	}

	//test
	@Override
	public boolean validateSheetPFE(int sheet_id, EtatSheetPFE etat, String note,int user_id) {

		Enseignant enseignant = em.find(Enseignant.class, user_id);

		try {

			SheetPFE sheetPFE = em.find(SheetPFE.class, sheet_id);

			if (etat.equals(EtatSheetPFE.VALIDATE)) {

				note = "Your subject of PFE has been accepted";
				sheetPFE.setNote(note);
				sheetPFE.setEtat(etat);
				em.merge(sheetPFE);

				PFENotification notification = new PFENotification();
				notification.setCreated(new Date());
				notification.setEnseignant(enseignant);
				notification.setEtudiant(sheetPFE.getEtudiant());
				notification.setSendby("Enseignant");
				notification.setType(TypeNotification.ACCEPTED);
				notification.setTitle("Validated Sheet PFE");
				notification.setNote(note);

				em.persist(notification);

				new Email().validateSheetPFE(sheetPFE, etat, note);

			} else {
				sheetPFE.setNote(note);
				sheetPFE.setEtat(etat);
				em.merge(sheetPFE);

				PFENotification notification = new PFENotification();
				notification.setCreated(new Date());
				notification.setEnseignant(enseignant);
				notification.setEtudiant(sheetPFE.getEtudiant());
				notification.setSendby("Enseignant");
				notification.setType(TypeNotification.REFUSE);
				notification.setTitle("Refused subject PFE");
				notification.setNote(note);

				em.persist(notification);

				new Email().validateSheetPFE(sheetPFE, etat, note);

			}

			return true;

		} catch (Exception e) {
			return false;
		}
	}

	//test
	@Override
	public List<SheetPFE> getAllSheetWaitEncadreur() {
		return em.createQuery(
				"select s from SheetPFE s where s.id not in (select s.id from EnseignantSheetPFE es Left join es.sheetPFE s  where es.type = 'ENCADREUR') and s.etat='VALIDATE' ",
				SheetPFE.class).getResultList();
	}

	@Override
	public List<Enseignant> getAllEnseignantOrderByEncadrement() {
		return em.createQuery(
				"select e.id from Enseignant e JOIN e.enseignantsheet es where es.type='ENCADREUR' group by(e.id) order by(count(e))",
				Enseignant.class).getResultList();
	}

	//test
	@Override
	public List<Enseignant> getEncardeurByCategories(int sheet_id) {

		List<Enseignant> listEnseignant = new ArrayList<Enseignant>();
		List<Enseignant> listEnseignantOrderByCategories = em.createQuery(
				"select e from Enseignant e join  e.categories c join c.sheetPFEs s where e.role='Enseignant' and s.id=:id group by(e) order by(count(e)) desc",
				Enseignant.class).setParameter("id", sheet_id).getResultList();

		for (Enseignant enseignant : listEnseignantOrderByCategories) {
			Long count = (Long) em.createQuery(
					"select count(e) from  Enseignant e join e.enseignantsheet es where es.type= 'ENCADREUR' and e.id = :id")
					.setParameter("id", enseignant.getId()).getSingleResult();

			if (enseignant.getSite().getMaxEncadrant() > count.intValue()) {
				listEnseignant.add(enseignant);
			}
		}

		return listEnseignant;

	}

	//test
	@Override
	public String affectEncadreurToSheetPFEAuto(int sheet_id, int user_id) {

		Enseignant chef = em.find(Enseignant.class, user_id);

		try {

			SheetPFE sheetPFE = em.find(SheetPFE.class, sheet_id);

			if (getEncardeurByCategories(sheet_id).size() == 0) {
				return "NO_CONTENT";
			}

			Enseignant enseignant = getEncardeurByCategories(sheet_id).stream().findFirst().get();
			EnseignantSheetPFE enseignantSheetPFE = new EnseignantSheetPFE();
			enseignantSheetPFE.setEnseignant(enseignant);
			enseignantSheetPFE.setSheetPFE(sheetPFE);
			enseignantSheetPFE.setType("ENCADREUR");
			em.merge(enseignantSheetPFE);

			PFENotification notification = new PFENotification();
			notification.setCreated(new Date());
			notification.setEnseignant(chef);
			notification.setEtudiant(sheetPFE.getEtudiant());
			notification.setSendby("ChefDeDepartement");
			notification.setType(TypeNotification.AFFECT);
			notification.setTitle("Affect Encadreur");
			notification.setNote("Chef Department affect encadreur '" + enseignant.getNom() + " "
					+ enseignant.getPrenom() + "' to your sheet PFE");

			em.persist(notification);

			new Email().affectEnseignantToSheetPFE(sheetPFE, enseignant, "ENCADREUR");

			return "SUCCESS";

		} catch (Exception e) {
			return "NOT_MODIFIED";
		}

	}

	//test
	@Override
	public boolean affectEncadreurToSheetPFEManual(int sheet_id, int enseignant_id,int user_id) {

		Enseignant chef = em.find(Enseignant.class, user_id);

		try {

			SheetPFE sheetPFE = em.find(SheetPFE.class, sheet_id);
			Enseignant enseignant = em.find(Enseignant.class, enseignant_id);
			EnseignantSheetPFE enseignantSheetPFE = new EnseignantSheetPFE();
			enseignantSheetPFE.setEnseignant(enseignant);
			enseignantSheetPFE.setSheetPFE(sheetPFE);
			enseignantSheetPFE.setType("ENCADREUR");
			em.merge(enseignantSheetPFE);

			PFENotification notification = new PFENotification();
			notification.setCreated(new Date());
			notification.setEnseignant(chef);
			notification.setEtudiant(sheetPFE.getEtudiant());
			notification.setSendby("ChefDeDepartement");
			notification.setType(TypeNotification.AFFECT);
			notification.setTitle("Affect Encadreur");
			notification.setNote("Chef Department affect encadreur '" + enseignant.getNom() + " "
					+ enseignant.getPrenom() + "' to your sheet PFE");

			em.persist(notification);

			new Email().affectEnseignantToSheetPFE(sheetPFE, enseignant, "ENCADREUR");

			return true;

		} catch (Exception e) {
			return false;

		}
	}

	//test
	@Override
	public boolean updateEncadreurSheetPFE(int sheetPFE_id, int enseignant_id,int user_id) {

		Enseignant chef = em.find(Enseignant.class, user_id);
		try {

			EnseignantSheetPFE enseignantSheetPFE = em.createQuery(
					"select e from EnseignantSheetPFE e left join e.sheetPFE s where s.id = :sheetPFE_id and e.type='ENCADREUR' ",
					EnseignantSheetPFE.class).setParameter("sheetPFE_id", sheetPFE_id).getSingleResult();
			Enseignant enseignant = em.find(Enseignant.class, enseignant_id);
			SheetPFE sheetPFE = em.find(SheetPFE.class, sheetPFE_id);

			enseignantSheetPFE.setEnseignant(enseignant);
			em.merge(enseignantSheetPFE);

			PFENotification notification = new PFENotification();
			notification.setCreated(new Date());
			notification.setEnseignant(chef);
			notification.setEtudiant(sheetPFE.getEtudiant());
			notification.setSendby("ChefDeDepartement");
			notification.setType(TypeNotification.CHANGE);
			notification.setTitle("Modify Encadreur");
			notification.setNote("Chef departement set new encadreur'" + enseignant.getNom() + " "
					+ enseignant.getPrenom() + "' to your sheet PFE");

			em.persist(notification);

			new Email().changeEnseignantToSheetPFE(sheetPFE, enseignant, "ENCADREUR");

			return true;

		} catch (Exception e) {

			return false;
		}

	}

	//test
	@Override
	public List<Enseignant> getRapporteurByCategories(int sheet_id) {

		List<Enseignant> listEnseignant = new ArrayList<Enseignant>();
		List<Enseignant> listEnseignantOrderByCategories = em.createQuery(
				"select e from Enseignant e join  e.categories c join c.sheetPFEs s where e.role='Enseignant' and s.id=:id group by(e) order by(count(e)) desc",
				Enseignant.class).setParameter("id", sheet_id).getResultList();

		for (Enseignant enseignant : listEnseignantOrderByCategories) {
			Long count = (Long) em.createQuery(
					"select count(e) from  Enseignant e join e.enseignantsheet es where es.type= 'RAPPORTEUR' and e.id = :id")
					.setParameter("id", enseignant.getId()).getSingleResult();

			if (enseignant.getSite().getMaxRapporteur() > count.intValue()) {
				listEnseignant.add(enseignant);
			}
		}

		return listEnseignant;

	}

	//test
	@Override
	public String affectRapporteurToSheetPFEAuto(int sheet_id,int user_id) {

		Enseignant chef = em.find(Enseignant.class, user_id);

		try {

			SheetPFE sheetPFE = em.find(SheetPFE.class, sheet_id);

			if (getRapporteurByCategories(sheet_id).size() == 0) {
				return "NO_CONTENT";
			}
			Enseignant enseignant = getRapporteurByCategories(sheet_id).stream().findFirst().get();
			EnseignantSheetPFE enseignantSheetPFE = new EnseignantSheetPFE();
			enseignantSheetPFE.setEnseignant(enseignant);
			enseignantSheetPFE.setSheetPFE(sheetPFE);
			enseignantSheetPFE.setType("RAPPORTEUR");
			em.merge(enseignantSheetPFE);

			PFENotification notification = new PFENotification();
			notification.setCreated(new Date());
			notification.setEnseignant(chef);
			notification.setEtudiant(sheetPFE.getEtudiant());
			notification.setSendby("ChefDeDepartement");
			notification.setType(TypeNotification.AFFECT);
			notification.setTitle("Affect Rapporteur");
			notification.setNote("Chef Department affect rapporteur '" + enseignant.getNom() + " "
					+ enseignant.getPrenom() + "' to your sheet PFE");

			em.persist(notification);

			new Email().affectEnseignantToSheetPFE(sheetPFE, enseignant, "RAPPORTEUR");

			return "SUCCESS";

		} catch (Exception e) {
			return "NOT_MODIFIED";
		}

	}

	//test
	@Override
	public boolean affectRapporteurToSheetPFEManual(int sheet_id, int enseignant_id,int user_id) {

		Enseignant chef = em.find(Enseignant.class, user_id);

		try {
			SheetPFE sheetPFE = em.find(SheetPFE.class, sheet_id);
			Enseignant enseignant = em.find(Enseignant.class, enseignant_id);
			EnseignantSheetPFE enseignantSheetPFE = new EnseignantSheetPFE();
			enseignantSheetPFE.setEnseignant(enseignant);
			enseignantSheetPFE.setSheetPFE(sheetPFE);
			enseignantSheetPFE.setType("RAPPORTEUR");
			em.merge(enseignantSheetPFE);

			PFENotification notification = new PFENotification();
			notification.setCreated(new Date());
			notification.setEnseignant(chef);
			notification.setEtudiant(sheetPFE.getEtudiant());
			notification.setSendby("ChefDeDepartement");
			notification.setType(TypeNotification.AFFECT);
			notification.setTitle("Affect Rapporteur");
			notification.setNote("Chef Department affect rapporteur '" + enseignant.getNom() + " "
					+ enseignant.getPrenom() + "' to your sheet PFE");

			em.persist(notification);

			new Email().affectEnseignantToSheetPFE(sheetPFE, enseignant, "RAPPORTEUR");

			return true;

		} catch (Exception e) {
			return false;

		}
	}

	//test
	@Override
	public boolean updateRapporteurSheetPFE(int sheetPFE_id, int enseignant_id,int user_id) {

		Enseignant chef = em.find(Enseignant.class, user_id);

		try {

			EnseignantSheetPFE enseignantSheetPFE = em.createQuery(
					"select e from EnseignantSheetPFE e left join e.sheetPFE s where s.id = :sheetPFE_id and e.type='RAPPORTEUR' ",
					EnseignantSheetPFE.class).setParameter("sheetPFE_id", sheetPFE_id).getSingleResult();
			Enseignant enseignant = em.find(Enseignant.class, enseignant_id);
			
			SheetPFE sheetPFE = em.find(SheetPFE.class, sheetPFE_id);

			enseignantSheetPFE.setEnseignant(enseignant);
			em.merge(enseignantSheetPFE);

			PFENotification notification = new PFENotification();
			notification.setCreated(new Date());
			notification.setEnseignant(chef);
			notification.setEtudiant(sheetPFE.getEtudiant());
			notification.setSendby("ChefDeDepartement");
			notification.setType(TypeNotification.CHANGE);
			notification.setTitle("Modify Rapporteur");
			notification.setNote("Chef Department set new rapporteur '" + enseignant.getNom() + " "
					+ enseignant.getPrenom() + "' to your sheet PFE");

			em.persist(notification);

			new Email().changeEnseignantToSheetPFE(sheetPFE, enseignant, "RAPPORTEUR");

			return true;

		} catch (Exception e) {

			return false;
		}

	}

	//test
	@Override
	public List<SheetPFE> getAllSheetWaitRapporter() {
		return em.createQuery(
				"select s from SheetPFE s where s.id not in (select s.id from EnseignantSheetPFE es Left join es.sheetPFE s  where es.type = 'RAPPORTEUR') and s.etat ='VALIDATE'",
				SheetPFE.class).getResultList();
	}

	//test
	@Override
	public List<Enseignant> getAllValidateur() {
		return em.createQuery("select e from Enseignant e where e.role='VALIDATEUR'", Enseignant.class).getResultList();

	}

	//test
	@Override
	public SheetPFE getSheetPFEById(int id) {
		try {
			SheetPFE sheetPFE = em.find(SheetPFE.class, id);
			return sheetPFE;
		} catch (Exception e) {
			return null;
		}
	}

	//test
	@Override
	public SheetPFE getSheetPFEByEtudiant(int user_id) {
		try {
			System.out.println(user_id);
			return em.createQuery("select s from SheetPFE s join s.etudiant e where e.id=:etudiantId", SheetPFE.class)
					.setParameter("etudiantId", user_id).getSingleResult();
			
		} catch (Exception e) {
			return null;
		}
		
	}

	//test
	@Override
	public boolean updateSheetPFE(SheetPFE sheetPFE) {

		SheetPFE oldsheet = em.find(SheetPFE.class, sheetPFE.getId());
		try {

			if (oldsheet.getEtat().equals(EtatSheetPFE.DEFAULT)) {
				em.merge(sheetPFE);
			} else if (oldsheet.getEtat().equals(EtatSheetPFE.REFUSE)) {

				SheetPFEModification sheetPFEModification = new SheetPFEModification();
				sheetPFEModification.setTitle(oldsheet.getTitle());
				sheetPFEModification.setDescription(oldsheet.getDescription());
				sheetPFEModification.setFeatures(oldsheet.getFeatures());
				sheetPFEModification.setProblematic(oldsheet.getProblematic());
				sheetPFEModification.setCreated(new Date());
				sheetPFEModification.setSheetPFE(oldsheet);
				sheetPFEModification.setEtat(EtatSheetPFE.ACCEPTED);
				sheetPFEModification.getCategories().addAll(oldsheet.getCategories());
				sheetPFEModification.setEntreprise(oldsheet.getEntreprise());

				em.persist(sheetPFEModification);
				
				sheetPFE.setEtat(EtatSheetPFE.DEFAULT);

				em.merge(sheetPFE);

				List<PFENotification> listnotify = em.createQuery(
						"select n from PFENotification n join n.etudiant e where e.id= :id and sendby='DirecteurDesStages'",
						PFENotification.class).setParameter("id", sheetPFE.getEtudiant().getId()).getResultList();

				PFENotification notifyby = listnotify.stream().findFirst().get();

				PFENotification notification = new PFENotification();
				notification.setCreated(new Date());
				notification.setEnseignant(notifyby.getEnseignant());
				notification.setEtudiant(sheetPFE.getEtudiant());
				notification.setSendby("Etudiant");
				notification.setType(TypeNotification.CHANGE);
				notification.setTitle("Change sheet PFE");
				notification.setNote(sheetPFE.getEtudiant().getPrenom() + " " + sheetPFE.getEtudiant().getNom() + " changed the sheet PFE");

				em.persist(notification);

			} else if (oldsheet.getEtat().equals(EtatSheetPFE.PRE_VALIDATE) || oldsheet.getEtat().equals(EtatSheetPFE.REQUEST)) {

				SheetPFEModification sheetPFEModification = new SheetPFEModification();
				sheetPFEModification.setTitle(oldsheet.getTitle());
				sheetPFEModification.setDescription(oldsheet.getDescription());
				sheetPFEModification.setFeatures(oldsheet.getFeatures());
				sheetPFEModification.setProblematic(oldsheet.getProblematic());
				sheetPFEModification.setCreated(new Date());
				sheetPFEModification.setSheetPFE(oldsheet);
				sheetPFEModification.setEtat(EtatSheetPFE.ACCEPTED);
				sheetPFEModification.getCategories().addAll(oldsheet.getCategories());
				sheetPFEModification.setEntreprise(oldsheet.getEntreprise());

				em.persist(sheetPFEModification);
				sheetPFE.setEtat(EtatSheetPFE.PRE_VALIDATE);
				sheetPFE.getEnseignantsheet().removeAll(sheetPFE.getEnseignantsheet());
				sheetPFE.getEnseignantsheet().addAll(sheetPFE.getEnseignantsheet());
				
				em.merge(sheetPFE);

				Enseignant enseignant = em.createQuery(
						"select e from Enseignant e join e.enseignantsheet es join es.sheetPFE s where s.id = :id and es.type='VALIDATEUR'",
						Enseignant.class).setParameter("id", sheetPFE.getId()).getSingleResult();

				PFENotification notification = new PFENotification();
				notification.setCreated(new Date());
				notification.setEnseignant(enseignant);
				notification.setEtudiant(sheetPFE.getEtudiant());
				notification.setSendby("Etudiant");
				notification.setType(TypeNotification.CHANGE);
				notification.setTitle("Change sheet PFE");
				notification.setNote(sheetPFE.getEtudiant().getPrenom() + " " + sheetPFE.getEtudiant().getNom() + " changed the sheet PFE");

				em.persist(notification);

			} else if (oldsheet.getEtat().equals(EtatSheetPFE.VALIDATE)) {

				if (oldsheet.getFeatures().equals(sheetPFE.getFeatures())
						&& oldsheet.getProblematic().equals(sheetPFE.getProblematic())) {
					SheetPFEModification sheetPFEModification = new SheetPFEModification();
					sheetPFEModification.setTitle(oldsheet.getTitle());
					sheetPFEModification.setDescription(oldsheet.getDescription());
					sheetPFEModification.setFeatures(oldsheet.getFeatures());
					sheetPFEModification.setProblematic(oldsheet.getProblematic());
					sheetPFEModification.setCreated(new Date());
					sheetPFEModification.setSheetPFE(oldsheet);
					sheetPFEModification.setEtat(EtatSheetPFE.ACCEPTED);
					sheetPFEModification.getCategories().addAll(oldsheet.getCategories());
					sheetPFEModification.setEntreprise(oldsheet.getEntreprise());

					em.persist(sheetPFEModification);
					
					sheetPFE.getEnseignantsheet().removeAll(sheetPFE.getEnseignantsheet());
					sheetPFE.getEnseignantsheet().addAll(sheetPFE.getEnseignantsheet());

					em.merge(sheetPFE);

					Enseignant enseignant = em.createQuery(
							"select e from Enseignant e join e.enseignantsheet es join es.sheetPFE s where s.id = :id and es.type='ENCADREUR'",
							Enseignant.class).setParameter("id", sheetPFE.getId()).getSingleResult();

					PFENotification notification = new PFENotification();
					notification.setCreated(new Date());
					notification.setEnseignant(enseignant);
					notification.setEtudiant(sheetPFE.getEtudiant());
					notification.setSendby("Etudiant");
					notification.setType(TypeNotification.CHANGE);
					notification.setTitle("Change sheet PFE");
					notification.setNote(sheetPFE.getEtudiant().getPrenom() + " " + sheetPFE.getEtudiant().getNom() + " changed the sheet PFE");

					em.persist(notification);

				} else {

					SheetPFEModification sheetPFEModification = new SheetPFEModification();
					sheetPFEModification.setTitle(sheetPFE.getTitle());
					sheetPFEModification.setDescription(sheetPFE.getDescription());
					sheetPFEModification.setFeatures(sheetPFE.getFeatures());
					sheetPFEModification.setProblematic(sheetPFE.getProblematic());
					sheetPFEModification.setCreated(new Date());
					sheetPFEModification.setSheetPFE(sheetPFE);
					sheetPFEModification.setEtat(EtatSheetPFE.DEFAULT);
					sheetPFEModification.getCategories().addAll(sheetPFE.getCategories());
					sheetPFEModification.setEntreprise(sheetPFE.getEntreprise());

					em.persist(sheetPFEModification);

					Enseignant enseignant = em.createQuery(
							"select e from Enseignant e join e.enseignantsheet es join es.sheetPFE s where s.id = :id and es.type='ENCADREUR'",
							Enseignant.class).setParameter("id", sheetPFE.getId()).getSingleResult();

					PFENotification notification = new PFENotification();
					notification.setCreated(new Date());
					notification.setEnseignant(enseignant);
					notification.setEtudiant(sheetPFE.getEtudiant());
					notification.setSendby("Etudiant");
					notification.setType(TypeNotification.CHANGE);
					notification.setTitle("Major change sheet PFE");
					notification.setNote(sheetPFE.getEtudiant().getPrenom() + " " + sheetPFE.getEtudiant().getNom()
							+ " changed the features and problematic of the sheet PFE");

					em.persist(notification);
				}

			}

			List<PFENotification> listnotify = em
					.createQuery("select n from PFENotification n join n.etudiant e where e.id= :id and sendby !='Etudiant' and n.type = 'REFUSE' and n.vu= 0",
							PFENotification.class)
					.setParameter("id", sheetPFE.getEtudiant().getId()).getResultList();
			
			for (PFENotification notify : listnotify) {
				notify.setVu(1);
				em.merge(notify);
			}
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	//test
	@Override
	public List<SheetPFE> getAllSheetPFEWaitNote() {
		return em.createQuery("select s from SheetPFE s where s.etat='VALIDATE' and (s.noteRapporteur= -1 or s.noteEncadreur= -1)",
				SheetPFE.class).getResultList();
	}

	public List<SheetPFE> getAllSheetPFEWaitPlaning() {
		return em.createQuery("select s from SheetPFE s where s.noteRapporteur > -1 and s.noteEncadreur > -1 ", SheetPFE.class)
				.getResultList();
	}

	//test
	@Override
	public List<SheetPFE> getAllSheetByEnseignant(int startyear, int toyear, String type, int user_id) {

		Enseignant enseignant = em.find(Enseignant.class, user_id);
		
		if (type.equals("ALL")) {

			return em.createQuery(
					"select s from SheetPFE s join s.enseignantsheet es join es.enseignant e where e.id = :id  and s.etudiant.classe.anneeDeDebut BETWEEN :startyear and :toyear  ",
					SheetPFE.class).setParameter("id", enseignant.getId()).setParameter("startyear", startyear)
					.setParameter("toyear", toyear).getResultList();
			
		} else {
			
			return em.createQuery(
					"select s from SheetPFE s join s.enseignantsheet es join es.enseignant e where e.id = :id and es.type = :type and  s.etudiant.classe.anneeDeDebut BETWEEN :startyear and :toyear  ",
					SheetPFE.class).setParameter("id", enseignant.getId()).setParameter("startyear", startyear).setParameter("type", type)
					.setParameter("toyear", toyear).getResultList();

		}
		
	}

	//test
	@Override
	public List<SheetPFE> getAllSheetByValidateur(int user_id) {

		Enseignant enseignant = em.find(Enseignant.class, user_id);

		return em.createQuery(
				"select s from SheetPFE s join s.enseignantsheet es join es.enseignant e where e.id = :id and es.type='VALIDATEUR' ",
				SheetPFE.class).setParameter("id", enseignant.getId()).getResultList();

	}

	//test
	@Override
	public List<SheetPFE> getAllSheetByEncadreur(int user_id) {

		Enseignant enseignant = em.find(Enseignant.class, user_id);

		return em.createQuery(
				"select s from SheetPFE s join s.enseignantsheet es join es.enseignant e where e.id = :id and es.type='ENCADREUR' ",
				SheetPFE.class).setParameter("id", enseignant.getId()).getResultList();

	}

	//test
	@Override
	public List<SheetPFE> getAllSheetByRapporteur(int user_id) {

		Enseignant enseignant = em.find(Enseignant.class, user_id);

		return em.createQuery(
				"select s from SheetPFE s join s.enseignantsheet es join es.enseignant e where e.id = :id and es.type='RAPPORTEUR' ",
				SheetPFE.class).setParameter("id", enseignant.getId()).getResultList();

	}

	//test
	@Override
	public boolean addNoteEncadreur(int note, int sheet_id) {
		try {
			SheetPFE sheetPFE = em.find(SheetPFE.class, sheet_id);
			sheetPFE.setNoteEncadreur(note);
			return true;
		} catch (Exception e) {

			return false;

		}
	}

	//test
	@Override
	public boolean addNoteRapporteur(int note, int sheet_id) {

		try {
			SheetPFE sheetPFE = em.find(SheetPFE.class, sheet_id);
			sheetPFE.setNoteRapporteur(note);
			return true;
		} catch (Exception e) {

			return false;

		}
	}

	//test
	@Override
	public boolean accepteSheetModify(int sheet_id, EtatSheetPFE etat, String note,int user_id) {

		Enseignant enseignant = em.find(Enseignant.class, user_id);

		try {

			SheetPFEModification sheet = em.find(SheetPFEModification.class, sheet_id);

			if (etat.equals(EtatSheetPFE.ACCEPTED)) {

				note = "Your modification of sheet PFE accepted.";

				SheetPFE sheetPFE = em.find(SheetPFE.class, sheet.getSheetPFE().getId());

				String title = sheetPFE.getTitle();
				String features = sheetPFE.getFeatures();
				String problematic = sheetPFE.getProblematic();
				String description = sheetPFE.getDescription();
				Set<Categorie> categories = sheetPFE.getCategories();
				

				sheetPFE.setFeatures(sheet.getFeatures());
				sheetPFE.setProblematic(sheet.getProblematic());
				sheetPFE.setDescription(sheet.getDescription());
				sheetPFE.setTitle(sheet.getTitle());
				sheetPFE.setCategories(sheet.getCategories());


				sheet.setFeatures(features);
				sheet.setProblematic(problematic);
				sheet.setTitle(title);
				sheet.setCategories(categories);
				sheet.setDescription(description);
				sheet.setEtat(etat);
				sheet.setNote(note);

				PFENotification notification = new PFENotification();
				notification.setCreated(new Date());
				notification.setEnseignant(enseignant);
				notification.setEtudiant(sheet.getSheetPFE().getEtudiant());
				notification.setSendby("Enseignant");
				notification.setType(TypeNotification.ACCEPTED);
				notification.setTitle("Modification accepted");
				notification.setNote(note);

				em.persist(notification);

				new Email().accepteSheetPFE(sheet);

			} else {

				sheet.setEtat(EtatSheetPFE.REFUSE);
				sheet.setNote(note);
				em.merge(sheet);

				PFENotification notification = new PFENotification();
				notification.setCreated(new Date());
				notification.setEnseignant(enseignant);
				notification.setEtudiant(sheet.getSheetPFE().getEtudiant());
				notification.setSendby("Enseignant");
				notification.setType(TypeNotification.REFUSE);
				notification.setTitle("Modification refuse");
				notification.setNote(note);

				em.persist(notification);

				new Email().accepteSheetPFE(sheet);

			}

			return true;

		} catch (Exception e) {
			return false;

		}
	}

	//test
	@Override
	public List<SheetPFEModification> getALLSheetModifyDefault() {

		return em.createQuery("select s from SheetPFEModification s  where s.etat = 'DEFAULT'  ",
				SheetPFEModification.class).getResultList();

	}

	//test
	@Override
	public SheetPFEModification getSheetModify(int sheet_id) {

		return em.find(SheetPFEModification.class, sheet_id);
	}

	//test
	@Override
	public List<PFENotification> getAllNotificationByEnseignant(int enseignant_id) {

		return em.createQuery("select n from PFENotification n join n.enseignant e  where e.id = :id ",
				PFENotification.class).setParameter("id", enseignant_id).getResultList();
	}

	//test
	@Override
	public List<PFENotification> getAllNotificationByEtudiant(int etudiant_id) {

		return em.createQuery("select n from PFENotification n join n.etudiant e  where e.id = :id ",
				PFENotification.class).setParameter("id", etudiant_id).getResultList();
	}

	private String generateRandomCode() {

		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 25) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}

	@Override
	public String exportSheetPFE(int sheet_id) {

		SheetPFE sheetPFE = em.find(SheetPFE.class, sheet_id);
		
		String filename ;
		try {
			Enseignant enseignant = em.createQuery(
					"select e from Enseignant e join e.enseignantsheet es join es.sheetPFE s where s.id = :id and es.type='ENCADREUR'",
					Enseignant.class).setParameter("id", sheetPFE.getId()).getSingleResult();
			
			filename = new PDF().generateSheetPFE(sheetPFE, enseignant);
   			sheetPFE.setPdf(filename);
			return filename;
			
		} catch (JRException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean uploadExcel(String file) {

		try {

			File excelFile = new File(file);
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sheet = wb.getSheetAt(0);

			Iterator<Row> rowIt = sheet.iterator();

			while (rowIt.hasNext()) {

				Row row = rowIt.next();

				Etudiant etudiant = em
						.createQuery("select e from Etudiant e where e.email = :email ", Etudiant.class)
						.setParameter("email", row.getCell(7).toString()).getSingleResult();


				if (etudiant.getSheetPFE() == null) {

					List<Categorie> listCategories = new ArrayList<Categorie>();

					String str = row.getCell(4).toString();
					String[] arrOfCat = str.split(",");

					for (String item : arrOfCat) {

						Categorie categorie = em
								.createQuery("select c from Categorie c where c.name = :name ", Categorie.class)
								.setParameter("name", item.toUpperCase()).getSingleResult();
						listCategories.add(categorie);
					}
					
					Entreprise entreprise = em
							.createQuery("select e from Entreprise e where e.EmailEntreprise = :email ", Entreprise.class)
							.setParameter("email", row.getCell(6).toString()).getSingleResult();
					
					SheetPFE sheetPFE = new SheetPFE();
					sheetPFE.setTitle(row.getCell(0).toString());
					sheetPFE.setDescription(row.getCell(1).toString());
					sheetPFE.setProblematic(row.getCell(2).toString());
					sheetPFE.setFeatures(row.getCell(3).toString());
					sheetPFE.setEtat(EtatSheetPFE.DEFAULT);
					sheetPFE.setQrcode(row.getCell(5).toString());
					sheetPFE.setEntreprise(entreprise);
					sheetPFE.setEtudiant(etudiant);
					sheetPFE.setNoteEncadreur(-1);
					sheetPFE.setNoteRapporteur(-1);
					sheetPFE.setEtudiant(etudiant);
					sheetPFE.getCategories().addAll(listCategories);
					em.persist(sheetPFE);

				}

			 }

			return true;

		} catch (IOException e) {
			return false;
		}

	}

	@Override
	public List<Object> dashboard() {

		Query q = em.createQuery(
				"select e.classe.anneeDeDebut, count(s) from SheetPFE s join s.entreprise es join s.etudiant e  where es.Pays != 'TUNISIE' and s.etat != 'CANCEL' group by(e.classe.anneeDeDebut) ");
		List<Object> list = (List<Object>) q.getResultList();

		return list;

	}

	@Override
	public boolean changeVu(int user_id, String role) {
		
		try {
			if (role.equals("Etudiant")) {
				
				List<PFENotification> listnotify = em
						.createQuery("select n from PFENotification n join n.etudiant e where e.id= :id and sendby !='Etudiant' and n.type != 'REFUSE' and n.vu=0",
								PFENotification.class)
						.setParameter("id", user_id).getResultList();
				
				for (PFENotification notify : listnotify) {
					notify.setVu(1);
					em.merge(notify);
				}
			} else {
				
				List<PFENotification> listnotify = em
						.createQuery("select n from PFENotification n join n.enseignant e where e.id= :id and sendby='Etudiant' and vu=0",
								PFENotification.class)
						.setParameter("id", user_id).getResultList();
				
				for (PFENotification notify : listnotify) {
					notify.setVu(1);
					em.merge(notify);
				}
			}
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
