package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

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
	public String Picture;
	public String Residency;
	public String PhoneNumber;
	
    
    @OneToMany(mappedBy = "organizer")
    private List<Event> organizedEvents;
}
