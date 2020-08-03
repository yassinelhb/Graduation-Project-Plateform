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
import tn.esprit.pfe.services.ChefDeDepartementService;
import utilities.ValidationError;

@Path("chefDeDepartement")
@RequestScoped
public class ChefDeDepartementWebServices {

	@EJB
	ChefDeDepartementService cs;
	
	@Context
	private HttpHeaders headers;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"DirecteurDesStages"})
	public Response affecterDirecteurDeStages(@QueryParam(value="idEnseignant") int idEnseignant, @QueryParam(value="idDepartement") int idDepartement) {
		AuthenticationFilter af=new AuthenticationFilter();
		Set<ValidationError> violations=cs.affecterChefDeDepartement(idEnseignant, idDepartement, af.getIdUser(headers));
		if (violations==null) {
			return Response.status(Status.CREATED).entity("affectation successful").build();
		}
		else return Response.status(Status.INTERNAL_SERVER_ERROR).entity(violations).build();
	}
	
	

}
