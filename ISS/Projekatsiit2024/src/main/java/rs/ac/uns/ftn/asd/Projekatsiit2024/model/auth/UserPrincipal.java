package rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth;

import java.util.Collection;
import java.util.Collections;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;

public class UserPrincipal implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	private final AuthentifiedUser user;
	
	public UserPrincipal(AuthentifiedUser user) {
		this.user = user;
	}
	
    @Override
    public Collection<Role> getAuthorities() {
    	if (this.user.getRole() == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(this.user.getRole());
    }
    
    @Override
    public String getUsername() {
        return this.user.getEmail();
    }
    
    @Override
	public String getPassword() {
		return this.user.getPassword();
	}
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
    	Date now = Date.valueOf(LocalDate.now());
        return this.user.getSuspensionEndDate() == null || this.user.getSuspensionEndDate().before(now);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !this.user.getIsDeleted() && this.user.getIsVerified();
    }

	public Timestamp getLastPasswordResetDate() {
		return this.user.getLastPasswordResetDate();
	}
	
	@JsonIgnore
	public AuthentifiedUser getUser() {
		return this.user;
	}
}
