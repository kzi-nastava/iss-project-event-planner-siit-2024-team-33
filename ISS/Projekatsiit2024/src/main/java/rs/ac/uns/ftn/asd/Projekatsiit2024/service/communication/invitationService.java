package rs.ac.uns.ftn.asd.Projekatsiit2024.service.communication;

import java.util.Date;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.invitation.PostInvitationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.communication.InvitationStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Invitation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.communication.InvitationRepository;
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
            invitation.setInvitedUser(email);
            invitationRepo.saveAndFlush(invitation);

            String subject = "Invitation to " + event.getName();
            String loginLink = "myapp://invitationlogin?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8);
            String registerLink = "myapp://invitationregister?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8);

            String body;
            if (invitedUser != null) {
                body = "<p>You are invited to join the event.</p>"
                     + "<p><a href=\"" + loginLink + "\">" + loginLink + "</a></p>";
            } else {
                body = "<p>You are invited to join the event.</p>"
                     + "<p><a href=\"" + registerLink + "\">" + registerLink + "</a></p>";
            }

            sendEmail(inviter.getEmail(), inviter.getPassword(), email, subject, body);
        }
    }
    
    @Transactional
    public void acceptInvitation(Integer invitationId, AuthentifiedUser user) {
        Invitation invitation = invitationRepo.findById(invitationId)
            .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));

        if (invitation.getStatus() == InvitationStatus.ACCEPTED) {
            throw new IllegalStateException("Invitation already accepted");
        }

        if (invitation.getInvitedUser() != null && !invitation.getInvitedUser().equals(user.getEmail())) {
            throw new IllegalArgumentException("This invitation is not for you.");
        }

    
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setInvitedUser(user.getEmail());

        Event event = invitation.getEvent();
        if (!event.getListOfAttendees().contains(user)) {
        	if (event.getNumOfAttendees() == event.getListOfAttendees().size()) {
        		throw new EventValidationException("There is no more place left to join event.", 
        				"NO_PLACE");
        	}
        	invitationRepo.save(invitation);
            event.getListOfAttendees().add(user);
            eventRepo.save(event);
        }
    }

    
    @Transactional
    public void denyInvitation(Integer invitationId, AuthentifiedUser user) {
        Invitation invitation = invitationRepo.findById(invitationId)
            .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));

        if (invitation.getStatus() == InvitationStatus.ACCEPTED) {
            throw new IllegalStateException("Invitation already accepted");
        }

        if (invitation.getStatus() == InvitationStatus.DENIED) {
            throw new IllegalStateException("Invitation already denied");
        }

        if (invitation.getInvitedUser() != null && !invitation.getInvitedUser().equals(user.getEmail())) {
            throw new IllegalArgumentException("This invitation is not for you.");
        }

        invitation.setStatus(InvitationStatus.DENIED);
        invitation.setInvitedUser(user.getEmail());
        invitationRepo.save(invitation);
    }






    public void sendEmail(String senderEmail, String senderPassword, String recipientEmail, String subject, String body) {
        JavaMailSender mailSender = DynamicMailSender.createMailSender("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");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true, "UTF-8");
	        helper.setFrom("hogridersunited@gmail.com");
	        helper.setTo(recipientEmail);
	        helper.setSubject(subject);
	        helper.setText(body, true);

            mailSender.send(message);
            System.out.println("Email sent successfully!");
		} catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
			e.printStackTrace();
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
    
    public List<Invitation> getInvitationForUser(AuthentifiedUser user){
    	List<Invitation> allInvitations = invitationRepo.findAll();
    	List<Invitation> ret = new ArrayList<Invitation>(); 
    	for(Invitation invitation:allInvitations) {
    		if(invitation.getInvitedUser()== user.getEmail() && invitation.getStatus() == InvitationStatus.PENDING) {
    			ret.add(invitation);
    		}
    	}
    	return ret;
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
