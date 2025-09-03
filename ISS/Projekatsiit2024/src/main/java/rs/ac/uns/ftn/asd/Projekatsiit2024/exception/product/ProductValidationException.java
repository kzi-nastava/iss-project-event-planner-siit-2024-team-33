package rs.ac.uns.ftn.asd.Projekatsiit2024.exception.product;

public class ProductValidationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ProductValidationException(String message) {
		super(message);
	}
}
