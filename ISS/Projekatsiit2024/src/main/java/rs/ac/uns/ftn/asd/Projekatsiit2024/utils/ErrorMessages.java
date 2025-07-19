package rs.ac.uns.ftn.asd.Projekatsiit2024.utils;

public class ErrorMessages {
	private String message;
	private String errorCode;
	
	public ErrorMessages() {
	}

	public ErrorMessages(String message) {
		this.message = message;
	}
	
	public ErrorMessages(String message, String errorCode) {
		this.message = message;
		this.setErrorCode(errorCode);
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
