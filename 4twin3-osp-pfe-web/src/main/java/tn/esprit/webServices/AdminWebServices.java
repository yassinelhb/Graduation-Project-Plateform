package tn.esprit.webServices;

import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import rest.utilities.authentication.Secure;
import tn.esprit.pfe.entities.Admin;
import tn.esprit.pfe.services.AdminService;
import tn.esprit.pfe.services.UserService;
import utilities.ValidationError;

@Path("admin")
@RequestScoped
public class AdminWebServices {

	@EJB
	UserService us;

	@EJB
	AdminService as;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"SuperAdmin"})
	public Response addEnseignant(Admin e) {
		Set<ValidationError> violations=us.addUser(e);
		if (violations==null) {
			return Response.status(Status.CREATED).build();
		}
		else return Response.status(Status.INTERNAL_SERVER_ERROR).entity(violations).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role={"SuperAdmin"})
	public Response getListAdmin() {
		List<Admin> liste = as.getListAdmin();
		return Response.status(Status.ACCEPTED).entity(liste).build();
	}
	
	/*@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public void updateReclamation(Reclamation rec) {	
		rst.updateReclamation(rec);

	}
	
	
	
	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	@Path("deleteRec/{idReclamation}")
	public void deleteClient(@QueryParam("idReclamation") int idRec) {
	 rst.deleteReclamation(idRec);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getReclamationByEtudiant")
	public List<Reclamation> getReclamation(@QueryParam("etudiant_id") int ide){
		return rst.getReclamationByEtudiant(ide);
	}*/
	

}
