package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Admin;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.utils.ImageManager;

@Getter
@Setter
public class GetUserDTO {
	private String email;
	private String password;
	private String name;
	private String surname;
	private String picture;
	private String residency;
    private String phoneNumber;
    private String providerName;
    private String description;
    private List<String> pictures;
    private String roleName;

    public static GetUserDTO from(AuthentifiedUser user) {
        if (user instanceof Provider) return new GetUserDTO((Provider) user);
        if (user instanceof Organizer) return new GetUserDTO((Organizer) user);
        if (user instanceof Admin) return new GetUserDTO((Admin) user);
        return new GetUserDTO(user);
    }
	
	
	public GetUserDTO() {
	}
	
	public GetUserDTO(AuthentifiedUser au) {
		this.setEmail(au.getEmail());
		this.setPassword(au.getPassword());
		this.setName(au.getName());
		this.setSurname(au.getSurname());
		this.setPicture(ImageManager.loadAsDataURI(au.getPicture()));
		this.setRoleName(au.getRole().getName());
	}
	
	public GetUserDTO(Admin admin) {
		this.setEmail(admin.getEmail());
		this.setPassword(admin.getPassword());
		this.setName(admin.getName());
		this.setSurname(admin.getSurname());
		this.setPicture(ImageManager.loadAsDataURI(admin.getPicture()));
		this.setRoleName(admin.getRole().getName());
	}
	
	public GetUserDTO(Organizer organizer) {
		this.setEmail(organizer.getEmail());
		this.setPassword(organizer.getPassword());
		this.setName(organizer.getName());
		this.setSurname(organizer.getSurname());
		this.setPicture(ImageManager.loadAsDataURI(organizer.getPicture()));
		this.setResidency(organizer.getResidency());
		this.setPhoneNumber(organizer.getPhoneNumber());
		this.setRoleName(organizer.getRole().getName());
	}
	
	public GetUserDTO(Provider provider) {
		this.setEmail(provider.getEmail());
		this.setPassword(provider.getPassword());
		this.setName(provider.getName());
		this.setSurname(provider.getSurname());
		this.setPicture(ImageManager.loadAsDataURI(provider.getPicture()));
		this.setResidency(provider.getResidency());
		this.setPhoneNumber(provider.getPhoneNumber());
		this.setProviderName(provider.getProviderName());
		this.setDescription(provider.getDescription());
		if (provider.getPictures() == null)
			provider.setPictures(new ArrayList<>());
		this.setPictures(
			    provider.getPictures().stream()
			        .map(ImageManager::loadAsDataURI)
			        .filter(img -> img != null)
			        .toList()
			);
		this.setRoleName(provider.getRole().getName());
	}
}
