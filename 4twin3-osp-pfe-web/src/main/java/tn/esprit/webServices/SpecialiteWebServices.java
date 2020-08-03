package tn.esprit.webServices;

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
import javax.ws.rs.core.Response.Status;

import rest.utilities.authentication.AuthenticationFilter;
import rest.utilities.authentication.Secure;
import tn.esprit.pfe.entities.Specialite;
import tn.esprit.pfe.services.SpecialiteService;
import utilities.ValidationError;

@Path("specialite")
@RequestScoped
public class SpecialiteWebServices {

	@EJB
	SpecialiteService ss;
	
	@Context
	private HttpHeaders headers;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	@Path ("{id}")
	public Response addSpecialite(Specialite s,@PathParam(value="id") int idDepartement) {
		//String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		AuthenticationFilter af=new AuthenticationFilter();
		Set<ValidationError> violations=ss.addSpecialite(s, idDepartement, af.getIdUser(headers));
		if (violations==null) {
			return Response.status(Status.CREATED).entity("add successful").build();
		}
		else return Response.status(Status.INTERNAL_SERVER_ERROR).entity(violations).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	@Path ("{id}")
	public Response modifierSpecialite(Specialite s, @PathParam(value="id") int idSpecialite) {
		//String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		AuthenticationFilter af=new AuthenticationFilter();
		Set<ValidationError> violations=ss.modifierSpecialite(s, idSpecialite, af.getIdUser(headers));
		if (violations==null) {
			return Response.status(Status.OK).entity("modify successful").build();
		}
		else return Response.status(Status.INTERNAL_SERVER_ERROR).entity(violations).build();
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	@Path ("{id}")
	public Response supprimerSpecialite(@PathParam(value="id") int idSpecialite) {
		//String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		AuthenticationFilter af=new AuthenticationFilter();
		Set<ValidationError> violations=ss.supprimerSpecialite(idSpecialite, af.getIdUser(headers));
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
	public Response getSpecialite(@PathParam(value="id") int idSpecialite) {
		Specialite specialite=ss.getSpecialite(idSpecialite);
		if (specialite!=null) {
			return Response.status(Status.OK).entity(specialite).build();
		}
		else return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Cette Spécialité n'existe pas ou vous n'etes pas autorisé à le consulter").build();
	}
	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	@GET
	@Path ("/list/{id}")
	public Response getListSpecialite(@PathParam(value="id") int idDepartement) {
		Set<Specialite> liste = ss.getListSpecialite(idDepartement);
		return Response.status(Status.OK).entity(liste).build();
	}
	

}
