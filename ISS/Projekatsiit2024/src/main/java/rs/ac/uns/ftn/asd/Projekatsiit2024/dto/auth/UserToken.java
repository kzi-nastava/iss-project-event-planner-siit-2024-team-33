package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserToken {
	
	private String accessToken;
	private Long expiresIn;
	
	public UserToken() {
		this.accessToken = null;
		this.expiresIn = null;
	}
	
	public UserToken(String accessToken, long expiresIn) {
		this.accessToken = accessToken;
		this.expiresIn = expiresIn;
	}
}
