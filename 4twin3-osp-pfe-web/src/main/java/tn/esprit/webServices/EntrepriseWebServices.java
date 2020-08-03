package tn.esprit.webServices;

import java.util.Set;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import rest.utilities.authentication.AuthenticationFilter;
import rest.utilities.authentication.Secure;
import tn.esprit.pfe.email.JavaMail;
import tn.esprit.pfe.entities.Admin;
import tn.esprit.pfe.entities.Entreprise;
import tn.esprit.pfe.entities.EntrepriseSupervisor;
import tn.esprit.pfe.entities.InternshipCataloge;
import tn.esprit.pfe.entities.InternshipOffer;
import tn.esprit.pfe.entities.JobOffer;
import tn.esprit.pfe.entities.ResponsableEntreprise;
import tn.esprit.pfe.services.EntrepriseServices;
import tn.esprit.pfe.services.UserService;
import utilities.ValidationError;

@RequestScoped
@Path("Entreprises")
public class EntrepriseWebServices {
	@Inject
	EntrepriseServices es;
	
	@EJB
	UserService us;

	@Context
	private HttpHeaders headers;

	/* Entreprise */

	@GET
	@Path("updatepack/{id}/{idp}")
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response getupdatePack(@PathParam("id") int id,@PathParam("idp") int idp)
	{
		return Response.status(Status.ACCEPTED).entity(es.updatepack(id, idp)).build();
	}
	
