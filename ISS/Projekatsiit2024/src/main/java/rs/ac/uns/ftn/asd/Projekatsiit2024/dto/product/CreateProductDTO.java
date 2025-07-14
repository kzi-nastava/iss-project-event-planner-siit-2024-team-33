package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Rating;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;

public class CreateProductDTO {
	public String Name;
	public String Description;
	public Double Price;
	public Double Discount;
	public List<String> Pictures;
	public Availability Availability;
    public Boolean IsPending;
    public OfferCategory Category;
    public Provider Provider;
    public List<EventType> ValidEvents;
}
