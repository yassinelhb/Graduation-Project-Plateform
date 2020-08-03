package tn.esprit.pfe.pdf;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import tn.esprit.pfe.entities.Categorie;
import tn.esprit.pfe.entities.Enseignant;
import tn.esprit.pfe.entities.InternshipAgreemen;
import tn.esprit.pfe.entities.SheetPFE;
import tn.esprit.pfe.services.SheetPFEService;

public class PDF {
	

	public String generateInternshipAgreemen(InternshipAgreemen internshipAgreemen) throws JRException {

		Map<String, Object> params = new HashMap<>();
		JRDataSource jrDataSource = new JREmptyDataSource();
		params.put("nameEntreprise", internshipAgreemen.getEntreprise().getNameEntreprise());
		//params.put("nameResponsable", internshipAgreemen.getEntreprise().getNomPrenomResponsable());
		params.put("tel", internshipAgreemen.getEntreprise().getTelResponsable());
		params.put("fax", internshipAgreemen.getEntreprise().getTelEntreprise());
		params.put("emailEntreprise", internshipAgreemen.getEntreprise().getEmailEntreprise());
		//params.put("emailResponsable", internshipAgreemen.getEntreprise().getEmailResponsable());
		params.put("siteweb", internshipAgreemen.getEntreprise().getSiteweb());
		params.put("pays", internshipAgreemen.getEntreprise().getPays());
		params.put("adresse", internshipAgreemen.getEntreprise().getAdresse());
		params.put("nameEtudiant",
				internshipAgreemen.getEtudiant().getPrenom() + " " + internshipAgreemen.getEtudiant().getNom());
		params.put("emailEtudiant", internshipAgreemen.getEtudiant().getEmail());
		
		String path = getClass().getClassLoader().getResource("PDF/").getPath();
		String filename = "Internship" + internshipAgreemen.getId() + ".pdf";

		JasperReport jasperReport = JasperCompileManager.compileReport(path + "Internship.jrxml");
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, jrDataSource);
		JasperExportManager.exportReportToPdfFile(jasperPrint, path + filename);
		return filename;
	}

	public String generateSheetPFE(SheetPFE sheetPFE,Enseignant enseignant) throws JRException {

		
		Map<String, Object> params = new HashMap<>();
		JRDataSource jrDataSource = new JREmptyDataSource();
		params.put("NameEtudiant", sheetPFE.getEtudiant().getPrenom()+ " "+sheetPFE.getEtudiant().getNom());
		params.put("emailEtudiant", sheetPFE.getEtudiant().getEmail());
		params.put("nameEnseignant", enseignant.getPrenom()+ " "+enseignant.getNom());
		params.put("emailEnseignant", enseignant.getEmail());
		params.put("titre", sheetPFE.getTitle());
		params.put("description", sheetPFE.getDescription());
		params.put("fonctionnalite", sheetPFE.getFeatures());
		params.put("problematique", sheetPFE.getProblematic());
		
		String categorie = "";
		for (Categorie cat : sheetPFE.getCategories()) {
			categorie = categorie +", "+cat.getName(); 
		}
		params.put("categorie",categorie);
	
		
		String path = getClass().getClassLoader().getResource("PDF/").getPath();
		String filename = "Sheet" + sheetPFE.getId() + ".pdf";

		JasperReport jasperReport = JasperCompileManager.compileReport(path + "Sheet.jrxml");
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, jrDataSource);
		JasperExportManager.exportReportToPdfFile(jasperPrint, path + filename);
		
		return filename;
	}
	

	private String generatename() {

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
}