	@GET
	@Path("packdetails/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response getPackdetails(@PathParam("id") int id)
	{
		return Response.status(Status.ACCEPTED).entity(es.getpacksDetails(id)).build();
	}
	
	@GET
	@Path("packdetails")
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response getallPack()
	{
		return Response.status(Status.ACCEPTED).entity(es.getallpacks()).build();
	}
	
	

	@POST
	@Path("addEntreprise")
	@Consumes(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response RegistreEntreprise(Entreprise ent) throws MessagingException {
		AuthenticationFilter af = new AuthenticationFilter();
		int idEnt = es.addEntreprise(ent,af.getIdUser(headers));
		String email = es.Email(af.getIdUser(headers));
		es.addEntreprisetoResponsable(af.getIdUser(headers), idEnt);
		if(idEnt != 0)
		{
			JavaMail.sendConfirmationAcount(email);
			return Response.status(Status.CREATED).entity("Registeration Successful").build();
		}
		return Response.status(Status.NOT_ACCEPTABLE).entity("Registration Failed ").build();
	}
	
	
	@POST
	@Path("responsable")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addResponsable(ResponsableEntreprise re) {
		Set<ValidationError> violations=us.addUser(re);
		if (violations==null) {
			return Response.status(Status.CREATED).entity("add successful").build();
		}
		else return Response.status(Status.INTERNAL_SERVER_ERROR).entity(violations).build();
	}
	

	@PUT
	@Path("updateEntreprise")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response updateEntreprise(Entreprise ent) {
		es.updateEntreprise(ent);
		return Response.status(Status.ACCEPTED).entity("Successful Update").build();
	}
	

	@GET
	@Path("Detail/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response getEntrepriseDetails(@PathParam("id") int id)
	{
		return Response.status(Status.ACCEPTED).entity(es.getEntrepriseDetails(id)).build();
	}
	

	
	@DELETE
	@Path("Delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response RemoveEntreprise(@PathParam("id") int id)
	{	
		es.deleteEntreprise(id);
	    return Response.status(Status.ACCEPTED).entity("Entreprise Deleted").build();
	}
	
	
	@GET
	@Path("All")
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response getAllentreprise()
	{
		return Response.status(Status.ACCEPTED).entity(es.getallEntreprises()).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEntreprises()
	{
		return Response.status(Status.ACCEPTED).entity(es.getallEntreprises()).build();
	}
	
	@GET
	@Path("StatEntreprise")
	@Secure(role = { "ResponsableEntreprise" })
	public Response gatStatEntreprise( @QueryParam("id") int idEnt)
	{	Long A=es.nbrOffredestage(idEnt);
		Long B=es.nbrOffredetr(idEnt);
		Long C=es.nbrcatalog(idEnt);
		Long D=es.nbrsupervisor(idEnt);
		String S = "Number of Your internship Offer = "+A+" Number of Your JobOffer= "+B+"Number of Your InternshipCataloge= "+C+"Number of Your Supervisor=" +D;
		return Response.status(Status.ACCEPTED).entity(S).build();
	}
	
	/* Internship */
	
	@GET
	@Path("AllIntership/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response getIntershipByEntreprise(@PathParam("id") int id)
	{
		return Response.status(Status.ACCEPTED).entity(es.getAllIntershipOfferByEntreprise(id)).build();
	}
	
	@GET
	@Path("AllIntershipToday")
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response getIntershipToday()
	{
		return Response.status(Status.ACCEPTED).entity(es.getAllIntershipOfferToday()).build();
	}
	
	@POST
	@Path("addInternshipOffer/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response addInternshipOffer(@PathParam("id") int idEnt,InternshipOffer inoff) {
		int idioff = es.addInternshipOffer(inoff);
		es.addInternshipOffertoEntreprise(idEnt, idioff);
		if(idioff != 0)
		{
			return Response.status(Status.CREATED).entity("Internship added Successful").build();
		}
		return Response.status(Status.NOT_ACCEPTABLE).entity("Internship added Failed ").build();
	}
	
	@GET
	@Path("internshipOfferDetail/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response getInternshipOffer(@PathParam("id") int idioff)
	{
		return Response.status(Status.ACCEPTED).entity(es.getInternshipOfferDetails(idioff)).build();
	}
	
	@PUT
	@Path("updateinternshipOffer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response updateinternshipOffer(InternshipOffer inoff) {
		es.updateInternshipOffer(inoff);
		return Response.status(Status.ACCEPTED).entity("Successful internshipOffer Update").build();
	}
	
	@DELETE
	@Path("DeleteinternshipOffer/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response RemoveInternshipoffer(@PathParam("id") int inoff)
	{	
		es.deleteInternshipOffer(inoff);
	    return Response.status(Status.ACCEPTED).entity("InternshipOffer Deleted").build();
	}
	
	/* Supervisor */
	
	@POST
	@Path("addSupervisor/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response addSupervisor(@PathParam("id") int idEnt,EntrepriseSupervisor sup) {
		
		int idsup = es.addSupervisor(sup);
		es.addSupervisortoEntreprise(idEnt, idsup);
		if(idsup != 0)
		{
			return Response.status(Status.CREATED).entity("Supervisor added Successful").build();
		}
		return Response.status(Status.NOT_ACCEPTABLE).entity("Supervisor added Failed ").build();
	}
	
	@PUT
	@Path("updateSupervisor")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response updateSupervisor(EntrepriseSupervisor sup) {
		es.updateSupervisor(sup);;
		return Response.status(Status.ACCEPTED).entity("Successful Supervisor Update").build();
	}
	
	@DELETE
	@Path("DeleteSupervisor/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response RemoveSupervisor(@PathParam("id") int idsup)
	{	
		es.deleteSupervisor(idsup);
	    return Response.status(Status.ACCEPTED).entity("Supervisor Deleted").build();
	}
	
	@GET
	@Path("SupervisorDetail/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response getSupervisorDetail(@PathParam("id") int idsup)
	{
		return Response.status(Status.ACCEPTED).entity(es.getEntrepriseSupervisor(idsup)).build();
	}

	@GET
	@Path("AllEntrepriseSupervisor/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response getAllEntrepriseSupervisorByEntreprise(@PathParam("id") int id)
	{
		return Response.status(Status.ACCEPTED).entity(es.getAllEntrepriseSupervisorByEntreprise(id)).build();
	}
	
	/* JobOffre */
	
	@POST
	@Path("addJobOffer/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response addJobOffre(@PathParam("id") int idEnt,JobOffer jo) {
		int idjo = es.addJobOffre(jo);
		es.addJobOffretoEntreprise(idEnt, idjo);
		
		if(idjo != 0 )
		{
			return Response.status(Status.CREATED).entity("JobOffer added Successful").build();
			
		}
		return Response.status(Status.NOT_ACCEPTABLE).entity("JobOffer added Failed ").build();
	}

	@GET
	@Path("JobOfferDetail/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response getJobOfferDetail(@PathParam("id") int idJo)
	{
		return Response.status(Status.ACCEPTED).entity(es.getJobOfferDetails(idJo)).build();
	}
	
	@PUT
	@Path("updateJobOffer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response updateJobOffer(JobOffer jo) {
		es.updateJobOffre(jo);
		return Response.status(Status.ACCEPTED).entity("Successful JobOffer Update").build();
	}

	@DELETE
	@Path("DeleteJobOffre/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response RemoveJobOffre(@PathParam("id") int idjo)
	{	
		es.deleteJobOffre(idjo);
	    return Response.status(Status.ACCEPTED).entity("JobOffre Deleted").build();
	}

	@GET
	@Path("AllJobOffreMois")
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response getAllJobOffreMois()
	{
		return Response.status(Status.ACCEPTED).entity(es.getAllJobOfferToday()).build();
	}
	
	@GET
	@Path("AllJobOffre/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response getAllJobOffreEntreprise(@PathParam("id") int id)
	{
		return Response.status(Status.ACCEPTED).entity(es.getAllJobOfferByEntreprise(id)).build();
	}
	
	/* InternshipCatalog */

	@POST
	@Path("InternshipCatalog/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response addInternshipCatalog(@PathParam("id") int idEnt,InternshipCataloge ic) {
		int idic = es.addInternshipCatalog(ic);
		es.addInternshipCatalogtoEntreprise(idEnt, idic);
		if(idic != 0)
		{
			return Response.status(Status.CREATED).entity("InternshipCatalog added Successful").build();
		}
		return Response.status(Status.NOT_ACCEPTABLE).entity("InternshipCatalog added Failed ").build();
	}

	@GET
	@Path("InternshipCatalog/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response getInternshipCatalogDetail(@PathParam("id") int idCat)
	{
		return Response.status(Status.ACCEPTED).entity(es.getInternshipCatalaogeDetails(idCat)).build();
	}

	@PUT
	@Path("updateInternshipCatalog")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response updateInternshipCatalog(InternshipCataloge ic) {
		es.updateInternshipCatalog(ic);
		return Response.status(Status.ACCEPTED).entity("Successful InternshipCataloge Update").build();
	}

	@GET
	@Path("AllInternshipCatalogEntreprise/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Secure(role = { "ResponsableEntreprise" })
	public Response getAllInternshipCatalogByEntreprise(@PathParam("id") int id)
	{
		return Response.status(Status.ACCEPTED).entity(es.getAllInternshipCatalogeByEntreprise(id)).build();
	}
	
}
