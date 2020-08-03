package tn.esprit.webServices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import rest.utilities.authentication.AuthenticationFilter;
import tn.esprit.pfe.entities.Enseignant;
import tn.esprit.pfe.entities.EtatSheetPFE;
import tn.esprit.pfe.entities.Etudiant;
import tn.esprit.pfe.entities.PFENotification;
import tn.esprit.pfe.entities.RequestCancelInternship;
import tn.esprit.pfe.entities.SheetPFE;
import tn.esprit.pfe.entities.SheetPFEModification;
import tn.esprit.pfe.interfaces.SheetPFERemote;

@Path("sheet")
public class SheetPFEWebServices {

	@EJB
	SheetPFERemote IsheetPFE;
	
	@Context
	private HttpHeaders headers;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addSheetPFE(SheetPFE sheetPFE) {
		
		int id = IsheetPFE.addSheetPFE(sheetPFE);
		if(id > 0) 
			return Response.status(Status.CREATED).entity(IsheetPFE.getSheetPFEById(id)).build();
		
		return Response.status(Status.BAD_REQUEST).build();
		

	}

	@GET
	@Path("/nostudent/{startyear}/{toyear}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllStudentNoSheetWithYear(@PathParam(value = "startyear") int startyear,
			@PathParam(value = "toyear") int toyear) {

		List<Etudiant> listetudiant = IsheetPFE.getAllStudentNoSheet(startyear, toyear);

		if (listetudiant.size() != 0) {

			return Response.status(Status.ACCEPTED).entity(listetudiant).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}


	@POST
	@Path("/reminder")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response reminderStudentNoSheet(List<Etudiant> icn) {

		IsheetPFE.reminderStudentNoSheet(icn);
		return Response.status(Status.ACCEPTED).entity(icn).build();

	}

	@GET
	@Path("/{etat}/{year}/{pays}/{id_categorie}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSheetPFEFilter(@PathParam(value = "etat") EtatSheetPFE etat,
			@PathParam(value = "year") int year, @PathParam(value = "pays") String pays,
			@PathParam(value = "id_categorie") int id_categorie) {

		List<SheetPFE> listSheetPFE = IsheetPFE.getAllSheetPFEFilter(etat, year, pays, id_categorie);

		if (listSheetPFE.size() != 0) {

			return Response.status(Status.ACCEPTED).entity(listSheetPFE).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@GET
	@Path("/default")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSheetPFEDefault() {

		
		List<SheetPFE> listSheetPFE = IsheetPFE.getAllSheetPFEDefault();

		if (listSheetPFE.size() != 0) {

			return Response.status(Status.ACCEPTED).entity(listSheetPFE).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@GET
	@Path("/accepted")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSheetPFEAccepted() {

		List<SheetPFE> listsheet = IsheetPFE.getAllSheetPFEAccepted();

		if (listsheet.size() != 0) {

			return Response.status(Status.ACCEPTED).entity(listsheet).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@GET
	@Path("/validate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSheetPFEValidate() {

		List<SheetPFE> listSheetPFE = IsheetPFE.getAllSheetValidate();

		if (listSheetPFE.size() != 0) {

			return Response.status(Status.ACCEPTED).entity(listSheetPFE).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@GET
	@Path("/waitEncadreur")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSheetWaitEncadreur() {

		List<SheetPFE> listSheetPFE = IsheetPFE.getAllSheetWaitEncadreur();

		if (listSheetPFE.size() != 0) {

			return Response.status(Status.ACCEPTED).entity(listSheetPFE).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@GET
	@Path("/waitRapporter")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSheetWaitRapporter() {

		List<SheetPFE> listSheetPFE = IsheetPFE.getAllSheetWaitRapporter();

		if (listSheetPFE.size() != 0) {

			return Response.status(Status.ACCEPTED).entity(listSheetPFE).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAgreemenById(@PathParam(value = "id") int id) {

		if (IsheetPFE.getSheetPFEById(id) != null)
			return Response.status(Status.ACCEPTED).entity(IsheetPFE.getSheetPFEById(id)).build();

		return Response.status(Status.NO_CONTENT).build();
	}

	@GET
	@Path("/etudiant/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAgreemenByEtudiant(@PathParam(value = "id") int id) {
		
		if (IsheetPFE.getSheetPFEByEtudiant(id) != null)
			return Response.status(Status.ACCEPTED).entity(IsheetPFE.getSheetPFEByEtudiant(id)).build();

		return Response.status(Status.NO_CONTENT).build();

	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateSheet(SheetPFE sheetPFE) {

		if (IsheetPFE.updateSheetPFE(sheetPFE))
			return Response.status(Status.ACCEPTED).entity(IsheetPFE.getSheetPFEById(sheetPFE.getId())).build();

		return Response.status(Status.NOT_MODIFIED).build();

	}

	@PUT
	@Path("/verification/{sheet_id}/{etat}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response verificationByDirectorSheetPFE(@PathParam(value = "sheet_id") int sheet_id,
			@PathParam(value = "etat") EtatSheetPFE etat, @PathParam(value = "user_id") int user_id ) {
				
		if (IsheetPFE.verificationByDirectorSheetPFE(sheet_id, etat, user_id))
			return Response.status(Status.ACCEPTED).entity(IsheetPFE.getSheetPFEById(sheet_id)).build();

		return Response.status(Status.NOT_MODIFIED).build();

	}

	@POST
	@Path("/cancel/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response requestCancelInternship(@PathParam(value = "id") int id) {
		
		AuthenticationFilter af = new AuthenticationFilter();

		IsheetPFE.requestCancelInternship(id,af.getIdUser(headers));
		return Response.status(Status.CREATED).entity(IsheetPFE.getResquest(id)).build();

	}

	@GET
	@Path("/cancel")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCancelSheetPFE() {

		List<RequestCancelInternship> listRequest = IsheetPFE.getAllRequest();

		if (listRequest.size() != 0) {

			return Response.status(Status.ACCEPTED).entity(listRequest).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@GET
	@Path("/cancel/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getResquest(@PathParam(value = "id") int id) {

		if (IsheetPFE.getResquest(id) != null)
			return Response.status(Status.ACCEPTED).entity(IsheetPFE.getResquest(id)).build();

		return Response.status(Status.NOT_FOUND).build();
	}

	@PUT
	@Path("/cancel/{request_id}/{etat}/{note}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateRequest(@PathParam(value = "request_id") int request_id,
			@PathParam(value = "etat") EtatSheetPFE etat, @PathParam(value = "note") String note) {

		AuthenticationFilter af = new AuthenticationFilter();
		
		if (IsheetPFE.updateRequest(request_id, etat, note,af.getIdUser(headers)))
			return Response.status(Status.ACCEPTED).entity(IsheetPFE.getResquest(request_id)).build();

		return Response.status(Status.NOT_MODIFIED).build();

	}

	@GET
	@Path("/encadreur/{sheet_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEncardeurByCategories(@PathParam(value = "sheet_id") int id) {

		if (IsheetPFE.getEncardeurByCategories(id) != null) {

			return Response.status(Status.ACCEPTED).entity(IsheetPFE.getEncardeurByCategories(id)).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@POST
	@Path("/encadreur/affect/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response affectEncadreurToSheetPFEAuto(@PathParam(value = "id") int id) {

		AuthenticationFilter af = new AuthenticationFilter();
		if (IsheetPFE.affectEncadreurToSheetPFEAuto(id,af.getIdUser(headers)).equals("SUCCESS")) {

			return Response.status(Status.ACCEPTED).build();
		}
		if (IsheetPFE.affectEncadreurToSheetPFEAuto(id,af.getIdUser(headers)).equals("NO_CONTENT")) {

			return Response.status(Status.NO_CONTENT).build();

		} else {
			return Response.status(Status.NOT_MODIFIED).build();
		}

	}

	@POST
	@Path("/encadreur/affect/{idSheet}/{idEnseignant}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response affectEncadreurToSheetPFEManual(@PathParam(value = "idSheet") int idSheet,
			@PathParam(value = "idEnseignant") int idEnseignant,  @PathParam(value = "user_id") int user_id) {

		if (IsheetPFE.affectEncadreurToSheetPFEManual(idSheet, idEnseignant,user_id)) {

			return Response.status(Status.ACCEPTED).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@GET
	@Path("/rapporteur/{sheet_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRapporteurByCategories(@PathParam(value = "sheet_id") int id) {

		if (IsheetPFE.getRapporteurByCategories(id) != null) {

			return Response.status(Status.ACCEPTED).entity(IsheetPFE.getEncardeurByCategories(id)).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@POST
	@Path("/rapporteur/affect/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response affectRapporteurToSheetPFEAuto(@PathParam(value = "id") int id) {

		AuthenticationFilter af = new AuthenticationFilter();
		if (IsheetPFE.affectRapporteurToSheetPFEAuto(id,af.getIdUser(headers)).equals("SUCCESS")) {

			return Response.status(Status.ACCEPTED).build();

		} else if (IsheetPFE.affectRapporteurToSheetPFEAuto(id,af.getIdUser(headers)).equals("NO_CONTENT")) {

			return Response.status(Status.NO_CONTENT).build();

		} else {

			return Response.status(Status.NOT_MODIFIED).build();

		}

	}

	@POST
	@Path("/rapporteur/affect/{idSheet}/{idEnseignant}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response affectRapporteurToSheetPFEManual(@PathParam(value = "idSheet") int idSheet,
			@PathParam(value = "idEnseignant") int idEnseignant,  @PathParam(value = "user_id") int user_id) {

		AuthenticationFilter af = new AuthenticationFilter();
		if (IsheetPFE.affectRapporteurToSheetPFEManual(idSheet, idEnseignant, user_id)) {

			return Response.status(Status.ACCEPTED).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@PUT
	@Path("/rapporteur/{sheetPFE_id}/{enseignant_id}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateRapporteurSheetPFE(@PathParam(value = "sheetPFE_id") int sheetPFE_id,
			@PathParam(value = "enseignant_id") int enseignant_id, @PathParam(value = "user_id") int user_id) {

		if (IsheetPFE.updateRapporteurSheetPFE(sheetPFE_id, enseignant_id, user_id))
			return Response.status(Status.ACCEPTED).build();

		return Response.status(Status.NOT_MODIFIED).build();

	}

	@GET
	@Path("/enseignantorderbyencadrement")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllEnseignantOrderByEncadrement() {

		List<Enseignant> list = IsheetPFE.getAllEnseignantOrderByEncadrement();

		if (list.size() > 0)
			return Response.status(Status.ACCEPTED).entity(list).build();

		return Response.status(Status.NOT_FOUND).entity("efe").build();
	}

	@GET
	@Path("/enseignant/{startyear}/{toyear}/{type}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSheetByEnseignant(@PathParam(value = "startyear") int startyear,
			@PathParam(value = "toyear") int toyear, @PathParam(value = "type") String type, @PathParam(value = "user_id") int user_id) {

		List<SheetPFE> list = IsheetPFE.getAllSheetByEnseignant(startyear, toyear, type, user_id);

		if (list.size() > 0) {

			return Response.status(Status.ACCEPTED).entity(list).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@GET
	@Path("/encadreur")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSheetByEncadreur() {
		AuthenticationFilter af = new AuthenticationFilter();

		List<SheetPFE> list = IsheetPFE.getAllSheetByEncadreur(af.getIdUser(headers));

		if (list.size() > 0) {

			return Response.status(Status.ACCEPTED).entity(list).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@GET
	@Path("/rapporteur")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSheetByRapporteur() {

		AuthenticationFilter af = new AuthenticationFilter();
		List<SheetPFE> list = IsheetPFE.getAllSheetByRapporteur(af.getIdUser(headers));

		if (list.size() > 0) {

			return Response.status(Status.ACCEPTED).entity(list).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@GET
	@Path("/sheetvalidateur")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSheetByValidateur() {

		AuthenticationFilter af = new AuthenticationFilter();
		List<SheetPFE> list = IsheetPFE.getAllSheetByValidateur(af.getIdUser(headers));

		if (list.size() != 0) {

			return Response.status(Status.ACCEPTED).entity(list).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@PUT
	@Path("/encadreur/{sheetPFE_id}/{enseignant_id}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateEncadreurSheetPFE(@PathParam(value = "sheetPFE_id") int sheetPFE_id,
			@PathParam(value = "enseignant_id") int enseignant_id, @PathParam(value = "user_id") int user_id ) {

		if (IsheetPFE.updateEncadreurSheetPFE(sheetPFE_id, enseignant_id, user_id))
			return Response.status(Status.ACCEPTED).build();

		return Response.status(Status.NOT_MODIFIED).build();

	}

	@GET
	@Path("/validateur")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllValidateur() {

		List<Enseignant> list = IsheetPFE.getAllValidateur();

		if (list.size() != 0) {

			return Response.status(Status.ACCEPTED).entity(list).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}
	
	
	@GET
	@Path("/validateur/{sheet_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getValidateurByCategories(@PathParam(value = "sheet_id") int id) {

		if (IsheetPFE.getValidateurByCategories(id) != null) {

			return Response.status(Status.ACCEPTED).entity(IsheetPFE.getValidateurByCategories(id)).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@POST
	@Path("/validateur/affect/{sheet_id}/{idEnseignant}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response affectValidateurToSheetPFE(@PathParam(value = "sheet_id") int id, @PathParam(value = "idEnseignant") int idEnseignant, @PathParam(value = "user_id") int user_id) {
		
		if (IsheetPFE.affectValidateurToSheetPFE(id,idEnseignant,user_id)) {
			return Response.status(Status.ACCEPTED).build();
		}
		 else {
			return Response.status(Status.NOT_MODIFIED).build();
		}

	}
	
	@PUT
	@Path("/accptedsheetmodifiy/{sheet_id}/{etat}/{note}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response accepteSheetModify(@PathParam(value = "sheet_id") int sheet_id,
			@PathParam(value = "etat") EtatSheetPFE etat, @PathParam(value = "note") String note ,@PathParam(value = "user_id") int user_id) {

		if (IsheetPFE.accepteSheetModify(sheet_id, etat, note,user_id))
			return Response.status(Status.ACCEPTED).build();

		return Response.status(Status.NOT_MODIFIED).build();

	}

	@POST
	@Path("/valid/{sheet_id}/{etat}/{note}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response validateSheetPFE(@PathParam(value = "sheet_id") int sheet_id,
			@PathParam(value = "etat") EtatSheetPFE etat, @PathParam(value = "note") String note, @PathParam(value = "user_id") int user_id) {
		if (IsheetPFE.validateSheetPFE(sheet_id, etat, note, user_id))
			return Response.status(Status.ACCEPTED).build();

		return Response.status(Status.NOT_MODIFIED).build();

	}

	@GET
	@Path("/modifiy")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getALLSheetModifyDefault() {

		List<SheetPFEModification> list = IsheetPFE.getALLSheetModifyDefault();

		if (list.size() != 0) {

			return Response.status(Status.ACCEPTED).entity(list).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@GET
	@Path("/modifiy/{sheet_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getALLSheetModifyDefault(@PathParam(value = "sheet_id") int sheet_id) {

		if (IsheetPFE.getSheetModify(sheet_id) != null) {

			return Response.status(Status.ACCEPTED).entity(IsheetPFE.getSheetModify(sheet_id)).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@GET
	@Path("/notificationenseignant/{enseignant_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllNotificationByEnseignant(@PathParam(value = "enseignant_id") int enseignant_id) {

		List<PFENotification> list = IsheetPFE.getAllNotificationByEnseignant(enseignant_id);

		if (list.size() > 0) {

			return Response.status(Status.ACCEPTED).entity(list).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@GET
	@Path("/notificationetudiant/{etudiant_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllNotificationByEtudiant(@PathParam(value = "etudiant_id") int etudiant_id) {

		List<PFENotification> list = IsheetPFE.getAllNotificationByEtudiant(etudiant_id);

		if (list.size() > 0) {

			return Response.status(Status.ACCEPTED).entity(list).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@GET
	@Path("/export/{sheet_id}")
	@Produces("application/pdf")  
	public Response exportSheetPFE(@PathParam(value = "sheet_id") int sheet_id) {

		if ( IsheetPFE.exportSheetPFE(sheet_id) != null) {
			File file = new File(getClass().getClassLoader().getResource("PDF/"+IsheetPFE.exportSheetPFE(sheet_id)).getFile());
	        ResponseBuilder response = Response.ok((Object) file);  
	        response.header("Content-Disposition","attachment; filename=\"Sheet.pdf\"");  
	        return response.build();  
		}
	
		return Response.status(Status.BAD_REQUEST).build();

	}

	@POST
	@Path("/uploadfile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(MultipartFormDataInput file) {
		

		String fileName = "";

		Map<String, List<InputPart>> uploadForm = file.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("uploadedFile");

		for (InputPart inputPart : inputParts) {

			try {

				MultivaluedMap<String, String> header = inputPart.getHeaders();
				fileName = getFileName(header);
				
				InputStream inputStream = inputPart.getBody(InputStream.class, null);

				byte[] bytes = IOUtils.toByteArray(inputStream);

				fileName = "C:\\Users\\lhbya\\git\\4twin3-osp-pfe\\4twin3-osp-pfe-ejb\\src\\main\\resources\\XLS\\" + fileName;

				writeFile(bytes, fileName);

			

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		if(IsheetPFE.uploadExcel(fileName)) {
			
			return Response.status(Status.ACCEPTED).build();

		}else {
			return Response.status(Status.BAD_REQUEST).build();

		}
		

	}
	
	private String getFileName(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
		
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");
				
				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	private void writeFile(byte[] content, String filename) throws IOException {

		File file = new File(filename);

		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();

	}
	
	
	@GET
	@Path("/dashboard")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dashboard() {
		
		return Response.status(Status.ACCEPTED).entity(IsheetPFE.dashboard()).build();

	}
	
	@POST
	@Path("/addencadreurnote/{note}/{sheet_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addNoteEncadreur(@PathParam(value = "sheet_id") int sheet_id, @PathParam(value = "note") int note) {

		if (IsheetPFE.addNoteEncadreur(note,sheet_id))
			return Response.status(Status.ACCEPTED).build();

		return Response.status(Status.NOT_MODIFIED).build();

	}
	
	@POST
	@Path("/addrapporteurnote/{note}/{sheet_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addNoteRapporteur(@PathParam(value = "sheet_id") int sheet_id, @PathParam(value = "note") int note) {

		if (IsheetPFE.addNoteRapporteur(note,sheet_id))
			return Response.status(Status.ACCEPTED).build();

		return Response.status(Status.NOT_MODIFIED).build();

	}
	
	@GET
	@Path("/waitNote")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSheetPFEWaitNote() {

		List<SheetPFE> list = IsheetPFE.getAllSheetPFEWaitNote();
		if (list.size() > 0)
			return Response.status(Status.ACCEPTED).entity(list).build();

		return Response.status(Status.NOT_MODIFIED).build();

	}
	
	@GET
	@Path("/waitPlaning")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSheetPFEWaitPlaning() {

		List<SheetPFE> list = IsheetPFE.getAllSheetPFEWaitPlaning();
		if (list.size() > 0)
			return Response.status(Status.ACCEPTED).entity(list).build();

		return Response.status(Status.NOT_MODIFIED).build();

	}
	
	@POST
	@Path("/vu/{user_id}/{role}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSheetPFEWaitPlaning(@PathParam(value = "user_id") int user_id,  @PathParam(value = "role") String role) {

		if (IsheetPFE.changeVu(user_id, role))
			return Response.status(Status.ACCEPTED).build();

		return Response.status(Status.NOT_MODIFIED).build();

	}
	
	
	

}
