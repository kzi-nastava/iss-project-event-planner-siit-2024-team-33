package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;

@Getter
@Setter
public class RegisteredUser extends RegisterUser {
	private Integer id;
	
	public RegisteredUser(Organizer organizer) {
		this.setId(organizer.getId());
		super.setEmail(organizer.getEmail());
		super.setPassword(organizer.getPassword());
		super.setName(organizer.getName());
		super.setSurname(organizer.getSurname());
		super.setPicture(organizer.getPicture());
		super.setResidency(organizer.getResidency());
		super.setPhoneNumber(organizer.getPhoneNumber());
		super.setRole(organizer.getRole().getName());
	}
	
	public RegisteredUser(Provider provider) {
		this.setId(provider.getId());
		super.setEmail(provider.getEmail());
		super.setPassword(provider.getPassword());
		super.setName(provider.getName());
		super.setSurname(provider.getSurname());
		super.setPicture(provider.getPicture());
		super.setPictures(provider.getPictures());
		super.setDescription(provider.getDescription());
		super.setPhoneNumber(provider.getPhoneNumber());
		super.setProviderName(provider.getProviderName());
		super.setResidency(provider.getResidency());
		super.setRole(provider.getRole().getName());
	}
	
	public RegisteredUser(AuthentifiedUser auser) {
		this.setId(auser.getId());
		super.setEmail(auser.getEmail());
		super.setPassword(auser.getPassword());
		super.setName(auser.getName());
		super.setSurname(auser.getSurname());
		super.setPicture(auser.getPicture());
		super.setRole(auser.getRole().getName());
	}
}
