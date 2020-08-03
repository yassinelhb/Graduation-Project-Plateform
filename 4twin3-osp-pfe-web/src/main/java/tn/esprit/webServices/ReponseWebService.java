package tn.esprit.webServices;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
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

import tn.esprit.pfe.entities.ForumQuestion;
import tn.esprit.pfe.entities.ForumReponse;
import tn.esprit.pfe.interfaces.ReponseServiceRemote;

@Path("Reponse")
public class ReponseWebService {
	@EJB
	ReponseServiceRemote rsr;
	List<ForumReponse> r=new ArrayList<ForumReponse>();
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{idq}/{ide}")
	public Response addReponse(ForumReponse R,@PathParam("idq")int idq,@PathParam("ide")int ide) {
		rsr.addReponse(R,idq,ide);
		return Response.status(Status.OK).entity("add successful").build();
		
	}
	@GET
	@Path("/notifier/{idq}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response notifier(@PathParam("idq")int idq) {
		int id=9;
		List<String> ls=rsr.notifier(idq);
		return Response.status(Status.ACCEPTED).entity(ls).build();
		
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/update/{idq}")
	public Response Mettreajourquestion(ForumReponse R,@PathParam(value="idq")int idq) {
		
		rsr.updaterep(R,idq);
		return Response.status(Status.OK).entity("votre contenu est modifier").build();}
			
	/*
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/update/{id_Reponse}")
	public Response Mettreajourquestion(@PathParam("id_Reponse") int idreponse,ForumReponse R) {
		//rsr.MettreajourReponse(idreponse,R);
		//return Response.status(Status.OK).build();
		for (ForumReponse rep : r) {
			if( rep.getId_Reponse() ==idreponse){
				rsr.MettreajourReponse(idreponse,R);
			return Response.status(Status.OK).entity("votre conetnu modiifier").build();
			
		}}
		return Response.status(Status.NOT_FOUND).entity("Not Found !").build();
			
	}*/
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/nombrerep/{id_Question}")
	public Response gettnombrerepquestion(@PathParam("id_Question")int idq) {
		rsr.getNombreReponseJPQL(idq);
		return Response.status(Status.OK).build();
		
	}
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Path("/all/{id_Question}")
	public Response getAllreponse(@PathParam("id_Question")int idq)
	{
		List<ForumReponse> q=rsr.getAllReponse(idq);
		return Response.status(Status.ACCEPTED).entity(q).build();
	}
	
	
	

	

}
