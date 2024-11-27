package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class CreateUserDTO {
	public Integer ID;
    public String Email;
    public String Password;
    public String Name;
    public String Surname;
    public List<String> Pictures;
}
