package rs.ac.uns.ftn.asd.Projekatsiit2024.exception.verification;

public class EmailSendingException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public EmailSendingException(String message) {
        super(message);
    }
}
