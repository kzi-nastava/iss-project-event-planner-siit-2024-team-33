package rs.ac.uns.ftn.asd.Projekatsiit2024.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.invitation.PostInvitationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Invitation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.VerificationToken;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.VerificationTokenRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.InvitationRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;

@Service
public class VerificationService {

    
    @Autowired
    private VerificationTokenRepository tokenRepo;
    @Transactional
    public void sendVerificationEmail(AuthentifiedUser user) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpirationDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
        tokenRepo.save(verificationToken);

        String verificationUrl = "http://localhost:8080/api/verify?token=" + token;

        JavaMailSender mailSender = DynamicMailSender.createMailSender("mirkodjukic718@gmail.com", "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");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setFrom("mirkodjukic718@gmail.com");
        message.setSubject("Account Verification");
        message.setText("Click the link to verify your account:\n\n" + verificationUrl);

        mailSender.send(message);
    }
}
