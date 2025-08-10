package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupUserDTO {
    private String email;
    private String name;
    private String surname;
    private String password;
    private String confirmPassword;
    private String picture;
}
