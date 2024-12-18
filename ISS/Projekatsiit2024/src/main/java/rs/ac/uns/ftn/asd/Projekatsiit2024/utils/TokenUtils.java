package rs.ac.uns.ftn.asd.Projekatsiit2024.utils;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.User;

@Component
public class TokenUtils {
	
	@Value("event-planner")
	private String APP_NAME;
	
	@Value("mysecret")
	public String SECRET;
	
	@Value("1800000")
	private int EXPIRES_IN;
	
	@Value("Authorization")
	private String AUTH_HEADER;
	
	//private static final String AUDIENCE_UNKNOWN = "unknown";
	//private static final String AUDIENCE_MOBILE = "mobile";
	//private static final String AUDIENCE_TABLET = "tablet";
	
	private static final String AUDIENCE_WEB = "web";
	
	private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
	
	
	public String generateToken(User user) {
		return Jwts.builder()
				.setIssuer(APP_NAME)
				.setSubject(user.getUsername())
				.setAudience(generateAudience())
				.setIssuedAt(new Date())
				.claim("role", user.getRole().toString())
				.setExpiration(generateExpirationDate())
				.signWith(SIGNATURE_ALGORITHM, SECRET).compact();
	}
	
	private String generateAudience() {
		
		//	Moze se iskoristiti org.springframework.mobile.device.Device objekat za odredjivanje tipa uredjaja sa kojeg je zahtev stigao.
		//	https://spring.io/projects/spring-mobile
				
		//	String audience = AUDIENCE_UNKNOWN;
		//		if (device.isNormal()) {
		//			audience = AUDIENCE_WEB;
		//		} else if (device.isTablet()) {
		//			audience = AUDIENCE_TABLET;
		//		} else if (device.isMobile()) {
		//			audience = AUDIENCE_MOBILE;
		//		}
		
		return AUDIENCE_WEB;
	}
	
	private Date generateExpirationDate() {
		return new Date(new Date().getTime() + EXPIRES_IN);
	}
	
	
	public String getToken(HttpServletRequest request) {
		String authHeader = getAuthHeaderFromHeader(request);
		// JWT se prosledjuje kroz header 'Authorization' u formatu:
		// Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
		
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7); // preuzimamo samo token (vrednost tokena je nakon "Bearer " prefiksa)
		}

		return null;
	}
	
	public String getUsernameFromToken(String token) {
		String username;
		
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			username = claims.getSubject();
		}
		catch(ExpiredJwtException ex) {
			throw ex;
		}
		catch(Exception e) {
			username = null;
		}
		
		return username;
	}
	
	public Date getIssuedAtDateFromToken(String token) {
		Date issueAt;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			issueAt = claims.getIssuedAt();
		}
		catch (ExpiredJwtException ex) {
			throw ex;
		}
		catch(Exception e) {
			issueAt = null;
		}
		
		return issueAt;
	}
	
	private Claims getAllClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		}
		catch(ExpiredJwtException ex) {
			throw ex;
		}
		catch(Exception e) {
			claims = null;
		}
		
		return claims;
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		User user = (User) userDetails;
		final String username = getUsernameFromToken(token);
		final Date created = getIssuedAtDateFromToken(token);
		
		return (username != null 
				&& username.equals(userDetails.getUsername()) 
				&& !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate()));
	}
	
	private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return (lastPasswordReset != null && created.before(lastPasswordReset));
	}
	
	public String getAuthHeaderFromHeader(HttpServletRequest request) {
		return request.getHeader(AUTH_HEADER);
	}
	
	public int getExpiredIn() {
		return EXPIRES_IN;
	}
}
