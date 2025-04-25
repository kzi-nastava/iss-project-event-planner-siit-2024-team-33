package rs.ac.uns.ftn.asd.Projekatsiit2024.model;

import java.sql.Date;
import java.util.List;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    
    private List<String> pictures;
    
    //Added for reports
    public Date suspensionEndDate;
    
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