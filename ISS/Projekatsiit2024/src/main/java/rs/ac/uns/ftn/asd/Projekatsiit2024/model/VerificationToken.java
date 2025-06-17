package rs.ac.uns.ftn.asd.Projekatsiit2024.model;

import jakarta.persistence.*;
import java.util.Date;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;

@Entity
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private Date expirationDate;

    @OneToOne
    private AuthentifiedUser user;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public AuthentifiedUser getUser() {
		return user;
	}

	public void setUser(AuthentifiedUser user) {
		this.user = user;
	}

    
    
}
