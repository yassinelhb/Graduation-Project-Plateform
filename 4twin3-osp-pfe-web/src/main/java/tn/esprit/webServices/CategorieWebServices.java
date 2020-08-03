package tn.esprit.webServices;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import tn.esprit.pfe.entities.Categorie;
import tn.esprit.pfe.interfaces.CategorieServiceRemote;


@RequestScoped
@Path("Categorie")
public class CategorieWebServices {

	@EJB
	CategorieServiceRemote csr;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{ide}")
	public Response addCategorie(Categorie c,@PathParam("ide")int ide) {
		if(c!=null) {
		csr.addCategorie(c,ide);
		return Response.status(Status.OK).entity("ajout categorie avec succes").build();
		}
		return Response.status(Status.NOT_FOUND).entity("not found").build();
	}
	
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCat(@PathParam("id") int idcat) {
	 csr.deleteCategorie(idcat);
	 return Response.status(Status.OK).entity("categorie supprimer avec succes").build();
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllcategorie()
	{
		List<Categorie> c=csr.getAllcategorie();
		return Response.status(Status.ACCEPTED).entity(c).build();
	}
	@GET
	@Path("/{name}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response rendrecatcommemodule(@PathParam("name") String name) {
	 csr.addCategoriecommemodule(name);
	 return Response.status(Status.OK).entity("votre categorie a devenir module").build();
		
	}
	@GET
	@Path("/catname/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getcatbyname(@PathParam("name") String name )
	{
				
		return Response.ok(csr.getCategoriebyName(name)).build();
	}
	
	@GET
	@Path("/cat/{idens}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getcatens(@PathParam("idens") int idens )
	{
				
		return Response.ok(csr.getCategorielesplusdemandées(idens)).build();
	}
}
