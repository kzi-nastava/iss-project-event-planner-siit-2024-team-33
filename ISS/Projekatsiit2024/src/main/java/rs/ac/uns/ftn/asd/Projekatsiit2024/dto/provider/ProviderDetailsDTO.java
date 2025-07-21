package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.provider;

import java.util.List;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.utils.ImageManager;

public class ProviderDetailsDTO {
	public Integer id;
	public String email;
    public String name;
    public String surname;
    public String description;
    public String phoneNumber;
    public String providerName;
    public String residency;
    public List<String> picturesDataURI;
    
    public ProviderDetailsDTO(Provider p) {
    	this.id = p.getId();
    	this.email = p.getEmail();
    	this.name = p.getName();
    	this.surname = p.getSurname();
    	this.description = p.getDescription();
    	this.phoneNumber = p.getPhoneNumber();
    	this.providerName = p.getProviderName();
    	this.residency = p.getResidency();
    	this.picturesDataURI = p.getPictures().stream().map(imgPath -> ImageManager.loadAsDataURI(imgPath)).toList();
    }
}
