package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePassword {
	private String oldPassword;
	private String newPassword;
}
