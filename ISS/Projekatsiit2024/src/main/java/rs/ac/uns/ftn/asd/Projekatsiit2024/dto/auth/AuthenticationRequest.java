package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {
	private String email;
	private String password;
	
	public AuthenticationRequest() {
		super();
	}
	
	public AuthenticationRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
