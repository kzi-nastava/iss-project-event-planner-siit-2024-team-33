package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.util.List;
import jakarta.persistence.*;

@Entity
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

    @ElementCollection
    private List<String> pictures;

    private Boolean isDeleted;

    @ManyToMany
    private List<Offer> favoriteOffers;

    @ManyToMany
    private List<Event> favoriteEvents;

    @ManyToMany
    private List<AuthentifiedUser> blockedUsers;

    @OneToMany(mappedBy = "receiver")
    private List<Notification> notifications;

}
