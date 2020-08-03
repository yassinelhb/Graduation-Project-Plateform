package tn.esprit.webServices;



import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.pfe.entities.Notifications;
import tn.esprit.pfe.entities.Reclamation;
import tn.esprit.pfe.interfaces.NotificationsServiceRemote;


@Path("/notif")
@ManagedBean
public class NotificationsWebServices {

	@EJB(beanName="NotificationsService")
	 NotificationsServiceRemote nsr;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addNotification(Notifications notif) {
		nsr.addNotification(notif)	;
		return Response.status(Status.CREATED).entity(notif).build();
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getById/{id}")
	public List<Notifications> getNotificationById(@PathParam("id") int id ) {
		return nsr.getNotificationByUser(id);

	}
	
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void TraiterNotification(Notifications notif ) {	
		nsr.TraitementNotification(notif);

	}
	
	
}
