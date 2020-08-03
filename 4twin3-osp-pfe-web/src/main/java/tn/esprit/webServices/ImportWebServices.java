package tn.esprit.webServices;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import rest.utilities.ExcelApiTest;
import rest.utilities.authentication.AuthenticationFilter;
import rest.utilities.authentication.Secure;
import tn.esprit.pfe.entities.Admin;
import tn.esprit.pfe.entities.Classe;
import tn.esprit.pfe.entities.Departement;
import tn.esprit.pfe.entities.Ecole;
import tn.esprit.pfe.entities.Etudiant;
import tn.esprit.pfe.entities.Site;
import tn.esprit.pfe.entities.Specialite;
import tn.esprit.pfe.services.AdminService;
import tn.esprit.pfe.services.EcoleService;
import tn.esprit.pfe.services.UserService;
import utilities.ValidationError;

@Path("import")
@RequestScoped
public class ImportWebServices {

	@EJB
	UserService us;

	@EJB
	AdminService as;

	@EJB
	EcoleService es;

	@Context
	private HttpHeaders headers;

	private XSSFWorkbook workbook;

	@POST
	@Secure(role = { "Admin" })
	@Consumes("multipart/form-data")
	@Produces(MediaType.APPLICATION_JSON)
	public Response importData(MultipartFormDataInput form) throws Exception {
		try {
			File uploadedInputStream = form.getFormDataPart("file", File.class, null);
			workbook = new XSSFWorkbook(uploadedInputStream);
			ExcelApiTest eat = new ExcelApiTest(uploadedInputStream);
			XSSFSheet sheet = workbook.getSheet("Site");
			Ecole e = new Ecole();
			e.setAdresse(form.getFormDataPart("adresse", String.class, null));
			e.setNom(form.getFormDataPart("nom", String.class, null));
			Set<Site> sites = new HashSet<>();
			for (int i = 2; i < sheet.getLastRowNum() + 2; i++) {
				Site s = new Site();
				s.setEcole(e);
				s.setAdresse(eat.getCellData("Site", "Adresse", i));
				s.setNom(eat.getCellData("Site", "Nom", i));
				String id = eat.getCellData("Site", "Id", i);
				Set<Departement> departements = new HashSet<>();
				XSSFSheet sheetDep = workbook.getSheet("Departement");
				System.out.println(sheetDep.getLastRowNum());
				for (int j = 2; j < sheetDep.getLastRowNum() + 2; j++) {
					if (eat.getCellData("Departement", "Site", j).equals(id)) {
						Departement d = new Departement();
						d.setSite(s);
						d.setNom(eat.getCellData("Departement", "Nom", j));
						String idDep = eat.getCellData("Departement", "Id", j);
						Set<Specialite> specialites = new HashSet<>();
						XSSFSheet sheetSpec = workbook.getSheet("Specialite");
						for (int k = 2; k < sheetSpec.getLastRowNum() + 2; k++) {
							if (eat.getCellData("Specialite", "Departement", k).equals(idDep)) {
								Specialite spec = new Specialite();
								spec.setDepartement(d);
								spec.setNom(eat.getCellData("Specialite", "Nom", k));
								Set<Classe> classes = new HashSet<Classe>();
								String idSpec = eat.getCellData("Specialite", "Id", k);
								XSSFSheet sheetClas = workbook.getSheet("Classe");
								for (int h = 2; h < sheetClas.getLastRowNum() + 2; h++) {
									if (eat.getCellData("Classe", "Specialite", h).equals(idSpec)) {
										System.out.println("Specialite=" + idSpec + " Classe="
												+ eat.getCellData("Classe", "Id", h));
										Classe c = new Classe();
										c.setAnneeDeDebut(Calendar.getInstance().get(Calendar.YEAR));
										c.setNumero((int) Double.parseDouble(eat.getCellData("Classe", "Numero", h)));
										Set<Etudiant> etudiants = new HashSet<Etudiant>();
										String idClas = eat.getCellData("Classe", "Id", h);
										XSSFSheet sheetEtud = workbook.getSheet("Etudiant");
										for (int l = 2; l < sheetEtud.getLastRowNum() + 2; l++) {
											if (eat.getCellData("Etudiant", "Classe", l).equals(idClas)) {
												System.out.println("Classe=" + idClas + " Etudiant="
														+ eat.getCellData("Etudiant", "Email", l));
												Etudiant et = new Etudiant();
												et.setClasse(c);
												et.setEmail(eat.getCellData("Etudiant", "Email", l));
												et.setPrenom(eat.getCellData("Etudiant", "Prenom", l));
												et.setNom(eat.getCellData("Etudiant", "Nom", l));
												et.setPassword(
														et.createPwd(eat.getCellData("Etudiant", "Identifiant", l)));
												et.setIdentifiant(eat.getCellData("Etudiant", "Identifiant", l));
												et.setRole(et.getClass().getSimpleName());
												etudiants.add(et);
											}
										}
										c.setEtudiants(etudiants);
										c.setSpecialite(spec);
										classes.add(c);
									}
								}
								spec.setClasses(classes);
								specialites.add(spec);
							}
						}
						d.setSpecialites(specialites);
						departements.add(d);
					}
				}
				s.setDepartements(departements);
				sites.add(s);
			}
			e.setSites(sites);
			AuthenticationFilter af = new AuthenticationFilter();
			Set<ValidationError> violations = es.addEcole(e, af.getIdUser(headers));
			if (violations == null) {
				return Response.status(Status.CREATED).entity("add successful").build();
			} else
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(violations).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	}

	@GET
	@Secure(role = { "Admin" })
	@Path("export")
	@Produces("application/vnd.ms-excel")
	public Response exportEcole() throws IOException {
		AuthenticationFilter af = new AuthenticationFilter();
		XSSFWorkbook workbook = new XSSFWorkbook();
		Ecole ecole = es.getEcoleAdmin(af.getIdUser(headers));

		XSSFSheet site = workbook.createSheet("Site");
		XSSFSheet departement = workbook.createSheet("Departement");
		XSSFSheet specialite = workbook.createSheet("Specialite");
		XSSFSheet classe = workbook.createSheet("Classe");
		XSSFSheet etudiant = workbook.createSheet("Etudiant");

		for (int i = 0; i < 6; i++) {
			site.setColumnWidth(i, 20*256);
			departement.setColumnWidth(i, 20*256);
			specialite.setColumnWidth(i, 20*256);
			classe.setColumnWidth(i, 20*256);
			etudiant.setColumnWidth(i, 20*256);
		}

		XSSFRow siteRow = site.createRow(0);
		XSSFCell cell = siteRow.createCell(0);
		XSSFCell cell1 = siteRow.createCell(1);
		XSSFCell cell2 = siteRow.createCell(2);
		cell.setCellValue("Id");
		cell1.setCellValue("Nom");
		cell2.setCellValue("Adresse");

		XSSFRow departementRow = departement.createRow(0);
		XSSFCell cell3 = departementRow.createCell(0);
		XSSFCell cell4 = departementRow.createCell(1);
		XSSFCell cell5 = departementRow.createCell(2);
		cell3.setCellValue("Id");
		cell4.setCellValue("Nom");
		cell5.setCellValue("Site");

		XSSFRow specialiteRow = specialite.createRow(0);
		XSSFCell cell6 = specialiteRow.createCell(0);
		XSSFCell cell7 = specialiteRow.createCell(1);
		XSSFCell cell8 = specialiteRow.createCell(2);
		cell6.setCellValue("Id");
		cell7.setCellValue("Nom");
		cell8.setCellValue("Departement");

		XSSFRow classeRow = classe.createRow(0);
		XSSFCell cell9 = classeRow.createCell(0);
		XSSFCell cell10 = classeRow.createCell(1);
		XSSFCell cell11 = classeRow.createCell(2);
		XSSFCell cell12 = classeRow.createCell(3);
		cell9.setCellValue("Id");
		cell10.setCellValue("Nom");
		cell11.setCellValue("Specialite");
		cell12.setCellValue("Nom");

		XSSFRow etudiantRow = etudiant.createRow(0);
		XSSFCell cell13 = etudiantRow.createCell(0);
		XSSFCell cell14 = etudiantRow.createCell(1);
		XSSFCell cell15 = etudiantRow.createCell(2);
		XSSFCell cell16 = etudiantRow.createCell(3);
		XSSFCell cell17 = etudiantRow.createCell(4);
		cell13.setCellValue("Identifiant");
		cell14.setCellValue("Nom");
		cell15.setCellValue("Prenom");
		cell16.setCellValue("Email");
		cell17.setCellValue("Classe");

		int rowCountSite = 0;
		int rowCountDepartement = 0;
		int rowCountSpecialite = 0;
		int rowCountClasse = 0;
		int rowCountEtudiant = 0;
		for (Site s : ecole.getSites()) {
			for (Departement d : s.getDepartements()) {
				for (Specialite sp : d.getSpecialites()) {
					for (Classe c : sp.getClasses()) {
						for (Etudiant e : c.getEtudiants()) {
							rowCountEtudiant++;
							XSSFRow rowEtudiant = etudiant.createRow(rowCountEtudiant);
							writeEtudiant(e, rowEtudiant);
						}
						rowCountClasse++;
						Row rowClasse = classe.createRow(rowCountClasse);
						writeClasse(c, rowClasse);
					}
					rowCountSpecialite++;
					Row rowSpecialite = specialite.createRow(rowCountSpecialite);
					writeSpecialite(sp, rowSpecialite);
				}
				rowCountDepartement++;
				Row rowDepartement = departement.createRow(rowCountDepartement);
				writeDepartement(d, rowDepartement);
			}
			rowCountSite++;
			Row rowSite = site.createRow(rowCountSite);
			writeSite(s, rowSite);
		}
		FileOutputStream file = new FileOutputStream("Export.xlsx");
		workbook.write(file);
		File f = new File("Export.xlsx");
		ResponseBuilder rb = Response.ok((Object)f);
		return rb.status(Status.OK).header("Content-Disposition",
				"attachment;filename=Export.xlsx").build();
	}

	private void writeSite(Site site, Row row) {
		Cell cell = row.createCell(0);
		cell.setCellValue(site.getId());

		cell = row.createCell(1);
		cell.setCellValue(site.getNom());

		cell = row.createCell(2);
		cell.setCellValue(site.getAdresse());
	}

	private void writeDepartement(Departement departement, Row row) {
		System.out.println(departement.getNom());
		Cell cell = row.createCell(0);
		cell.setCellValue(departement.getId());

		Cell cell2 = row.createCell(1);
		cell2.setCellValue(departement.getNom());

		Cell cell3 = row.createCell(2);
		cell3.setCellValue(departement.getSite().getId());
	}

	private void writeSpecialite(Specialite specialite, Row row) {
		Cell cell = row.createCell(0);
		cell.setCellValue(specialite.getId());

		Cell cell1 = row.createCell(1);
		cell1.setCellValue(specialite.getNom());

		Cell cell2 = row.createCell(2);
		cell2.setCellValue(specialite.getDepartement().getId());
	}

	private void writeClasse(Classe classe, Row row) {
		Cell cell = row.createCell(0);
		cell.setCellValue(classe.getId());

		Cell cell1 = row.createCell(1);
		cell1.setCellValue(classe.getNumero());

		Cell cell2 = row.createCell(2);
		cell2.setCellValue(classe.getSpecialite().getId());

		Cell cell3 = row.createCell(3);
		cell3.setCellValue("5" + classe.getSpecialite().getNom() + classe.getNumero());
	}

	private void writeEtudiant(Etudiant etudiant, Row row) {
		Cell cell = row.createCell(0);
		cell.setCellValue(etudiant.getIdentifiant());

		Cell cell1 = row.createCell(1);
		cell1.setCellValue(etudiant.getNom());

		Cell cell2 = row.createCell(2);
		cell2.setCellValue(etudiant.getPrenom());

		Cell cell3 = row.createCell(3);
		cell3.setCellValue(etudiant.getEmail());

		Cell cell4 = row.createCell(4);
		cell4.setCellValue("5" + etudiant.getClasse().getSpecialite().getNom() + etudiant.getClasse().getNumero());
	}

}
