package rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event;

public class EventValidationException extends RuntimeException {
	private static final long serialVersionUID = 1L;	
	public EventValidationException(String message) {
		super(message);
	}
}
