package tn.esprit.webServices;

import java.util.List;
import java.util.Set;

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
import javax.ws.rs.core.Response.Status;
import tn.esprit.pfe.entities.ForumQuestion;
import tn.esprit.pfe.interfaces.QuestionServiceRemote;

@Path("Question")
public class QuestionWebService {
	@EJB
	QuestionServiceRemote qsr;
	@Context
	private HttpHeaders headers;
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{ide}")
	public Response addQuestion(ForumQuestion Q,@PathParam("ide")int ide) {
		if(Q!=null) {
		qsr.addQuestion(Q,ide);
		return Response.status(Status.ACCEPTED).entity("ajout question avec succes").build();
	}
		return Response.status(Status.ACCEPTED).entity("Erreur").build();}
	
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response getAllquestion()
	{
		List<ForumQuestion> q=qsr.getAllquestion();
		return Response.status(Status.ACCEPTED).entity(q).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getqid/{id_Question}")
	public Response getAllquestionid(@PathParam("id_Question")int idq)
	{
		ForumQuestion q=qsr.getQuestionid(idq);
		return Response.status(Status.ACCEPTED).entity(q).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/update/{id_Question}")
	public Response Mettreajourquestion(@PathParam("id_Question") int idquestion) {
		if(idquestion!=0) {
		qsr.MetreajourQuestion( idquestion);
		return Response.status(Status.ACCEPTED).entity("votre question et ajour").build();}
		
		return Response.status(Status.ACCEPTED).entity("verifier id question").build();
		
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response deletequestion(@PathParam("id") int idquestion) {
	 if(idquestion!=0) {
		qsr.deleteQuestion(idquestion);	
	return Response.status(Status.OK).entity("votre question a été supprimer").build();
	}
	return Response.status(Status.OK).entity("verifier id question").build();
	}

}
