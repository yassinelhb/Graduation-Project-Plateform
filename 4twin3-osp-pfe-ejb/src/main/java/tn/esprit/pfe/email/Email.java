package tn.esprit.pfe.email;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import tn.esprit.pfe.entities.Enseignant;
import tn.esprit.pfe.entities.EtatSheetPFE;
import tn.esprit.pfe.entities.Etudiant;
import tn.esprit.pfe.entities.InternshipAgreemen;
import tn.esprit.pfe.entities.SheetPFE;
import tn.esprit.pfe.entities.SheetPFEModification;

@Stateless
public class Email {
	// @Resource(lookup = "java:jboss/mail/Default")
	private Session session;

	public void sendQRCodeSheetPFE(SheetPFE sheetPFE) throws NamingException, AddressException, MessagingException {

		InitialContext ic = new InitialContext();
		session = (Session) ic.lookup("java:jboss/mail/Default");
		Message message = new MimeMessage(session);
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sheetPFE.getEtudiant().getEmail()));
		message.setSubject("QRCode Sheet PFE");

		Multipart multipart = new MimeMultipart();

		BodyPart messageBodyPart = new MimeBodyPart();

		String content = "<h3>Hello,</h3>" + sheetPFE.getEtudiant().getPrenom() + " " + sheetPFE.getEtudiant().getNom()
				+ "<br><br>" + "<p>\r\n"
				+ "It's QRCode will allow you to see the progress of the processing of the sheet PFE.</p>";

		messageBodyPart.setContent(content, "text/html");

		multipart.addBodyPart(messageBodyPart);

		messageBodyPart = new MimeBodyPart();

		String filename = "C:\\Users\\lhbya\\git\\4twin3-osp-pfe\\4twin3-osp-pfe-ejb\\src\\main\\resources\\QRCode\\"
				+ sheetPFE.getQrcode() + ".png";
		DataSource source = new FileDataSource(filename);
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(filename);

		multipart.addBodyPart(messageBodyPart);

		// Send the complete message parts
		message.setContent(multipart);

		Transport.send(message);
	}

	public void sendAgreemen(InternshipAgreemen internshipAgreemen)
			throws NamingException, AddressException, MessagingException {

		InitialContext ic = new InitialContext();
		session = (Session) ic.lookup("java:jboss/mail/Default");
		Message message = new MimeMessage(session);
		message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(internshipAgreemen.getEtudiant().getEmail()));
		message.setSubject("Internship agreement");

		Multipart multipart = new MimeMultipart();

		BodyPart messageBodyPart = new MimeBodyPart();

		String content = "<h3>Hello,</h3>" + internshipAgreemen.getEtudiant().getPrenom() + " "
				+ internshipAgreemen.getEtudiant().getNom() + "<br><br>" + "<p>\r\n"
				+ "Please you should come to the internship management to retrieve the agreement signed by ESPRIT.</p>";

		messageBodyPart.setContent(content, "text/html");

		multipart.addBodyPart(messageBodyPart);

		// Send the complete message parts
		message.setContent(multipart);

		Transport.send(message);
	}

	public void progressSheetPFE(SheetPFE sheetPFE) throws NamingException, AddressException, MessagingException {

		InitialContext ic = new InitialContext();
		session = (Session) ic.lookup("java:jboss/mail/Default");
		Message message = new MimeMessage(session);
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sheetPFE.getEtudiant().getEmail()));
		message.setSubject("Progress of sheet PFE");

		Multipart multipart = new MimeMultipart();

		BodyPart messageBodyPart = new MimeBodyPart();

		String content = "<h3>Hello,</h3>" + sheetPFE.getEtudiant().getPrenom() + " " + sheetPFE.getEtudiant().getNom()
				+ "<br><br>" + "<p>\r\n" + "Your sheet PFE being processed.</p>";

		messageBodyPart.setContent(content, "text/html");

		multipart.addBodyPart(messageBodyPart);

		// Send the complete message parts
		message.setContent(multipart);

		Transport.send(message);
	}

	public void entrepriseNotExist(SheetPFE sheetPFE) throws NamingException, AddressException, MessagingException {

		InitialContext ic = new InitialContext();
		session = (Session) ic.lookup("java:jboss/mail/Default");
		Message message = new MimeMessage(session);
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sheetPFE.getEtudiant().getEmail()));

		message.setSubject("Sheet PFE Refuse");

		Multipart multipart = new MimeMultipart();

		BodyPart messageBodyPart = new MimeBodyPart();

		String content = "<h3>Hello,</h3>" + sheetPFE.getEtudiant().getPrenom() + " " + sheetPFE.getEtudiant().getNom()
				+ "<br><br>" + "<p>\r\n"
				+ "Sorry, your sheet PFE Refuse.</p><p>Problem of refusing <br>"
				+ sheetPFE.getNote() + "</p>";

		messageBodyPart.setContent(content, "text/html");

		multipart.addBodyPart(messageBodyPart);

		// Send the complete message parts
		message.setContent(multipart);

		Transport.send(message);
	}

	public void requestCancelInternship(SheetPFE sheetPFE,String note) throws NamingException, AddressException, MessagingException {

		InitialContext ic = new InitialContext();
		session = (Session) ic.lookup("java:jboss/mail/Default");
		Message message = new MimeMessage(session);
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sheetPFE.getEtudiant().getEmail()));

		if(sheetPFE.getRequest().getEtat().equals(EtatSheetPFE.ACCEPTED)) {
			   message.setSubject("Internship CANCEL");

		}else {
			   message.setSubject("Your request Refuse");
		}

		Multipart multipart = new MimeMultipart();

		BodyPart messageBodyPart = new MimeBodyPart();

		String content = "";
		if(sheetPFE.getRequest().getEtat().equals(EtatSheetPFE.ACCEPTED)) {
			 content = "<h3>Hello,</h3>" + sheetPFE.getEtudiant().getPrenom() + " " + sheetPFE.getEtudiant().getNom()
					+ "<br><br>" + "<p>\r\n" +note +"</p>";
		}else {
			 content = "<h3>Hello,</h3>" + sheetPFE.getEtudiant().getPrenom() + " " + sheetPFE.getEtudiant().getNom()
					+ "<br><br>" + "<p>\r\n" + "Your request to cancel the internship has been refused.</p><p> Problem of refusing <br>" + note + "</p>";

			
		}
		
		messageBodyPart.setContent(content, "text/html");

		multipart.addBodyPart(messageBodyPart);

		// Send the complete message parts
		message.setContent(multipart);

		Transport.send(message);
	}
	
	public void reminderStudent(Etudiant etudiant) throws NamingException, MessagingException {
		
		InitialContext ic = new InitialContext();
		session = (Session) ic.lookup("java:jboss/mail/Default");
		Message message = new MimeMessage(session);
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(etudiant.getEmail()));

		message.setSubject("Reminder to create sheet PFE");

		Multipart multipart = new MimeMultipart();

		BodyPart messageBodyPart = new MimeBodyPart();

		String content = "<h3>Hello,</h3>" + etudiant.getPrenom() + " " + etudiant.getNom()
				+ "<br><br>" + "<p>\r\n"
				+ "Your must create a sheet PFE</p>";

		messageBodyPart.setContent(content, "text/html");

		multipart.addBodyPart(messageBodyPart);

		// Send the complete message parts
		message.setContent(multipart);

		Transport.send(message);
	}
	
	public void validateSheetPFE(SheetPFE sheetPFE,EtatSheetPFE etat, String note) throws NamingException, AddressException, MessagingException {
		
		InitialContext ic = new InitialContext();
		session = (Session) ic.lookup("java:jboss/mail/Default");
		Message message = new MimeMessage(session);
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sheetPFE.getEtudiant().getEmail()));

		if(etat.equals(EtatSheetPFE.VALIDATE)) {
			   message.setSubject("Validated Sheet PFE");
		}else {
			
			   message.setSubject("Refused subject PFE");
		}

		Multipart multipart = new MimeMultipart();

		BodyPart messageBodyPart = new MimeBodyPart();

		String content = "";
		if(etat.equals(EtatSheetPFE.VALIDATE)) {
			 content = "<h3>Hello,</h3>" + sheetPFE.getEtudiant().getPrenom() + " " + sheetPFE.getEtudiant().getNom()
					+ "<br><br>" + "<p>\r\n" + "Your subject of PFE has been accepted.</p>";
		}else {
			 content = "<h3>Hello,</h3>" + sheetPFE.getEtudiant().getPrenom() + " " + sheetPFE.getEtudiant().getNom()
					+ "<br><br>" + "<p>\r\n" + "Your subject of PFE has been refused</p><p><br>"+note+"</p>";

		}
		
		messageBodyPart.setContent(content, "text/html");

		multipart.addBodyPart(messageBodyPart);

		// Send the complete message parts
		message.setContent(multipart);

		Transport.send(message);
		
	}
	
	public void affectEnseignantToSheetPFE(SheetPFE sheetPFE,Enseignant enseignant,String type) throws MessagingException, NamingException {
		

		InitialContext ic = new InitialContext();
		session = (Session) ic.lookup("java:jboss/mail/Default");
		Message message = new MimeMessage(session);
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sheetPFE.getEtudiant().getEmail()));

		if(type.equals("VALIDATEUR")) {
			   message.setSubject("Affect validateur");
		}else if(type.equals("RAPPORTEUR")) {
			   message.setSubject("Affect rapporteur");
		}else if(type.equals("ENCADREUR")) {
				message.setSubject("Affect directeur");
		}

		Multipart multipart = new MimeMultipart();

		BodyPart messageBodyPart = new MimeBodyPart();

		String content = "";
		
		if(type.equals("VALIDATEUR")) {
			 content = "<h3>Hello,</h3>" + sheetPFE.getEtudiant().getPrenom() + " " + sheetPFE.getEtudiant().getNom()
						+ "<br><br>" + "<p>\r\n" + "Affect validateur '"+ enseignant.getPrenom() + " "+enseignant.getNom()+ "' to your sheet PFE.</p>"
								+ "<p>CONTACT : "+enseignant.getEmail()+" </p>";
		}else if(type.equals("RAPPORTEUR")) {
			 content = "<h3>Hello,</h3>" + sheetPFE.getEtudiant().getPrenom() + " " + sheetPFE.getEtudiant().getNom()
						+ "<br><br>" + "<p>\r\n" + "Affect rapporteur '"+ enseignant.getPrenom() + " "+enseignant.getNom()+ "' to your sheet PFE.</p>"
								+ "<p>CONTACT : "+enseignant.getEmail()+" </p>";
		}else if(type.equals("ENCADREUR")) {
			 content = "<h3>Hello,</h3>" + sheetPFE.getEtudiant().getPrenom() + " " + sheetPFE.getEtudiant().getNom()
						+ "<br><br>" + "<p>\r\n" + "Affect directeur '"+ enseignant.getPrenom() + " "+enseignant.getNom()+ "' to your sheet PFE.</p>"
								+ "<p>CONTACT : "+enseignant.getEmail()+" </p>";
		}
		
		messageBodyPart.setContent(content, "text/html");

		multipart.addBodyPart(messageBodyPart);

		// Send the complete message parts
		message.setContent(multipart);

		Transport.send(message);
	}
	
	public void changeEnseignantToSheetPFE(SheetPFE sheetPFE,Enseignant enseignant,String type) throws MessagingException, NamingException {
		

		InitialContext ic = new InitialContext();
		session = (Session) ic.lookup("java:jboss/mail/Default");
		Message message = new MimeMessage(session);
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sheetPFE.getEtudiant().getEmail()));
		
		if(type.equals("RAPPORTEUR")) {
			   message.setSubject("Modify rapporteur");
		}else if(type.equals("ENCADREUR")) {
				message.setSubject("Modify directeur");
		}

		Multipart multipart = new MimeMultipart();

		BodyPart messageBodyPart = new MimeBodyPart();

		String content = "";
		
	    if(type.equals("RAPPORTEUR")) {
			 content = "<h3>Hello,</h3>" + sheetPFE.getEtudiant().getPrenom() + " " + sheetPFE.getEtudiant().getNom()
						+ "<br><br>" + "<p>\r\n" + "Modify rapporteur '"+ enseignant.getPrenom() + " "+enseignant.getNom()+ "' to your sheet PFE.</p>"
								+ "<p>CONTACT : "+enseignant.getEmail()+" </p>";
		}else if(type.equals("ENCADREUR")) {
			 content = "<h3>Hello,</h3>" + sheetPFE.getEtudiant().getPrenom() + " " + sheetPFE.getEtudiant().getNom()
						+ "<br><br>" + "<p>\r\n" + "Modify directeur '"+ enseignant.getPrenom() + " "+enseignant.getNom()+ "' to your sheet PFE.</p>"
								+ "<p>CONTACT : "+enseignant.getEmail()+" </p>";
		}
		
		messageBodyPart.setContent(content, "text/html");

		multipart.addBodyPart(messageBodyPart);

		// Send the complete message parts
		message.setContent(multipart);

		Transport.send(message);
	}
	
	
