package rs.ac.uns.ftn.asd.Projekatsiit2024.model;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "spring_users")
public class User implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String password;
    
    @Column(name = "last_password_reset_date")
    private Timestamp lastPasswordResetDate;
    
    @ManyToOne
    private Role role;
    
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	if (role == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(role);
    }
    
    @Override
    public String getUsername() {
        return email;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public Role getRole() {
       return role;
    }
    
    public void setUsername(String username) {
        this.email = username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
