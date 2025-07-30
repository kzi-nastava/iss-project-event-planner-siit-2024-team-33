package rs.ac.uns.ftn.asd.Projekatsiit2024.controller.user;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.VerificationResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.user.VerificationService;

@RestController
@RequestMapping("/api/users/")
public class VerificationController {
    
    @Autowired
    private VerificationService verificationService;

    @GetMapping("verification")
    public ResponseEntity<VerificationResponse> verifyUser(@RequestParam("token") String token) {
    	String decodedToken = URLDecoder.decode(token, StandardCharsets.UTF_8);
    	verificationService.verifyUser(decodedToken);
        return ResponseEntity.ok(new VerificationResponse("User verified successfully!"));
    }
}

