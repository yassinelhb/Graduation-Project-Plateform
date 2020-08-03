package tn.esprit.webServices;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import tn.esprit.pfe.entities.Message;
import tn.esprit.pfe.interfaces.MailSenderInterface;
import tn.esprit.pfe.interfaces.NotificationsServiceRemote;

@Path("/mailsender")
@ManagedBean
public class MailSenderWebService {

	
	
	@EJB(beanName="NotificationsService")
	NotificationsServiceRemote nsr;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendMessageToClient(Message message) {
		try {
			nsr.sendMessageToClient(message);
			
		} catch (MessagingException e) {
			System.out.println("error sending message");
		}
	}
}
