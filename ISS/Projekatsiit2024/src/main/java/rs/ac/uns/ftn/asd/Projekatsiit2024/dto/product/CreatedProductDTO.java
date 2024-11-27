package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Availability;

public class CreatedProductDTO {
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
}
