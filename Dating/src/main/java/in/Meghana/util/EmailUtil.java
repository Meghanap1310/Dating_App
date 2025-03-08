package in.Meghana.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;

@Component
public class EmailUtil 
{
	public void sendMessage(String to,String subject ,String body)
	{
		 String host = "smtp.gmail.com"; // SMTP server (e.g., Gmail: smtp.gmail.com)
	     final String username = "pmeghana1310@gmail.com"; // Your email
	     final String password = "lfwo bthg bbgj dwti"; // Your email password

	     // Recipient's email ID
	    

	     // Setting up the mail server properties
	     Properties properties = new Properties();
	     properties.put("mail.smtp.host", host);
	        properties.put("mail.smtp.port", "587"); // Default port for SMTP
	        properties.put("mail.smtp.auth", "true");
	        properties.put("mail.smtp.starttls.enable", "true"); 
	     
	     

	     // Get the Session object and authenticate the user
	     Session session = Session.getInstance(properties, new Authenticator() {
	         protected PasswordAuthentication getPasswordAuthentication() {
	             return new PasswordAuthentication(username, password);
	         }
	     });

	     try {
	         // Create a default MimeMessage object
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field
	         message.setFrom(new InternetAddress(username));

	         // Set To: header field
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

	         // Set Subject: header field
	         message.setSubject(subject);

	         // Set the actual message
	         message.setText(body);

	         // Send the email
	         Transport.send(message);

	         System.out.println("Email sent successfully!");
	     } catch (MessagingException e) {
	         e.printStackTrace();
	     }
	}
}
