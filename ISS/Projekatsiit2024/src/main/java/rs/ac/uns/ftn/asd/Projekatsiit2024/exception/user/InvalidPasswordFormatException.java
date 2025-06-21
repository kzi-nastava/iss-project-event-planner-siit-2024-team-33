package rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user;

public class InvalidPasswordFormatException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public InvalidPasswordFormatException(String message) {
		super(message);
	}
}
