package rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event;

public class EventUserValidationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public EventUserValidationException(String message) {
		super(message);
	}
}
