package rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event;

import lombok.Getter;

@Getter
public class EventValidationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private final String errorCode;
	
	public EventValidationException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	
	public EventValidationException(String message) {
		super(message);
		this.errorCode = "";
	}
}
