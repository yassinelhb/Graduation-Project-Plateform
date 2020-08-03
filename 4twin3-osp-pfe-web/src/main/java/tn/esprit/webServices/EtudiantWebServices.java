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
import tn.esprit.pfe.entities.Etudiant;
import tn.esprit.pfe.services.EtudiantService;
import utilities.ValidationError;

@Path("etudiant")
@RequestScoped
public class EtudiantWebServices {

	@EJB
	EtudiantService es;
	
	@Context
	private HttpHeaders headers;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	@Path ("{id}")
	public Response addEtudiant(Etudiant e,@PathParam(value="id") int idClasse) {
		//String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		AuthenticationFilter af=new AuthenticationFilter();
		Set<ValidationError> violations=es.addEtudiant(e, idClasse, af.getIdUser(headers));
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
	public Response modifierEtudiant(Etudiant e, @PathParam(value="id") int idEtudiant) {
		AuthenticationFilter af=new AuthenticationFilter();
		Set<ValidationError> violations=es.modifierEtudiant(e, idEtudiant, af.getIdUser(headers));
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
	public Response supprimerEtudiant(@PathParam(value="id") int idEtudiant) {
		//String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		AuthenticationFilter af=new AuthenticationFilter();
		Set<ValidationError> violations=es.supprimerEtudiant(idEtudiant, af.getIdUser(headers));
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
	public Response getEtudiant(@PathParam(value="id") int idEtudiant) {
		Etudiant e = es.getEtudiant(idEtudiant);
		if (e!=null) {
			return Response.status(Status.OK).entity(e).build();
		}
		else return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Cet étudiant n'existe pas ou vous n'etes pas autorisé à le consulter").build();
	}
	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	@GET
	@Path ("/listDepartement/{id}")
	public Response getListEtudiantParDepartement(@PathParam(value="id") int idDepartement) {
		AuthenticationFilter af=new AuthenticationFilter();
		Set<Etudiant> liste = es.getListEtudiantParDepartement(idDepartement,af.getIdUser(headers));
		return Response.status(Status.OK).entity(liste).build();
	}
	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	@GET
	@Path ("/listSite/{id}")
	public Response getListEtudiantParSite(@PathParam(value="id") int idSite) {
		AuthenticationFilter af=new AuthenticationFilter();
		Set<Etudiant> liste = es.getListEtudiantParSite(idSite,af.getIdUser(headers));
		return Response.status(Status.OK).entity(liste).build();
	}
	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	@GET
	@Path ("/listEcole")
	public Response getListEtudiantParEcole() {
		AuthenticationFilter af=new AuthenticationFilter();
		Set<Etudiant> liste = es.getListEtudiantParEcole(af.getIdUser(headers));
		return Response.status(Status.OK).entity(liste).build();
	}
	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	@GET
	@Path ("/listClasse/{id}")
	public Response getListEtudiantParClasse(@PathParam(value="id") int idClasse) {
		AuthenticationFilter af=new AuthenticationFilter();
		Set<Etudiant> liste = es.getListEtudiantParClasse(idClasse,af.getIdUser(headers));
		return Response.status(Status.OK).entity(liste).build();
	}
	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"Admin"})
	@GET
	@Path ("/listSpecialite/{id}")
	public Response getListEtudiantParSpecialite(@PathParam(value="id") int idSpecialite) {
		AuthenticationFilter af=new AuthenticationFilter();
		Set<Etudiant> liste = es.getListEtudiantParSpecialite(idSpecialite,af.getIdUser(headers));
		return Response.status(Status.OK).entity(liste).build();
	}
	

}
