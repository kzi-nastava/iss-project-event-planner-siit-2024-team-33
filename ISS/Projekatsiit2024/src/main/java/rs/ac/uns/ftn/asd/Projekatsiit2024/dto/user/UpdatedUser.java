package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;

@Getter
@Setter
public class UpdatedUser {
	private String email;
	private String name;
	private String surname;
	private String picture;
	private String city;
	private String residency;
	private String phoneNumber;
	private String description;
	private String providerName;
	private List<String> pictures;
	
	public UpdatedUser() {
	}
	
	public UpdatedUser(AuthentifiedUser aUser) {
		this.setEmail(aUser.getEmail());
		this.setName(aUser.getName());
		this.setSurname(aUser.getSurname());
		this.setPicture(aUser.getPicture());
	}
	
	public UpdatedUser(Organizer organizer) {
		this.setEmail(organizer.getEmail());
		this.setName(organizer.getName());
		this.setSurname(organizer.getSurname());
		this.setPicture(organizer.getPicture());
		this.setCity(organizer.getCity());
		this.setResidency(organizer.getResidency());
		this.setPhoneNumber(organizer.getPhoneNumber());
	}
	
	public UpdatedUser(Provider provider) {
		this.setEmail(provider.getEmail());
		this.setName(provider.getName());
		this.setSurname(provider.getSurname());
		this.setPicture(provider.getPicture());
		this.setCity(provider.getCity());
		this.setResidency(provider.getResidency());
		this.setPhoneNumber(provider.getPhoneNumber());
		this.setDescription(provider.getDescription());
		this.setProviderName(provider.getProviderName());
		this.setPictures(provider.getPictures());
	}
}
