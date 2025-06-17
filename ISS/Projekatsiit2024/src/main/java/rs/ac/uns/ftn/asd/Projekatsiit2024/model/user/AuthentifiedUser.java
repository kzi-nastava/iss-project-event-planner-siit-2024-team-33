package rs.ac.uns.ftn.asd.Projekatsiit2024.model.user;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Notification;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.Role;

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
    private String city;
    private String picture;
    private Boolean isDeleted;
    private Boolean isVerified;
    private Date suspensionEndDate; //added for reports
    @ManyToOne
    private Role role;
    @Column(name = "last_password_reset_date")
    private Timestamp lastPasswordResetDate;

    @ManyToMany
    private List<Offer> favoriteOffers;
    @ManyToMany
    private List<Event> favoriteEvents;
    @ManyToMany
    private List<AuthentifiedUser> blockedUsers;
    @OneToMany(mappedBy = "receiver")
    private List<Notification> notifications;
}