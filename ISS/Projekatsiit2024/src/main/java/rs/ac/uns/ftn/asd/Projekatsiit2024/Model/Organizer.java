package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Organizer extends AuthentifiedUser {
	public String Picture;
	public String Residency;
	public String PhoneNumber;
}
