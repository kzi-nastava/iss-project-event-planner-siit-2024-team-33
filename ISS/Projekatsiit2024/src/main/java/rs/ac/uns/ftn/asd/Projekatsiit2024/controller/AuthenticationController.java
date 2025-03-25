package rs.ac.uns.ftn.asd.Projekatsiit2024.controller;

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
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.auth.RegisterUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.auth.RegisteredUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.auth.UserToken;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.AuthentifiedUserCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.OrganizerCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.ProviderCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.UserCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.User;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.AuthenticationService;
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
		User user = (User) authentication.getPrincipal();
		String jwt = tokenUtils.generateToken(user);
		int expiresIn = tokenUtils.getExpiredIn();
		
		return ResponseEntity.ok(new UserToken(jwt, expiresIn));
	}
	
	@PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RegisteredUser> createUser(@RequestBody RegisterUser registerUser) throws Exception, 
	UserCreationException, AuthentifiedUserCreationException, OrganizerCreationException, ProviderCreationException {
		
		AuthentifiedUser user = authenticationService.registerUser(registerUser);
		RegisteredUser registeredUser = new RegisteredUser(user);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
		
	}
	
	//exception handlers for this controller
	
	@ExceptionHandler(UserCreationException.class)
    public ResponseEntity<String> handleException(UserCreationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
	
	@ExceptionHandler(OrganizerCreationException.class)
    public ResponseEntity<String> handleException(OrganizerCreationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
	
	@ExceptionHandler(ProviderCreationException.class)
    public ResponseEntity<String> handleException(ProviderCreationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
	
	@ExceptionHandler(AuthentifiedUserCreationException.class)
    public ResponseEntity<String> handleException(AuthentifiedUserCreationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<String> handleException(AuthenticationException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<String> handleException(BadCredentialsException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
	}
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
		System.err.println("Caught exception: " + ex.getClass().getName());
		System.err.println("Exception message: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }
	
}
