package tn.esprit.webServices;

import java.io.File;
import java.util.List;

import javax.ejb.EJB;
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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import rest.utilities.authentication.AuthenticationFilter;
import tn.esprit.pfe.entities.InternshipAgreemen;
import tn.esprit.pfe.interfaces.InternshipAgreemenRemote;

@Path("agreemen")
public class InternshipAgreemenWebServices {

	@EJB
	InternshipAgreemenRemote Iagreemen;

	@Context
	private HttpHeaders headers;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAgreemen(InternshipAgreemen internshipAgreemen) {

		int id = Iagreemen.addInternshipAgreemen(internshipAgreemen);

		return Response.status(Status.CREATED).entity(Iagreemen.getAgreemenById(id)).build();

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllAgreemen() {

		List<InternshipAgreemen> listInternshipAgreemens = Iagreemen.getAllAgreemen();

		if (listInternshipAgreemens.size() != 0) {

			return Response.status(Status.ACCEPTED).entity(listInternshipAgreemens).build();
		}

		return Response.status(Status.NO_CONTENT).build();

	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAgreemenById(@PathParam(value = "id") int id) {

		if (Iagreemen.getAgreemenById(id) != null)
			return Response.status(Status.ACCEPTED).entity(Iagreemen.getAgreemenById(id)).build();

		return Response.status(Status.NO_CONTENT).build();
	}

	@GET
	@Path("/etudiant/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAgreemenByEtudiant(@PathParam(value = "id") int id) {

		if (Iagreemen.getAgreemenByEtudiant(id) != null)
			return Response.status(Status.ACCEPTED).entity(Iagreemen.getAgreemenByEtudiant(id))
					.build();

		return Response.status(Status.NO_CONTENT).build();

	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateInternshipAgreemen(InternshipAgreemen internshipAgreemen) {

		if (Iagreemen.updateInternshipAgreemen(internshipAgreemen))
			return Response.status(Status.ACCEPTED).entity(internshipAgreemen).build();

		return Response.status(Status.NOT_MODIFIED).build();

	}

	@DELETE
	@Path("/{id}")
	public Response removeInternshipAgreemen(@PathParam(value = "id") int id) {

		if (Iagreemen.removeInternshipAgreemen(id))
			return Response.status(Status.ACCEPTED).build();

		return Response.status(Status.BAD_REQUEST).build();
	}

	@GET
	@Path("/filter/{email}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchInternshipAgreemen(@PathParam(value = "email") String email) {

		List<InternshipAgreemen> list = Iagreemen.searchInternshipAgreemen(email);
		if (list.size() > 0)
			return Response.status(Status.ACCEPTED).entity(list).build();

		return Response.status(Status.NO_CONTENT).build();
	}

	@GET
	@Path("/export/{id}")
	@Produces("application/pdf")  
	public Response exportInternshipAgreemen(@PathParam(value = "id") int id) {
		
		if ( Iagreemen.exportPDE(id) != null) {
				File file = new File(getClass().getClassLoader().getResource("PDF/"+Iagreemen.exportPDE(id)).getFile());
		        ResponseBuilder response = Response.ok((Object) file);  
		        response.header("Content-Disposition","attachment; filename=\"Internship.pdf\"");  
		        return response.build();  
		}
		
		return Response.status(Status.BAD_REQUEST).build();
	}

}
