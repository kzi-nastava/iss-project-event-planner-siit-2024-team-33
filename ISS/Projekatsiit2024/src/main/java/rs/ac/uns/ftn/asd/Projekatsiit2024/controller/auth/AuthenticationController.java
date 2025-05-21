package rs.ac.uns.ftn.asd.Projekatsiit2024.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.auth.AuthenticationRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.auth.EmailRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.auth.UserToken;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.auth.AuthenticationService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.utils.ErrorMessages;
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
		String jwt = tokenUtils.generateToken(user);
		int expiresIn = tokenUtils.getExpiredIn();
		
		return ResponseEntity.ok(new UserToken(jwt, expiresIn));
	}
	
	@PostMapping(value="/check-email", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> checkEmailAvailability(@RequestBody EmailRequest emailReq) throws Exception {
		Boolean available = authenticationService.isEmailAvailable(emailReq.getEmail());
		
		return ResponseEntity.status(HttpStatus.OK).body(available);
	}
	
	//exception handlers for this controller
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorMessages> handleException(AuthenticationException ex) {
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessages(
	    		"Invalid email or password."));
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorMessages> handleException(BadCredentialsException ex) {
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessages(
	    		"Invalid email or password."));
	}
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }
	
}
