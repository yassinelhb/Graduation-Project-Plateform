package tn.esprit.webServices;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
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

import tn.esprit.pfe.entities.Reclamation;
import tn.esprit.pfe.entities.Soutenance;
import tn.esprit.pfe.interfaces.SoutenanceServiceRemote;

@Path("/soutenance")
public class SoutenanceWebService {
	@EJB
	SoutenanceServiceRemote rst;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public void addReclamation(Soutenance s) {
		
		rst.addSoutenance(s);	
	}
	
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteSoutenance(@QueryParam(value="id")int id) {
		rst.deleteSoutenance(id);
	}
	
	@PUT
	@Path("/test/{ids}/{notee}/{note}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void addNoteT(@PathParam("ids") int ids , @PathParam("notee") float notee , @PathParam("note") float note)  {	
		rst.testNote(ids, notee, note);

	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getByTitre/{titre}")
	public List<Soutenance> getSoutenanceByTitre(@PathParam("titre") String titre ) {
		return rst.afficherSoutenanceSelonEtudiant(titre);

	}
	
	
	@Path("/note")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Soutenance>  SoutenanceNonNote() {
	
	   return rst.afficherSoutenanceNonNote();
		
	}
	

	
	
	@Path("/note/moyenne")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Float>  MoyenneNote() {
	
	   return rst.MoyenneNote();
		
	}
	
	
	
	
	
}
