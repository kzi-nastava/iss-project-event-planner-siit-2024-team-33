package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Notification;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Offer;

public class CreatedUserDTO {
	public Integer Id;
    public String Email;
    public String Password;
    public String Name;
    public String Surname;
    public List<String> Pictures;
}
