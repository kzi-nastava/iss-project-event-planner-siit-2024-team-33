package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDTO {
	private String email;
	private String password;
	private String name;
	private String surname;
	private List<String> pictures;
}
