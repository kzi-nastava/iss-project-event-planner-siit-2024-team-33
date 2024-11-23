package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.sql.Date;
import java.util.List;

public class Offer
{
    public int ID ;
    public int OfferID ;
    public String Name ;
    public String Description ;
    public int Price ;
    public int Discount ;
    public List<String> Pictures;
    public Availability Availability ;
    public Date CreationDate ;
    public Boolean IsPending ;
    public Provider Provider ; 
    public List<OfferReservation> OfferReservations; 
    public List<Rating> Ratings; 
}
