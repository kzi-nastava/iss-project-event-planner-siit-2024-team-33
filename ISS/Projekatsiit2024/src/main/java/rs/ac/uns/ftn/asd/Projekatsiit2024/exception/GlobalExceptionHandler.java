package rs.ac.uns.ftn.asd.Projekatsiit2024.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventActivityValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventTypeValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventUserValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.offerReservation.ServiceBookingException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.reportPDF.PdfGenerationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.AuthentifiedUserValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.InvalidPasswordException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.InvalidPasswordFormatException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.OrganizerValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.ProviderValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.UserCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.UserDeletionException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.UserUpdateException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.UserUpgradeException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.verification.VerificationTokenException;
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
    
    @ExceptionHandler(UserDeletionException.class)
    public ResponseEntity<ErrorMessages> handleException(UserDeletionException ex) {
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
	
	
	//event exception
	
	@ExceptionHandler(EventValidationException.class)
	public ResponseEntity<ErrorMessages> handleException(EventValidationException ex) {
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessages(ex.getMessage(), ex.getErrorCode()));
	}
	
	@ExceptionHandler(EventActivityValidationException.class)
	public ResponseEntity<ErrorMessages> handleException(EventActivityValidationException ex) {
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessages(ex.getMessage()));
	}
	
	@ExceptionHandler(EventUserValidationException.class)
	public ResponseEntity<ErrorMessages> handleException(EventUserValidationException ex) {
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessages(ex.getMessage()));
	}
	
	
	//event type exception
	
	@ExceptionHandler(EventTypeValidationException.class)
	public ResponseEntity<ErrorMessages> handleException(EventTypeValidationException ex) {
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessages(ex.getMessage()));
	}
	
	
	//PDF generation exception
	@ExceptionHandler(PdfGenerationException.class)
    public ResponseEntity<ErrorMessages> handleException(PdfGenerationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessages(ex.getMessage()));
    }
	
	
	//service

    @ExceptionHandler(ServiceBookingException.class)
    public ResponseEntity<Map<String, String>> handleServiceBookingException(ServiceBookingException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("errorCode", ex.getErrorCode());
        body.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }
	
	
	//verification and upgrade
	
	@ExceptionHandler(VerificationTokenException.class)
    public ResponseEntity<ErrorMessages> handleException(VerificationTokenException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessages(ex.getMessage(), ex.getErrorCode()));
    }
	
	@ExceptionHandler(UserUpgradeException.class)
    public ResponseEntity<ErrorMessages> handleException(UserUpgradeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessages(ex.getMessage()));
    }
	
    
	@ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessages> handleException(Exception ex) {
		ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessages("An unexpected error occurred."));
    }
}
