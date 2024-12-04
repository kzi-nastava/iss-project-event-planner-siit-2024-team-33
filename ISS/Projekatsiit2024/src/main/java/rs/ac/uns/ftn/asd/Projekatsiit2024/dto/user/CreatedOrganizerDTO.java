package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Organizer;

@Getter
@Setter
public class CreatedOrganizerDTO extends CreatedUserDTO {
	private String picture;
	private String residency;
	private String phoneNumber;
	
	public CreatedOrganizerDTO(Organizer organizer) {
		super.setId(organizer.getId());
		super.setEmail(organizer.getEmail());
		super.setEmail(organizer.getEmail());
		super.setPassword(organizer.getPassword());
		super.setName(organizer.getName());
		super.setSurname(organizer.getSurname());
		this.setPicture(organizer.getPicture());
		this.setResidency(organizer.getResidency());
		this.setPhoneNumber(organizer.getPhoneNumber());
	}
}
