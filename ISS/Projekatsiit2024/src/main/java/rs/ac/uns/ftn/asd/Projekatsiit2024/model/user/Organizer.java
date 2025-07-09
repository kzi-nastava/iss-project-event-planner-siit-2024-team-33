package rs.ac.uns.ftn.asd.Projekatsiit2024.model.user;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;

@Entity
@Getter
@Setter
public class Organizer extends AuthentifiedUser {
	private String residency;
	private String phoneNumber;
    
    @OneToMany(mappedBy = "organizer")
    private Set<Event> organizedEvents;
    
    public void addEvent(Event event) {
    	if (this.organizedEvents == null) {
    		this.organizedEvents = new HashSet<>();
    	}
        this.organizedEvents.add(event);
    }
}
