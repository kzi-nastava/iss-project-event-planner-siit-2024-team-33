package rs.ac.uns.ftn.asd.Projekatsiit2024.exception.reportPDF;

public class PdfGenerationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public PdfGenerationException(String message) {
		super(message);
	}
}
