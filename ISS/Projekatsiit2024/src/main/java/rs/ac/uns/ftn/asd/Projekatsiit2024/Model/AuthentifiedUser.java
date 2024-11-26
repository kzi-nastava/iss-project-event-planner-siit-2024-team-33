package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.UniqueConstraint;

public class AuthentifiedUser
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer ID;
	@Column(unique = true)
    public String Email;
    public String Password;
    public String Name;
    public String Surname;
    public List<String> Pictures;

    @ManyToMany
    public List<Offer> FavoriteOffers;
    @ManyToMany
    public List<Event> FavoriteEvents;
    @ManyToMany
    public List<AuthentifiedUser> BlockedUsers;
    @OneToMany(mappedBy = "Receiver")
    public List<Notification> Notifications;
}
