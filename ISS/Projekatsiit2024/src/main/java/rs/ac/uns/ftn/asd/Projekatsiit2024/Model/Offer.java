package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Offer
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    public int ID;
    public int OfferID;
    public String Name;
    public String Description;
    public Double Price;
    public Double Discount;
    public List<String> Pictures;
    @Enumerated(EnumType.STRING)
    public Availability Availability;
    public Date CreationDate;
    public Boolean IsPending;
    public Boolean IsDeleted;
    
    @ManyToOne
    public OfferCategory Category;
    @ManyToOne
    public Provider Provider;
    @ManyToMany
    public List<EventType> ValidEvents;
    @OneToMany(mappedBy = "Offer")
    public List<OfferReservation> OfferReservations;
    @OneToMany(mappedBy = "Offer")
    public List<Rating> Ratings; 
}
