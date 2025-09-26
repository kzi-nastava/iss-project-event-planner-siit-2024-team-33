package rs.ac.uns.ftn.asd.Projekatsiit2024.service.user;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.UpgradeUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.UserUpgradeException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.verification.EmailSendingException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.verification.VerificationTokenException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.communication.VerificationToken;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.UnverifiedUserUpgrade;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.communication.VerificationTokenRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;

@Service
public class VerificationService {
    
    @Autowired
    private VerificationTokenRepository tokenRepo;
    @Autowired
    private AuthentifiedUserRepository userRepo;
    @Autowired
    private UserUpgradeService userUpgradeService;
    
    @Value("${mail.sender.email}")
    private String senderEmail;
    @Autowired
    private JavaMailSender mailSender;

    
    @Transactional
	public UnverifiedUserUpgrade upgradeUser(UserPrincipal userPrincipal, UpgradeUser upgradeUser) 
			throws UserUpgradeException {
		UnverifiedUserUpgrade uuu = userUpgradeService.createUpgradeUserRequest(userPrincipal, upgradeUser);
		//send verification message
		this.sendVerificationEmail(uuu.getAUser());
		
		return uuu;
	}

    
    public void sendVerificationEmail(AuthentifiedUser user) {
    	try {
    		String token = UUID.randomUUID().toString();

            VerificationToken verificationToken = new VerificationToken();
            verificationToken.setToken(token);
            verificationToken.setUserId(user.getId());
            verificationToken.setExpirationDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
            tokenRepo.save(verificationToken);

//            String verificationUrl = "http://localhost:4200/authentication/verification?token=" + token;
            String verificationUrl = "http://192.168.2.8:8080/api/verify?token=" + token;
            
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setFrom(senderEmail);
            message.setSubject("Account Verification");
            message.setText("Click the link to verify your account:\n\n" + verificationUrl);

            mailSender.send(message);
    	} catch (MailException | DataAccessException ex) {
            throw new EmailSendingException("Failed to send verification email or save token.");
        }
    }
    
    
    
    @Transactional
    public void verifyUser(String decodedToken) {
    	
    	//token verification
    	Optional<VerificationToken> optionalToken = tokenRepo.findByToken(decodedToken);
        if (optionalToken.isEmpty()) {
        	throw new VerificationTokenException("Invalid token.");
        }
        VerificationToken verificationToken = optionalToken.get();
        if (verificationToken.getExpirationDate().before(new Date())) {
        	throw new VerificationTokenException("Token expired.", "EXPIRED");
        }

        //user verification
        Integer userId = verificationToken.getUserId();
        Optional<AuthentifiedUser> userOpt = userRepo.findById(userId);
        if(userOpt.isEmpty()) {
        	throw new VerificationTokenException("No such user can be verified.");
        }
        AuthentifiedUser user = userOpt.get();
        
        if ((user.getRole().getName().equals("ORGANIZER_ROLE") || 
        		user.getRole().getName().equals("PROVIDER_ROLE")) 
        		&& user.getIsVerified().equals(true))
        	throw new VerificationTokenException("You have already verified your account.", 
        			"ALREADY_VERIFIED");
        
        
        //upgrade user or just verify him
        if (user.getRole().getName().equals("AUSER_ROLE")) {
        	userUpgradeService.upgradeUserFinal(user);
        }
        else {
        	user.setIsVerified(true);
        	userRepo.save(user);
        }
    }
}
