package rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user;

public class UserCreationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserCreationException(String message) {
        super(message);
    }
}
