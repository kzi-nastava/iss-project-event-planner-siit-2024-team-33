package rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user;

public class InvalidPasswordException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public InvalidPasswordException(String message) {
		super(message);
	}

}
