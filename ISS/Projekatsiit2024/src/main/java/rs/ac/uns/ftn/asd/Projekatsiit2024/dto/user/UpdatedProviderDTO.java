package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatedProviderDTO extends UpdatedUserDTO {
	private String Description;
	private String PhoneNumber;
	private String ProviderName;
	private String Residency;
}
