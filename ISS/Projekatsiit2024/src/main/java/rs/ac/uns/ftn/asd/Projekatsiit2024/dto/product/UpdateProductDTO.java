package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product;

import java.sql.Date;
import java.util.List;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Rating;

public class UpdateProductDTO {
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
