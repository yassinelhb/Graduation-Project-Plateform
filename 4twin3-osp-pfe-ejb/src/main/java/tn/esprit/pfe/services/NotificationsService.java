package tn.esprit.pfe.services;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import tn.esprit.pfe.entities.Message;
import tn.esprit.pfe.entities.Notifications;
import tn.esprit.pfe.interfaces.NotificationsServiceRemote;

@Stateless
@LocalBean
public class NotificationsService implements NotificationsServiceRemote {

	
	@PersistenceContext
	EntityManager em;
	
	
	@Override
	public int addNotification(Notifications notif) {
		em.persist(notif);
		return notif.getIdNotification();

	}

	@Override
	public List<Notifications> getNotificationByUser(int id) {
		Query query = em.createQuery("select n.text , n.user.id from Notifications n where n.user.id = :id AND n.etat IS NULL")
				.setParameter("id", id);
		return query.getResultList();
	}

	@Override
	public void TraitementNotification(Notifications notif) {
		try
		{
			int query = em.createQuery("update Notifications notif set notif.etat =  :etat where notif.idNotification = :id").
					setParameter("etat",notif.getEtat()).setParameter("id",notif.getIdNotification())
					.executeUpdate();
		}
		catch (Exception e )
		{
			System.out.println(e);
		}
		
	}
	
	@Override
	public void sendMessageToClient(Message message) throws MessagingException {
		MailSender mailSender = new MailSender();
		mailSender.sendMessage("smtp.gmail.com", "mouhamed.boufaied@gmail.com", "23233901**", "587", "true", "true",
				message.getToUserEmail(), message.getSubject() + ": " + message.getType(), message.getMessage());
		em.persist(message);

	}

	
	

	
	
}
