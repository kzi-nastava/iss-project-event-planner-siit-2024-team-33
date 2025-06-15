package rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user;

public class AuthentifiedUserValidationException extends Throwable {
	private static final long serialVersionUID = 1L;
	
	public AuthentifiedUserValidationException(String message) {
		super(message);
	}
}
