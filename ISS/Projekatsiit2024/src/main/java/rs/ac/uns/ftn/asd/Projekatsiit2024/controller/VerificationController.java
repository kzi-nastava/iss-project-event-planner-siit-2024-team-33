package rs.ac.uns.ftn.asd.Projekatsiit2024.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.VerificationToken;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.VerificationTokenRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.VerificationService;

@RestController
@RequestMapping("/api")
public class VerificationController {

    @Autowired
    private VerificationTokenRepository tokenRepo;

    @Autowired
    private AuthentifiedUserRepository userRepo;
    
    @Autowired
    private VerificationService verificationService;

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token) {
    	String decodedToken = URLDecoder.decode(token, StandardCharsets.UTF_8);
    	verificationService.verifyUser(decodedToken);
        return ResponseEntity.ok("User verified successfully!");
    }
}

