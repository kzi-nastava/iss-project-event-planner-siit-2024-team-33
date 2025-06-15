package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUser {
	private String name;
	private String surname;
	private String picture;
	private String residency;
	private String phoneNumber;
	private String description;
	private List<String> pictures;
	private String providerName;
	
	public UpdateUser() {
	}
}
