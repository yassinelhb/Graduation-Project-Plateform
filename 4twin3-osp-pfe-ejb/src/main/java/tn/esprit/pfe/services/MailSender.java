package tn.esprit.pfe.services;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import tn.esprit.pfe.interfaces.MailSenderInterface;



public class MailSender implements MailSenderInterface{

    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;

    @Override
    public void sendMessage(
            String Host,
            String user,
            String password,
            String port,
            String auth,
            String starttls,
            String recipient,
            String subject,
            String messageBody
    ) throws MessagingException {
        mailServerProperties = System.getProperties();
        /*
         * Host configuration
         */
        mailServerProperties.put("mail.smtp.port", port);
        mailServerProperties.put("mail.smtp.auth", auth);
        mailServerProperties.put("mail.smtp.starttls.enable", starttls);
        mailServerProperties.put("mail.smtp.host", "smtp.gmail.com");
        mailServerProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        /*
         *Session Configuration
         */
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        /*
         * Message Configuration
         */
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        generateMailMessage.setSubject(subject);
        generateMailMessage.setContent(messageBody, "text/html");
        /*
         * Transport Configuration
         */
        Transport transport = getMailSession.getTransport("smtp");
        transport.connect(Host, user, password);
        /*
         * Sending E-mail Message
         */
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();

    }
    



}