package rs.ac.uns.ftn.asd.Projekatsiit2024.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.GetUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.RegisterUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.RegisteredUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.AuthentifiedUserCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.OrganizerCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.ProviderCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.UserCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.user.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RegisteredUser> createUser(@RequestBody RegisterUser registerUser) throws Exception, 
	UserCreationException, AuthentifiedUserCreationException, OrganizerCreationException, ProviderCreationException {
		
		AuthentifiedUser user = userService.registerUser(registerUser);
		RegisteredUser registeredUser = new RegisteredUser(user);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/me")
    public ResponseEntity<GetUserDTO> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
		return ResponseEntity.ok(new GetUserDTO(userPrincipal.getUser()));
    }
	
	/*@GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetUserDTO> getMyUser() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		
		AuthentifiedUser au = userRepo.findByEmail(email);
		if(au == null)
			return ResponseEntity.notFound().build();
		
		GetUserDTO user = new GetUserDTO(au);
		
		return new ResponseEntity<GetUserDTO>(user, HttpStatus.OK);
	}*/
	
    @PostMapping(value = "/{blockerId}/block/{blockedId}")
    public ResponseEntity<String> blockUser(@PathVariable Integer blockerId, @PathVariable Integer blockedId) {
        try {
            userService.blockAUser(blockerId, blockedId);
            return ResponseEntity.ok("");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
    }

    @DeleteMapping(value = "/{blockerId}/block/{blockedId}")
    public ResponseEntity<String> unblockUser(@PathVariable Integer blockerId, @PathVariable Integer blockedId) {
        try {
            userService.unblockAUser(blockerId, blockedId);
            return ResponseEntity.ok("");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
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
    
	@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
		ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }
    
    /*@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetUserDTO> getUser(@PathVariable("id") Long id) {
		GetUserDTO user = new GetUserDTO();
		
		return new ResponseEntity<GetUserDTO>(user, HttpStatus.OK);
	}
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<GetUserDTO>> getUsers() {
		Collection<GetUserDTO> users = new ArrayList<>();

		GetUserDTO user = new GetUserDTO();
		GetUserDTO user2 = new GetUserDTO();

		users.add(user);
		users.add(user2);

		return new ResponseEntity<Collection<GetUserDTO>>(users, HttpStatus.OK);
	}
    
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UpdatedUserDTO> updateUser(@RequestBody UpdateUserDTO user, @PathVariable Long id)
			throws Exception {
		UpdatedUserDTO updatedUser = new UpdatedUserDTO();

		return new ResponseEntity<UpdatedUserDTO>(updatedUser, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") Long Id) {
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}*/
    
}
