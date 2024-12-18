package rs.ac.uns.ftn.asd.Projekatsiit2024.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Entity
public class Organizer extends AuthentifiedUser {
	private String picture;
	private String residency;
	private String phoneNumber;
	
    
    @OneToMany(mappedBy = "organizer")
    private List<Event> organizedEvents;
}
