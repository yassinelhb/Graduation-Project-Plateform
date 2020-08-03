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
import tn.esprit.pfe.entities.Classe;
import tn.esprit.pfe.services.ClasseService;
import utilities.ValidationError;

@Path("classe")
@RequestScoped
public class ClasseWebServices {

	@EJB
	ClasseService cs;
	
	@Context
	private HttpHeaders headers;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	@Path ("{id}")
	public Response addSpecialite(@PathParam(value="id") int idSpecialite) {
		//String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		AuthenticationFilter af=new AuthenticationFilter();
		Set<ValidationError> violations=cs.addClasse(idSpecialite, af.getIdUser(headers));
		if (violations==null) {
			return Response.status(Status.CREATED).entity("add successful").build();
		}
		else return Response.status(Status.INTERNAL_SERVER_ERROR).entity(violations).build();
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	@Path ("{id}")
	public Response supprimerClasse(@PathParam(value="id") int idClasse) {
		//String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		AuthenticationFilter af=new AuthenticationFilter();
		Set<ValidationError> violations=cs.supprimerClasse(idClasse, af.getIdUser(headers));
		if (violations==null) {
			return Response.status(Status.OK).entity("delete successful").build();
		}
		else return Response.status(Status.INTERNAL_SERVER_ERROR).entity(violations).build();
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	@Path ("{id}")
	public Response getClasse(@PathParam(value="id") int idClasse) {
		Classe classe=cs.getClasse(idClasse);
		if (classe!=null) {
			return Response.status(Status.OK).entity(classe).build();
		}
		else return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Cette classe n'existe pas ou vous n'etes pas autorisé à le consulter").build();
	}
	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	@GET
	@Path ("/list/{id}")
	public Response getListSpecialite(@PathParam(value="id") int idSpecialite) {
		Set<Classe> liste = cs.getListClasse(idSpecialite);
		return Response.status(Status.OK).entity(liste).build();
	}
	

}
