package tn.esprit.webServices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.RandomStringUtils;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import rest.utilities.authentication.AuthenticationFilter;
import rest.utilities.authentication.Secure;
import tn.esprit.pfe.entities.Classe;
import tn.esprit.pfe.entities.Departement;
import tn.esprit.pfe.entities.Ecole;
import tn.esprit.pfe.entities.Site;
import tn.esprit.pfe.entities.Specialite;
import tn.esprit.pfe.services.AdminService;
import tn.esprit.pfe.services.EcoleService;
import utilities.ValidationError;

@Path("ecole")
@RequestScoped
public class EcoleWebServices {

	@EJB
	EcoleService es;
	@EJB
	AdminService as;

	@Context
	private HttpHeaders headers;
	
	private static final String UPLOAD_FOLDER = "../uploadedFiles/";

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "Admin" })
	public Response addEcole(Ecole e) {
		// String authorizationHeader =
		// headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		AuthenticationFilter af = new AuthenticationFilter();
		Set<ValidationError> violations = es.addEcole(e, af.getIdUser(headers));
		if (violations == null) {
			return Response.status(Status.CREATED).entity(as.getAdmin(af.getIdUser(headers))).build();
		} else
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(violations).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "Admin" })
	@Path("{id}")
	public Response modifierEcole(Ecole e, @PathParam(value = "id") int idEcole) {
		// String authorizationHeader =
		// headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		AuthenticationFilter af = new AuthenticationFilter();
		Set<ValidationError> violations = es.modifierEcole(e, af.getIdUser(headers), idEcole);
		if (violations == null) {
			return Response.status(Status.OK).entity(as.getAdmin(af.getIdUser(headers))).build();
		} else
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(violations).build();
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "Admin" })
	@Path("{id}")
	public Response supprimerEcole(@PathParam(value = "id") int idEcole) {
		// String authorizationHeader =
		// headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		AuthenticationFilter af = new AuthenticationFilter();
		Set<ValidationError> violations = es.supprimerEcole(idEcole, af.getIdUser(headers));
		if (violations == null) {
			return Response.status(Status.OK).entity("delete successful").build();
		} else
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(violations).build();
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "Admin" })
	@Path("{id}")
	public Response getMyEcole(@PathParam(value = "id") int idEcole) {
		// String authorizationHeader =
		// headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		AuthenticationFilter af = new AuthenticationFilter();
		Ecole ecole = es.getEcole(idEcole, af.getIdUser(headers));
		if (ecole != null) {
			return Response.status(Status.OK).entity(ecole).build();
		} else
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Cette ecole n'existe pas ou vous n'etes pas autorisé à la consulter").build();
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "Admin" })
	@Path("statistiques/{id}")
	public Response getMyEcoleStats(@PathParam(value = "id") int idEcole) {
		// String authorizationHeader =
		// headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		AuthenticationFilter af = new AuthenticationFilter();
		Map<String, Object> response = new HashMap<>();
		int nbDepartements=0;
		int nbSpecialites=0;
		int nbClasses=0;
		int nbEtudiants=0;
		Ecole ecole = es.getEcole(idEcole, af.getIdUser(headers));
		if (ecole != null) {
			response.put("Nombre d'enseignants", ecole.getEnseignants().size());
			response.put("Nombre de sites", ecole.getSites().size());
			for (Site s:ecole.getSites()) {
				nbDepartements+=s.getDepartements().size();
				for (Departement d:s.getDepartements()) {
					nbSpecialites+=d.getSpecialites().size();
					for (Specialite sp:d.getSpecialites()) {
						nbClasses+=sp.getClasses().size();
						for (Classe c:sp.getClasses()) {
							nbEtudiants+=c.getEtudiants().size();
						}
					}
				}
			}
			response.put("Nombre de départements", nbDepartements);
			response.put("Nombre de Spécialités", nbSpecialites);
			response.put("Nombre de Classes", nbClasses);
			response.put("Nombre d'étudiants", nbEtudiants);
			return Response.status(Status.OK).entity(response).build();
		} else
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Cette ecole n'existe pas ou vous n'etes pas autorisé à la consulter").build();
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Secure(role = { "Admin" })
	@Path("image/{id}")
	public Response getMyEcoleImage(@PathParam(value = "id") int idEcole) {
		// String authorizationHeader =
		// headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		AuthenticationFilter af = new AuthenticationFilter();
		Ecole ecole = es.getEcole(idEcole, af.getIdUser(headers));
		if (ecole != null) {
			if (ecole.getLogo() != null) {
				File file = new File (ecole.getLogo());
				ResponseBuilder rb = Response.ok((Object) file);
				return rb.status(Status.OK).header("Content-Disposition",
						"attachment;filename="+file.getName()).build();
			}
			return Response.status(Status.OK).entity("Cette école n'a pas de logo").build();
		} else
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Cette ecole n'existe pas ou vous n'etes pas autorisé à la consulter").build();
	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "Admin", "SuperAdmin" })
	@GET
	public Response getListEcole() {
		List<Ecole> liste = es.getListEcole();
		return Response.status(Status.OK).entity(liste).build();
	}

	@POST
	@Secure(role = { "Admin" })
	@Consumes("multipart/form-data")
	@Path("image")
	public Response addImage(MultipartFormDataInput form) throws IOException {
		AuthenticationFilter af = new AuthenticationFilter();
		String uid = RandomStringUtils.random(8, false, true);
		InputStream uploadedInputStream = form.getFormDataPart("image", InputStream.class, null);
		if (uploadedInputStream == null)
			return Response.status(400).entity("Invalid form data").build();
		try {
			createFolderIfNotExists(UPLOAD_FOLDER);
		} catch (SecurityException se) {
			return Response.status(500).entity("Can not create destination folder on server").build();
		}
		String uploadedFileLocation = UPLOAD_FOLDER + uid + ".jpg";
		try {
			saveToFile(uploadedInputStream, uploadedFileLocation);
		} catch (IOException e) {
			return Response.status(500).entity("Can not save file").build();
		}
		es.addImage(uploadedFileLocation, af.getIdUser(headers));
		return Response.status(200).entity("Image added successfully").build();
	}
	
	
	private void saveToFile(InputStream inStream, String target) throws IOException {
		OutputStream out = null;
		int read = 0;
		byte[] bytes = new byte[1024];
		out = new FileOutputStream(new File(target));
		while ((read = inStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
	}

	private void createFolderIfNotExists(String dirName) throws SecurityException {
		File theDir = new File(dirName);
		if (!theDir.exists()) {
			theDir.mkdir();
		}
	}

}
