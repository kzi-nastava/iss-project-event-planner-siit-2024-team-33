package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.sql.Date;
import java.util.List;

public class Offer
{
    public int ID;
    public int OfferID;
    public String Name;
    public String Description;
    public Double Price;
    public Double Discount;
    public List<String> Pictures;
    public Availability Availability;
    public Date CreationDate;
    public Boolean IsPending;
    
    public OfferCategory Category;
    public Provider Provider;
    public List<EventType> ValidEvents;
    public List<OfferReservation> OfferReservations;
    public List<Rating> Ratings; 
}
