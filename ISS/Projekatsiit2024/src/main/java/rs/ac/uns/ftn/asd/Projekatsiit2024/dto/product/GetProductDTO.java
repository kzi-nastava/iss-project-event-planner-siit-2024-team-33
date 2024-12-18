package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product;

import java.sql.Date;
import java.util.List;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Rating;

public class GetProductDTO {
	private Integer Id;
	private String Name;
	private String Description;
	private Double Price;
	private Double Discount;
	private List<String> Pictures;
	public Availability Availability;
    public Date CreationDate;
    public Boolean IsPending;
    public Boolean IsDeleted;
    public OfferCategory Category;
    public Provider Provider;
    public List<EventType> ValidEvents;
    public List<OfferReservation> OfferReservations;
    public List<Rating> Ratings; 
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Double getPrice() {
		return Price;
	}
	public void setPrice(Double price) {
		Price = price;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public Double getDiscount() {
		return Discount;
	}
	public void setDiscount(Double discount) {
		Discount = discount;
	}
	public List<String> getPictures() {
		return Pictures;
	}
	public void setPictures(List<String> pictures) {
		Pictures = pictures;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
}
