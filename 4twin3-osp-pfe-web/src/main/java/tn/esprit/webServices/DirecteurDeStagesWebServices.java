package tn.esprit.webServices;

import java.util.Set;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import rest.utilities.authentication.AuthenticationFilter;
import rest.utilities.authentication.Secure;
import tn.esprit.pfe.services.DirecteurDeStagesService;
import utilities.ValidationError;

@Path("directeurDeStages")
@RequestScoped
public class DirecteurDeStagesWebServices {

	@EJB
	DirecteurDeStagesService ds;
	
	@Context
	private HttpHeaders headers;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	public Response affecterDirecteurDeStages(@QueryParam(value="idEnseignant") int idEnseignant, @QueryParam(value="idSite") int idSite) {
		AuthenticationFilter af=new AuthenticationFilter();
		Set<ValidationError> violations=ds.affecterDirecteurDeStage(idEnseignant, idSite, af.getIdUser(headers));
		if (violations==null) {
			return Response.status(Status.CREATED).entity("affectation successful").build();
		}
		else return Response.status(Status.INTERNAL_SERVER_ERROR).entity(violations).build();
	}
	
	

}
