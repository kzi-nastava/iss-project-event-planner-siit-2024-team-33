package rs.ac.uns.ftn.asd.Projekatsiit2024.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;

@Entity
@Setter
@Getter
public class EventRating {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer ratingValue;
    private String comment; 
    private Boolean accepted;
    private Boolean isDeleted;

    @ManyToOne
    private AuthentifiedUser author;
    
    @ManyToOne
    private Event event;
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventRating)) return false;
        EventRating eventRating = (EventRating) o;
        return this.getId() == eventRating.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
    
}
