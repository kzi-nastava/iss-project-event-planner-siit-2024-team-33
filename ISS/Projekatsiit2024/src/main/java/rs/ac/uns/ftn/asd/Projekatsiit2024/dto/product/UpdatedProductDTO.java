package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product;

import java.sql.Date;
import java.util.List;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Rating;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;

public class UpdatedProductDTO {
	public Integer Id;
	public String Name;
	public String Description;
	public Double Price;
	public Double Discount;
	public List<String> Pictures;
	public Availability Availability;
    public Boolean IsPending;
    public OfferCategory Category;
    public Provider Provider;
}
