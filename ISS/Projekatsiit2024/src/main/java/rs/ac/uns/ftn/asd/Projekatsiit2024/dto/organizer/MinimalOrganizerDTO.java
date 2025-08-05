package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.organizer;

import lombok.Getter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;

@Getter
public class MinimalOrganizerDTO {
	private String email;
	private String name;
	private String surname;
	
	public MinimalOrganizerDTO(AuthentifiedUser organizer) {
		this.email = organizer.getEmail();
		this.name = organizer.getName();
		this.surname = organizer.getSurname();
	}
    public MinimalOrganizerDTO() {}

}
