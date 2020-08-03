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
import tn.esprit.pfe.entities.Departement;
import tn.esprit.pfe.services.DepartementService;
import utilities.ValidationError;

@Path("departement")
@RequestScoped
public class DepartementWebServices {

	@EJB
	DepartementService ds;
	
	@Context
	private HttpHeaders headers;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	@Path ("{id}")
	public Response addDepartement(Departement d,@PathParam(value="id") int idSite) {
		//String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		AuthenticationFilter af=new AuthenticationFilter();
		Set<ValidationError> violations=ds.addDepartement(d, idSite, af.getIdUser(headers));
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
	public Response modifierDepartement(Departement d, @PathParam(value="id") int idDepartement) {
		//String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		AuthenticationFilter af=new AuthenticationFilter();
		Set<ValidationError> violations=ds.modifierDepartement(d, idDepartement, af.getIdUser(headers));
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
	public Response supprimerDepartement(@PathParam(value="id") int idDepartement) {
		//String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		AuthenticationFilter af=new AuthenticationFilter();
		Set<ValidationError> violations=ds.supprimerDepartement(idDepartement, af.getIdUser(headers));
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
	public Response getDepartement(@PathParam(value="id") int idDepartement) {
		Departement departement=ds.getDepartement(idDepartement);
		if (departement!=null) {
			return Response.status(Status.OK).entity(departement).build();
		}
		else return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Ce département n'existe pas ou vous n'etes pas autorisé à le consulter").build();
	}
	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	@GET
	@Path ("/list/{id}")
	public Response getListDepartement(@PathParam(value="id") int idSite) {
		Set<Departement> liste = ds.getListDepartement(idSite);
		return Response.status(Status.OK).entity(liste).build();
	}
	

}
