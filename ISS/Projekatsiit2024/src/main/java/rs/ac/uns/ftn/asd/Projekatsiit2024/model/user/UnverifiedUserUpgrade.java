package rs.ac.uns.ftn.asd.Projekatsiit2024.model.user;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.Role;

@Entity
@Getter
@Setter
public class UnverifiedUserUpgrade {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String residency;
	private String phoneNumber;
	private String providerName;
    private String description;
    private LocalDateTime dateOfCreation;
    @ManyToOne
    private Role role;
    @OneToOne
    private AuthentifiedUser aUser;
}
