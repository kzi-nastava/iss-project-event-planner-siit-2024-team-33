package rs.ac.uns.ftn.asd.Projekatsiit2024.model.event;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.EventRating;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;


@Entity
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Integer numOfAttendees;
    private Boolean isPrivate;
    private String place;
    private LocalDateTime dateOfEvent;
    private LocalDateTime endOfEvent;
    
    //for maps
    private Double latitude;
    private Double longitude;
    
    //event logistics
    @ManyToOne
    private Organizer organizer;
    @ManyToOne
    private EventType eventType;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private Set<EventActivity> eventActivities = new HashSet<>();
    
    @ManyToMany
    private Set<AuthentifiedUser> listOfAttendees = new HashSet<>();
    @OneToMany(mappedBy = "event")
    private Set<Invitation> privateInvitations = new HashSet<>();
    
    @OneToMany(mappedBy = "event")
    private Set<EventRating> eventRatings = new HashSet<>();

    //event reservations
    @OneToMany(mappedBy = "event")
    private List<OfferReservation> reservations;
    @OneToMany(mappedBy = "event")
    private List<BudgetItem> budgetItems;
    
    public boolean isOver() {
    	return LocalDateTime.now().isAfter(this.getEndOfEvent());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return this.getId() == event.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}

