package rs.ac.uns.ftn.asd.Projekatsiit2024.service;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import java.util.Properties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class DynamicMailSender {

    public static JavaMailSender createMailSender(String senderEmail, String senderPassword) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.sendgrid.net");
        props.put("mail.smtp.port", "587");

        // Use "apikey" as the username and senderPassword as the API key
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("apikey", senderPassword);
            }
        });

        // Configure JavaMailSender
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setSession(session);
        mailSender.setHost("smtp.sendgrid.net");
        mailSender.setPort(587);
        mailSender.setUsername("apikey");
        mailSender.setPassword(senderPassword); // Your SendGrid API Key
        mailSender.setProtocol("smtp");
        mailSender.setDefaultEncoding("UTF-8");

        return mailSender;
    }
}
