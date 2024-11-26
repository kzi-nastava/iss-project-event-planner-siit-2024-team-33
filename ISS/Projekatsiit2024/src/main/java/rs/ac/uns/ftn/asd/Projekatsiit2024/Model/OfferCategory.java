package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.util.List;

import jakarta.persistence.OneToMany;

public class OfferCategory
{
    public String Name;
    public String Description;
    public Boolean IsAccepted;
    public Boolean IsEnabled;

    @OneToMany(mappedBy = "Category")
    public List<Offer> Offers;
}
