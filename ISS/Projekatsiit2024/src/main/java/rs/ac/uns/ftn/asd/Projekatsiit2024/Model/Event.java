package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private int numOfAttendees;
    private Boolean isPrivate;
    private String place;
    private Double latitude;
    private Double longitude;
    private LocalDateTime dateOfEvent;
    private LocalDateTime endOfEvent;
    private Boolean itsJoever;
    private String picture;
    private Integer price;
    
    
    @ManyToOne
    private Organizer organizer;
    
    @ManyToMany
    private List<EventType> eventTypes;

    @OneToMany(mappedBy = "event")
    private List<OfferReservation> reservations;

    @OneToMany(mappedBy = "event")
    private List<BudgetItem> budgetItems;
}

