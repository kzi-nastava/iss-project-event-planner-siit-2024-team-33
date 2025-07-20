package rs.ac.uns.ftn.asd.Projekatsiit2024.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.invitation.PostInvitationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.VerificationToken;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.VerificationTokenRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.InvitationRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;

@Service
public class VerificationService {

    
    @Autowired
    private VerificationTokenRepository tokenRepo;
    @Autowired
    private AuthentifiedUserRepository userRepo;
    
    @Transactional
    public void sendVerificationEmail(Integer userId) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUserId(userId);
        verificationToken.setExpirationDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
        tokenRepo.save(verificationToken);

        String verificationUrl = "http://localhost:8080/api/verify?token=" + token;
        
        JavaMailSender mailSender = DynamicMailSender.createMailSender("senjare2@gmail.com", "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");
        
        Optional<AuthentifiedUser> userOpt = userRepo.findById(userId);
        if(userOpt.isEmpty()) {
        	throw new IllegalArgumentException("You are not logged in");
        }
        
        AuthentifiedUser user = userOpt.get();
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setFrom("milosmilic2@gmail.com");
        message.setSubject("Account Verification");
        message.setText("Click the link to verify your account:\n\n" + verificationUrl);

        mailSender.send(message);
    }
    
    public void verifyUser(String decodedToken) {
    	Optional<VerificationToken> optionalToken = tokenRepo.findByToken(decodedToken);

        if (optionalToken.isEmpty()) {
        	throw new IllegalArgumentException("Invalid token");
        }

        VerificationToken verificationToken = optionalToken.get();

        if (verificationToken.getExpirationDate().before(new Date())) {
        	throw new IllegalArgumentException("Token expired");
        }

        
        Integer userId = verificationToken.getUserId();
        Optional<AuthentifiedUser> userOpt = userRepo.findById(userId);
        if(userOpt.isEmpty()) {
        	throw new IllegalArgumentException("You are not logged in");
        }
        
        AuthentifiedUser user = userOpt.get();
        
        user.setIsVerified(true);
        userRepo.save(user);
    }
}
