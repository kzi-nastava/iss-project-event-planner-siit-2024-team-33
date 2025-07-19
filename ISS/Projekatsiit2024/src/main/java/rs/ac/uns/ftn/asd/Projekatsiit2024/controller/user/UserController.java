package rs.ac.uns.ftn.asd.Projekatsiit2024.controller.user;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.auth.UserToken;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.GetUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.RegisterUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.RegisteredUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.UpdatePassword;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.UpdateUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.UpdatedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.AuthentifiedUserValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.InvalidPasswordException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.InvalidPasswordFormatException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.OrganizerValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.ProviderValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.UserCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.UserUpdateException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.user.UserService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.utils.TokenUtils;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.VerificationService;


@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private VerificationService verificationService;
	@Autowired
	private AuthentifiedUserRepository userRepo;
	
	@Autowired
	private TokenUtils tokenUtils;
	
	
	@PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RegisteredUser> createUser(@RequestBody RegisterUser registerUser) 
			throws Exception, UserCreationException, AuthentifiedUserValidationException, 
			OrganizerValidationException, ProviderValidationException {
		
		AuthentifiedUser user = userService.registerUser(registerUser);
		RegisteredUser registeredUser = new RegisteredUser(user);
		
		verificationService.sendVerificationEmail(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
	}
	
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/me",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetUserDTO> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {

		return ResponseEntity.ok(GetUserDTO.from(userPrincipal.getUser()));
    }
	
	
	@PreAuthorize("isAuthenticated()")
	@PutMapping(value = "/update/profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UpdatedUser> updateUser(@AuthenticationPrincipal UserPrincipal userPrincipal, 
	@RequestBody UpdateUser updateUser) throws Exception, UserUpdateException, AuthentifiedUserValidationException, 
	OrganizerValidationException, ProviderValidationException {
		AuthentifiedUser user = userPrincipal.getUser();
		UpdatedUser updatedUser = userService.updateUser(user, updateUser);
		
		return ResponseEntity.ok(updatedUser);
	}
	
	
	@PreAuthorize("isAuthenticated()")
	@PutMapping(value = "/update/password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserToken> updatePassword(@AuthenticationPrincipal UserPrincipal userPrincipal, 
			@RequestBody UpdatePassword updatePassword) throws UserUpdateException, InvalidPasswordException, InvalidPasswordFormatException {
		AuthentifiedUser user = userPrincipal.getUser();		
		AuthentifiedUser updatedUser = userService.updatePassword(user, updatePassword);
		
		UserPrincipal updatedPrincipal = new UserPrincipal(updatedUser);
		
		//new token creation
		Timestamp passwordChangeDate = userService.getUpdatedPasswordDate(updatedUser);
		String jwt = tokenUtils.generateToken(updatedPrincipal, new Date(passwordChangeDate.getTime()));
		int expiresIn = tokenUtils.getExpiredIn();
		
		return ResponseEntity.ok(new UserToken(jwt, expiresIn));
	}
	
	
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping(value = "/terminate/profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> terminateUser(@AuthenticationPrincipal UserPrincipal userPrincipal) throws UserUpdateException {
		
		AuthentifiedUser user = userPrincipal.getUser();
		AuthentifiedUser updatedUser = userService.terminateUser(user);
		
		return ResponseEntity.ok(updatedUser.getIsDeleted());
	}
	
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/block/{blockedEmail}")
	public ResponseEntity<String> blockUser(@AuthenticationPrincipal UserPrincipal userPrincipal,
	                                        @PathVariable String blockedEmail) {
	    int blockerId = userPrincipal.getUser().getId();
	    try {
	        userService.blockAUser(blockerId, blockedEmail);
	        return ResponseEntity.ok("User blocked successfully.");
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while blocking the user.");
	    }
	}


    
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping(value = "/block/{blockedEmail}")
	public ResponseEntity<String> unblockUser(@AuthenticationPrincipal UserPrincipal userPrincipal,
	                                          @PathVariable String blockedEmail) {
	    int blockerId = userPrincipal.getUser().getId();
	    try {
	        userService.unblockAUser(blockerId, blockedEmail);
	        return ResponseEntity.ok("User unblocked successfully.");
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while unblocking the user.");
	    }
	}
}
