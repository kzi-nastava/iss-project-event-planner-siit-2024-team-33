package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {
	private String Password;
	private String Name;
	private String Surname;
	private List<String> Pictures;
}
