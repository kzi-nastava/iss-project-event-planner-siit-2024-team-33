package rs.ac.uns.ftn.asd.Projekatsiit2024.security.auth;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2024.utils.TokenUtils;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
	private TokenUtils tokenUtils;
	
	private UserDetailsService userDetailsService;
	
	protected final Log LOGGER = LogFactory.getLog(getClass());
	
	public TokenAuthenticationFilter(TokenUtils tokenHelper, UserDetailsService userDetailsService) {
		this.tokenUtils = tokenHelper;
		this.userDetailsService = userDetailsService;
	}
	
	
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		String username;
		
		String authToken = tokenUtils.getToken(request);
		
		
		try {
			if (authToken != null && !authToken.equals("")) {
				username = tokenUtils.getUsernameFromToken(authToken);
				
				if (username != null) {
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					
					//account check
					if (userDetails.isEnabled() || 
					    userDetails.isAccountNonLocked() || 
					    userDetails.isAccountNonExpired() || 
					    userDetails.isCredentialsNonExpired()) {
						if (tokenUtils.validateToken(authToken, userDetails)) {
							
							TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
							authentication.setToken(authToken);
							SecurityContextHolder.getContext().setAuthentication(authentication);
						}
					}
					else {
						LOGGER.warn("User account state invalid.");
					}
				}
			}
		}
		catch(ExpiredJwtException ex) {
			LOGGER.debug("Token expired!");
		}
		chain.doFilter(request, response);
	}
}