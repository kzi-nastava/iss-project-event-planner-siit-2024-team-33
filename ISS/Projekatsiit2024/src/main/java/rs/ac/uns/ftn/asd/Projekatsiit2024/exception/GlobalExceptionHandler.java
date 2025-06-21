package rs.ac.uns.ftn.asd.Projekatsiit2024.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.AuthentifiedUserValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.InvalidPasswordException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.InvalidPasswordFormatException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.OrganizerValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.ProviderValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.UserCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.UserUpdateException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.utils.ErrorMessages;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	//authentication exception
	
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
	
	
	//user exception
	
	@ExceptionHandler(UserCreationException.class)
    public ResponseEntity<ErrorMessages> handleException(UserCreationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessages(ex.getMessage()));
    }
    
    @ExceptionHandler(UserUpdateException.class)
    public ResponseEntity<ErrorMessages> handleException(UserUpdateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessages(ex.getMessage()));
    }
	
	@ExceptionHandler(OrganizerValidationException.class)
    public ResponseEntity<ErrorMessages> handleException(OrganizerValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessages(ex.getMessage()));
    }
	
	@ExceptionHandler(ProviderValidationException.class)
    public ResponseEntity<ErrorMessages> handleException(ProviderValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessages(ex.getMessage()));
    }
	
	@ExceptionHandler(AuthentifiedUserValidationException.class)
    public ResponseEntity<ErrorMessages> handleException(AuthentifiedUserValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessages(ex.getMessage()));
    }
	
	@ExceptionHandler(InvalidPasswordException.class)
	public ResponseEntity<ErrorMessages> handleException(InvalidPasswordException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessages(ex.getMessage()));
	}

	@ExceptionHandler(InvalidPasswordFormatException.class)
	public ResponseEntity<ErrorMessages> handleException(InvalidPasswordFormatException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessages(ex.getMessage()));
	}
	
	
	
	
    
	@ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessages> handleException(Exception ex) {
		ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessages("An unexpected error occurred."));
    }
}
