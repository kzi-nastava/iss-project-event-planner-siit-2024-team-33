package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatedOrganizerDTO extends UpdatedUserDTO {
	private String Picture;
	private String Residency;
	private String PhoneNumber;
}
