package rs.ac.uns.ftn.asd.Projekatsiit2024.model.user;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Event;

@Entity
@Getter
@Setter
public class Organizer extends AuthentifiedUser {
	private String residency;
	private String phoneNumber;
    
    @OneToMany(mappedBy = "organizer")
    private List<Event> organizedEvents;
}
