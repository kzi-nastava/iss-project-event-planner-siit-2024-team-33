package rs.ac.uns.ftn.asd.Projekatsiit2024.Service;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	private String superSecretKey = "4cf00a03894691b13c8078639de05bc261420a67257a3f496e00db896cf073d2";
	
	public String generateToken(UserDetails ud) {
		return Jwts.builder()
				.setSubject(ud.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.signWith(getKey(), SignatureAlgorithm.ES256)
				.compact();
	}
	
	public String extractUsername(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	public Boolean isTokenValid(String token, UserDetails ud) {
		return extractUsername(token) == ud.getUsername();
	}
	
	private Key getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(superSecretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
