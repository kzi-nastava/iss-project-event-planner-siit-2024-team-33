package rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event;

public class EventTypeValidationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public EventTypeValidationException(String message) {
		super(message);
	}
}
