package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserDTO {
	private Integer Id;
	private String Email;
	private String Password;
	private String Name;
	private String Surname;
	private List<String> Pictures;
}
