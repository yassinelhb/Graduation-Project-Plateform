package tn.esprit.webServices;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.pfe.entities.SheetPFEModification;
import tn.esprit.pfe.interfaces.SheetPFEModificationRemote;

@Path("modifysheet")
public class SheetPFEModificationWebServices {


	@EJB
	SheetPFEModificationRemote IsheetPFEModify;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSheetPFEModifications() {

		List<SheetPFEModification> listSheetPFE = IsheetPFEModify.getAllRefuseSheetPFEModifications();

		if (listSheetPFE.size() != 0) {

			return Response.status(Status.ACCEPTED).entity(listSheetPFE).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSheetPFEModification(@PathParam(value="id") int id) {
		
		if(IsheetPFEModify.getSheetPFEModification(id) != null)
			  return Response.status(Status.ACCEPTED).entity(IsheetPFEModify.getSheetPFEModification(id)).build();
		
		return  Response.status(Status.NOT_FOUND).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response validateSheetPFEModification(SheetPFEModification sheetPFEModification) {	
		
		if(IsheetPFEModify.validateSheetPFEModification(sheetPFEModification))
	    	return Response.status(Status.ACCEPTED).entity(sheetPFEModification).build();
		
    	return Response.status(Status.NOT_MODIFIED).build();

	
	}
	
	@DELETE
	@Path("/{id}")
    public Response removeInternshipAgreemen(@PathParam(value="id") int id) {	
		
		if(IsheetPFEModify.removeSheetPFEModification(id))
	    	return Response.status(Status.ACCEPTED).entity("success").build();
		
		return Response.status(Status.BAD_REQUEST).build();
	}
	
	
}
