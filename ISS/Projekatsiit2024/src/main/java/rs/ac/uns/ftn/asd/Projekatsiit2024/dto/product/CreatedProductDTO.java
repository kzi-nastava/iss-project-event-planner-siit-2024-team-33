package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Rating;

public class CreatedProductDTO {
	public Integer Id;
	public String Name;
	public String Description;
	public Double Price;
	public Double Discount;
	public List<String> Pictures;
	public Availability Availability;
    public Date CreationDate;
    public Boolean IsPending;
    public Boolean IsDeleted;
    public OfferCategory Category;
    public Provider Provider;
    public List<EventType> ValidEvents;
    public List<OfferReservation> OfferReservations;
    public List<Rating> Ratings;
}
