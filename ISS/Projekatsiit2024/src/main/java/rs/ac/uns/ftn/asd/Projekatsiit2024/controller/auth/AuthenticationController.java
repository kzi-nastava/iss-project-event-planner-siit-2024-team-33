package rs.ac.uns.ftn.asd.Projekatsiit2024.controller.auth;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.auth.AuthenticationRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.auth.UserToken;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.auth.AuthenticationService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.utils.TokenUtils;

@RestController
@RequestMapping(value = "api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {
	
	@Autowired
	private TokenUtils tokenUtils;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	
	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserToken> createUserToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception, 
	AuthenticationException {
		//AuthenticationException if username(email) and password aren't correct
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationRequest.getEmail(), authenticationRequest.getPassword()));
		
		//if authentication successful, put user in current security context
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		//token creation
		UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
		String jwt = tokenUtils.generateToken(user, new Date());
		int expiresIn = tokenUtils.getExpiredIn();
		
		return ResponseEntity.ok(new UserToken(jwt, expiresIn));
	}
	
	@GetMapping("/check-email")
	public ResponseEntity<Boolean> checkEmailAvailability(@RequestParam String email) {
	    boolean isTaken = authenticationService.isEmailNotAvailable(email);
	    return ResponseEntity.ok(isTaken);
	}
}