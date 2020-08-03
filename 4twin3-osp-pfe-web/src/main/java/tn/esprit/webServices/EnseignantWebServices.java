package tn.esprit.webServices;

import java.util.Set;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import rest.utilities.authentication.AuthenticationFilter;
import rest.utilities.authentication.Secure;
import tn.esprit.pfe.entities.Enseignant;
import tn.esprit.pfe.services.EnseignantService;
import utilities.ValidationError;

@Path("enseignant")
@RequestScoped
public class EnseignantWebServices {

	@EJB
	EnseignantService es;

	@Context
	private HttpHeaders headers;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "Admin" })
	@Path("{id}")
	public Response addEnseignant(Enseignant e, @PathParam(value = "id") int idSite) {
		AuthenticationFilter af = new AuthenticationFilter();
		Set<ValidationError> violations = es.addEnseignant(e, idSite, af.getIdUser(headers));
		if (violations == null) {
			return Response.status(Status.CREATED).entity("add successful").build();
		} else
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(violations).build();
	}

	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "Admin" })
	@GET
	public Response getListEnseignant() {
		AuthenticationFilter af = new AuthenticationFilter();
		Set<Enseignant> liste = es.getListEnseignant(af.getIdUser(headers));
		return Response.status(Status.OK).entity(liste).build();
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "Admin" })
	@Path("{id}")
	public Response supprimerEnseignant(@PathParam(value = "id") int idEnseignant) {
		AuthenticationFilter af = new AuthenticationFilter();
		Set<ValidationError> violations = es.supprimerEnseignant(idEnseignant, af.getIdUser(headers));
		if (violations == null) {
			return Response.status(Status.OK).entity("delete successful").build();
		} else
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(violations).build();
	}

	/*
	 * @PUT
	 * 
	 * @Consumes(MediaType.APPLICATION_JSON)
	 * 
	 * @Produces(MediaType.TEXT_PLAIN) public void updateReclamation(Reclamation
	 * rec) { rst.updateReclamation(rec);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * @DELETE
	 * 
	 * @Produces(MediaType.TEXT_PLAIN)
	 * 
	 * @Path("deleteRec/{idReclamation}") public void
	 * deleteClient(@QueryParam("idReclamation") int idRec) {
	 * rst.deleteReclamation(idRec); }
	 * 
	 * @GET
	 * 
	 * @Produces(MediaType.APPLICATION_JSON)
	 * 
	 * @Path("/getReclamationByEtudiant") public List<Reclamation>
	 * getReclamation(@QueryParam("etudiant_id") int ide){ return
	 * rst.getReclamationByEtudiant(ide); }
	 */

}
