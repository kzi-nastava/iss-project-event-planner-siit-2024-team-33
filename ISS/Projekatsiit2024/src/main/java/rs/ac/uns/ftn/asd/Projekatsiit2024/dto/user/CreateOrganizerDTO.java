package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrganizerDTO extends CreateUserDTO {
	private String picture;
	private String residency;
	private String phoneNumber;
}
