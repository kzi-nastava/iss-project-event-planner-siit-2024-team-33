package rs.ac.uns.ftn.asd.Projekatsiit2024.security.auth;

import java.security.Principal;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class TokenBasedAuthentication extends AbstractAuthenticationToken implements Principal {
	
	private static final long serialVersionUID = 1L;
	
	private String token;
	private final UserDetails principal;
	
	public TokenBasedAuthentication(UserDetails principal) {
		super(principal.getAuthorities());
		this.principal = principal;
		super.setAuthenticated(true);
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	@Override
	public Object getCredentials() {
		return token;
	}
	
	@Override
	public UserDetails getPrincipal() {
		return principal;
	}
	
	@Override
	public String getName() {
	    return principle.getUsername();
	}
}
