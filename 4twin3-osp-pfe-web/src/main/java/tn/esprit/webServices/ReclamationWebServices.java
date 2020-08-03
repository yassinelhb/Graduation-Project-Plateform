package tn.esprit.webServices;

import java.util.List;


import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.pfe.entities.Notifications;
import tn.esprit.pfe.entities.Reclamation;
import tn.esprit.pfe.entities.User;
import tn.esprit.pfe.interfaces.NotificationsServiceRemote;
import tn.esprit.pfe.interfaces.ReclamationServiceRemote;

@Path("/reclamation")
@ManagedBean
public class ReclamationWebServices {

	

	
	 @EJB(beanName="ReclamationServices")
	ReclamationServiceRemote rst;
	
 	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addReclamation(Reclamation rec) {
		rst.addReclamation(rec);	
		return Response.status(Status.CREATED).entity(rec).build();
	} 
	

	
	
	
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void updateReclamation(Reclamation rec ) {	
		rst.updateReclamation(rec);

	}
	
	
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{idReclamation}")
	public void deleteReclamation(@PathParam("idReclamation") int idReclamation) {
		try {
			
	 rst.deleteReclamation(idReclamation);}
		catch(Exception ex) {
			System.out.println(ex);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	
	public List<Reclamation> getReclamation(){
		return rst.getAllReclamation();
	}
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getById/{nom}/{prenom}")
	public List<Reclamation> getReclamationByEtudiant(@PathParam("nom") String nom , @PathParam("prenom") String prenom) {
		return rst.getReclamationByEtudiant(nom,prenom);

	}
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getBySoutenance/{id}")
	public List<Reclamation> getReclamationBySoutenance(@PathParam("id") int id ) {
		return rst.getReclamationBySoutenance(id);

	}
	
	@Path("/nombreReclamation")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Float> nombreDeReclamation() {
	
	   return rst.nombreDeReclamationSelonDateAjout();
		
	}
	
	@Path("/nombreReclamationM")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object[]> nombreDeReclamationParMois() {
	
	   return rst.nombreDeReclamationParMois();
		
	}
	
	
		
	
}
