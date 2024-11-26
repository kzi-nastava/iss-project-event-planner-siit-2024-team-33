package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Event
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer ID;
    public String Name ;
    public String Description ;
    public int NumOfAttendees ;
    public Boolean IsPrivate ;
    public String Place ;
    public Double Latitude;
    public Double Longitude;
    public Date DateOfEvent;
    
    @ManyToMany
    public List<EventType> EventTypes;
    @OneToMany(mappedBy = "Event")
    public List<OfferReservation> Reservations;
    @OneToMany(mappedBy = "Event")
    public List<BudgetItem> BudgetItems;
}