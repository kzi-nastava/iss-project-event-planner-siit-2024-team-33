package rs.ac.uns.ftn.asd.Projekatsiit2024.model.user;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.Role;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.communication.Notification;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class AuthentifiedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    private String surname;
    private String picture;
    private Boolean isDeleted;
    private Boolean isVerified;
    private LocalDateTime suspensionEndDate; //added for reports
    private LocalDateTime dateOfCreation;
    @ManyToOne
    private Role role;
    @Column(name = "last_password_reset_date", columnDefinition = "TIMESTAMP(0)")
    private Timestamp lastPasswordResetDate;

    @ManyToMany
    private Set<Offer> favoriteOffers = new HashSet<>();
    @ManyToMany
    private Set<Event> favoriteEvents = new HashSet<>();
    
    @ManyToMany(mappedBy = "listOfAttendees")
    private Set<Event> joinedEvents = new HashSet<>();
    
    
    @ManyToMany
    private List<AuthentifiedUser> blockedUsers;
    @OneToMany(mappedBy = "receiver")
    private List<Notification> notifications;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthentifiedUser)) return false;
        AuthentifiedUser aUser = (AuthentifiedUser) o;
        return this.getId() == aUser.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}