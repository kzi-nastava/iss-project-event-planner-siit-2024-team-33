package rs.ac.uns.ftn.asd.Projekatsiit2024.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.invitation.PostInvitationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Invitation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.InvitationRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;

@Service
public class invitationService {

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private AuthentifiedUserRepository authentifiedUserRepo;

    @Autowired
    private InvitationRepository invitationRepo;


    @Transactional
    public void createInvitations(
        PostInvitationDTO Sentinvitation,
        AuthentifiedUser user,
        Date invitationDate) {
    	
        Event event = eventRepo.findById(Sentinvitation.getEventId())
            .orElseThrow(() -> new IllegalArgumentException(""));
        AuthentifiedUser inviter = authentifiedUserRepo.findById(user.getId())
            .orElseThrow(() -> new IllegalArgumentException(""));

        for (String email : Sentinvitation.getEmailAddresses()) {
            AuthentifiedUser invitedUser = authentifiedUserRepo.findByEmail(email);

            Invitation invitation = new Invitation();
            invitation.setText(Sentinvitation.getMessage());
            invitation.setDate(invitationDate);
            invitation.setEvent(event);
            invitation.setInviter(inviter);
            invitationRepo.saveAndFlush(invitation);

            String subject = "Invitation to " + event.getName();
            String body = invitedUser != null
                ? "You are invited to join the event. Please log in to accept the invitation. \n\n http://localhost:4200/authentication/signin"
                : "You are invited to join the event. Click here to register and accept the invitation. \n\n http://localhost:4200/authentication/AK";

            sendEmail(inviter.getEmail(), inviter.getPassword(), email, subject, body);
        }
    }


    private void sendEmail(String senderEmail, String senderPassword, String recipientEmail, String subject, String body) {
        JavaMailSender mailSender = DynamicMailSender.createMailSender("mirkodjukic718@gmail.com", "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");

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
