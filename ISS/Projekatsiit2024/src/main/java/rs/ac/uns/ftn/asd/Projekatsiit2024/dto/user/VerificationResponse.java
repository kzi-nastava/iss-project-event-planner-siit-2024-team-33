package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationResponse {
	private String message;
	
	public VerificationResponse(String message) {
		this.message = message;	
	}
}