public void accepteSheetPFE(SheetPFEModification sheetMod) throws MessagingException, NamingException {
		

		InitialContext ic = new InitialContext();
		session = (Session) ic.lookup("java:jboss/mail/Default");
		Message message = new MimeMessage(session);
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sheetMod.getSheetPFE().getEtudiant().getEmail()));
		
		if(sheetMod.getEtat().equals(EtatSheetPFE.ACCEPTED)) {
		    message.setSubject("Modification accepted");
		}else{
			message.setSubject("Modification refused");
		}

		Multipart multipart = new MimeMultipart();

		BodyPart messageBodyPart = new MimeBodyPart();

		String content = "";
		
	    if(sheetMod.getEtat().equals(EtatSheetPFE.ACCEPTED)) {
			 content = "<h3>Hello,</h3>" + sheetMod.getSheetPFE().getEtudiant().getPrenom() + " " + sheetMod.getSheetPFE().getEtudiant().getNom()
						+ "<br><br>" + "<p>\r\n" + " Your modification of sheet PFE accepted.</p>";
		}else  {
			 content = "<h3>Hello,</h3>" + sheetMod.getSheetPFE().getEtudiant().getPrenom() + " " +  sheetMod.getSheetPFE().getEtudiant().getNom()
						+ "<br><br>" + "<p>\r\n" + "Your modification of sheet PFE refused.<p> Problem of refusing <br>" + sheetMod.getNote() + "</p>";
						
		}
		
		messageBodyPart.setContent(content, "text/html");

		multipart.addBodyPart(messageBodyPart);

		// Send the complete message parts
		message.setContent(multipart);

		Transport.send(message);
	}
	
	

}
