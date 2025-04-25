package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Provider;

@Getter
@Setter
public class CreatedProviderDTO extends CreatedUserDTO {
	private String description;
    private String phoneNumber;
    private String providerName;
    private String residency;
    
    public CreatedProviderDTO(Provider provider) {
    	super.setId(provider.getId());
		super.setEmail(provider.getEmail());
		super.setPassword(provider.getPassword());
		super.setName(provider.getName());
		super.setSurname(provider.getSurname());
		super.setPictures(provider.getPictures());
		this.setDescription(provider.getDescription());
		this.setPhoneNumber(provider.getPhoneNumber());
		this.setProviderName(provider.getProviderName());
		this.setResidency(provider.getResidency());
    }
}
