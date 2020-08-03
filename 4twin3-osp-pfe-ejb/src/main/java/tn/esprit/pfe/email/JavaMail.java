package tn.esprit.pfe.email;

import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class JavaMail {
	public static void sendConfirmationAcount(String Recepient) throws MessagingException {
	    // 1 -> Création de la session
	    Properties properties = new Properties();
	    properties.put("mail.smtp.auth", "true");
	    properties.put("mail.smtp.starttls.enable", "true");
	    properties.put("mail.smtp.host", "smtp.gmail.com");
	    properties.put("mail.smtp.port", "587");
	    		
	   String myAccountEmail="ahmed.ahmed@esprit.tn";
	   String password="Ahmedahmed96";
	   Session session=Session.getInstance(properties,new Authenticator() {
		   @Override
		   protected PasswordAuthentication getPasswordAuthentication() {
			   return new PasswordAuthentication(myAccountEmail, password);
		   }
	}
	   );
	   Message message =prepareMessage(session,myAccountEmail,Recepient);
	   Transport.send(message);
	}
	public static void sendMessageforupdate(String Recepient) throws MessagingException {
	    // 1 -> Création de la session
	    Properties properties = new Properties();
	    properties.put("mail.smtp.auth", "true");
	    properties.put("mail.smtp.starttls.enable", "true");
	    properties.put("mail.smtp.host", "smtp.gmail.com");
	    properties.put("mail.smtp.port", "587");
	    		
	   String myAccountEmail="ahmed.ahmed@esprit.tn";
	   String password="Ahmedahmed96";
	   Session session=Session.getInstance(properties,new Authenticator() {
		   @Override
		   protected PasswordAuthentication getPasswordAuthentication() {
			   return new PasswordAuthentication(myAccountEmail, password);
		   }
	}
	   );
	   Message message =prepareMessageforupdate(session,myAccountEmail,Recepient);
	   Transport.send(message);
	}
private static Message prepareMessageforupdate(Session session,String myAccountEmail,String Recepient ) {
		
		try {
			Message message =new MimeMessage(session);
			message.setFrom(new InternetAddress(myAccountEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(Recepient));
			message.setSubject("Your Mail was Changed successfully");
			
			message.setText("Welcome To Our Professional Network ..!");
			
			return message;
		}catch(Exception ex)
		{
			Logger.getLogger(JavaMail.class.getName()).log(Level.SEVERE,null,ex);;
		}
		return null;
	}
	private static Message prepareMessage(Session session,String myAccountEmail,String Recepient ) {
		
		try {
			Message message =new MimeMessage(session);
			message.setFrom(new InternetAddress(myAccountEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(Recepient));
			message.setSubject("Entreprise Created successfully");
			
			message.setText("Welcome To Our ESP Platefom ..!");
			
			return message;
		}catch(Exception ex)
		{
			Logger.getLogger(JavaMail.class.getName()).log(Level.SEVERE,null,ex);;
		}
		return null;
	}
}
