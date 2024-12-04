package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProviderDTO extends CreateUserDTO {
	private String description;
	private String phoneNumber;
	private String providerName;
	private String residency;
}
