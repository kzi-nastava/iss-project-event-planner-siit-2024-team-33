package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.auth;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUser {
	private String email;
	private String password;
	private String name;
	private String surname;
	private String picture;
	private List<String> pictures;
	private String residency;
	private String phoneNumber;
	private String description;
	private String providerName;
	private String role;
}
