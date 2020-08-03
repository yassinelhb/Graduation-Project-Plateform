package tn.esprit.pfe.interfaces;

import java.util.List;

import javax.ejb.Remote;

import javax.mail.MessagingException;

import tn.esprit.pfe.entities.Message;
import tn.esprit.pfe.entities.Notifications;


@Remote
public interface NotificationsServiceRemote {

	
	public int addNotification(Notifications notif);
	 public List<Notifications> getNotificationByUser( int id);
		public void TraitementNotification(Notifications notif);
	
		
		void sendMessageToClient(Message message) throws MessagingException;
}
