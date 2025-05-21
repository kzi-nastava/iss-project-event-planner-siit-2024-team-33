package rs.ac.uns.ftn.asd.Projekatsiit2024.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Invitation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.InvitationRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;

@Service
public class invitationService {

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private AuthentifiedUserRepository authentifiedUserRepo;

    @Autowired
    private InvitationRepository invitationRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Transactional
    public void createInvitations(
        Integer eventId,
        List<String> emails,
        String invitationText,
        Date invitationDate,
        Integer authentifiedUserId,
        String senderEmail,
        String senderPassword) {
        Event event = eventRepo.findById(eventId)
            .orElseThrow(() -> new IllegalArgumentException(""));
        AuthentifiedUser inviter = authentifiedUserRepo.findById(authentifiedUserId)
            .orElseThrow(() -> new IllegalArgumentException(""));

        for (String email : emails) {
            AuthentifiedUser invitedUser = authentifiedUserRepo.findByEmail(email);

            Invitation invitation = new Invitation();
            invitation.setText(invitationText);
            invitation.setDate(invitationDate);
            invitation.setEvent(event);
            invitation.setInviter(inviter);
            invitationRepo.saveAndFlush(invitation);

            String subject = "Invitation to " + event.getName();
            String body = invitedUser != null
                ? "You are invited to join the event. Please log in to accept the invitation. \n\n http://localhost:4200/authentication/signin"
                : "You are invited to join the event. Click here to register and accept the invitation. \n\n http://localhost:4200/authentication/AK";

            sendEmail(senderEmail, senderPassword, email, subject, body);
        }
    }


    public void sendEmail(String senderEmail, String senderPassword, String recipientEmail, String subject, String body) {
        JavaMailSender mailSender = DynamicMailSender.createMailSender(senderEmail, "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");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public List<Invitation> getInvitationsByForEvent(Integer id){
    	Optional<Event> ev = eventRepo.findById(id);
    	
    	if(ev.isEmpty()) {
    		throw new IllegalArgumentException("");
    	}
    	Event event = ev.get();
    	List<Invitation> invs = invitationRepo.findByEvent(event);
		return invs;
    	
    }
    
    public Invitation getInvitationById(Integer id) {
    	Optional<Invitation> invitationOPT = invitationRepo.findById(id);
    	if(invitationOPT.isEmpty()) {
    		throw new IllegalArgumentException("");
    	}
    	Invitation invitation = invitationOPT.get();
    	
    	return invitation;
    }
    
    public void deleteInvitationById(Integer id) {
        Optional<Invitation> invitationOpt = invitationRepo.findById(id);
        if (invitationOpt.isEmpty()) {
            throw new IllegalArgumentException("");
        }
        
        invitationRepo.delete(invitationOpt.get());
    }

}
