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
import tn.esprit.pfe.entities.Site;
import tn.esprit.pfe.services.SiteService;
import utilities.ValidationError;

@Path("site")
@RequestScoped
public class SiteWebServices {

	@EJB
	SiteService ss;
	
	@Context
	private HttpHeaders headers;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	public Response addSite(Site s) {
		//String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		AuthenticationFilter af=new AuthenticationFilter();
		Set<ValidationError> violations=ss.addSite(s, af.getIdUser(headers));
		if (violations==null) {
			return Response.status(Status.CREATED).build();
		}
		else return Response.status(Status.INTERNAL_SERVER_ERROR).entity(violations).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"DirecteurDesStages"})
	public Response modifierSiteDirecteurDesStages(Site s) {
		//String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		AuthenticationFilter af=new AuthenticationFilter();
		Set<ValidationError> violations=ss.modifierSiteDirecteurDesStages(s, af.getIdUser(headers));
		if (violations==null) {
			return Response.status(Status.OK).build();
		}
		else return Response.status(Status.INTERNAL_SERVER_ERROR).entity(violations).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	@Path ("{id}")
	public Response modifierSite(Site s, @PathParam(value="id") int idSite) {
		//String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		AuthenticationFilter af=new AuthenticationFilter();
		Set<ValidationError> violations=ss.modifierSite(s, af.getIdUser(headers), idSite);
		if (violations==null) {
			return Response.status(Status.OK).build();
		}
		else return Response.status(Status.INTERNAL_SERVER_ERROR).entity(violations).build();
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	@Path ("{id}")
	public Response supprimerSite(@PathParam(value="id") int idSite) {
		//String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		AuthenticationFilter af=new AuthenticationFilter();
		Set<ValidationError> violations=ss.supprimerSite(idSite, af.getIdUser(headers));
		if (violations==null) {
			return Response.status(Status.OK).build();
		}
		else return Response.status(Status.INTERNAL_SERVER_ERROR).entity(violations).build();
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin","DirecteurDesStages"})
	@Path ("{id}")
	public Response getSite(@PathParam(value="id") int idSite) {
		Site site=ss.getSite(idSite);
		if (site!=null) {
			return Response.status(Status.OK).entity(site).build();
		}
		else return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Ce site n'existe pas ou vous n'etes pas autorisé à le consulter").build();
	}
	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	@GET
	@Path ("/list/{id}")
	public Response getListSite(@PathParam(value="id") int idEcole) {
		Set<Site> liste = ss.getListSite(idEcole);
		return Response.status(Status.OK).entity(liste).build();
	}
	

}
