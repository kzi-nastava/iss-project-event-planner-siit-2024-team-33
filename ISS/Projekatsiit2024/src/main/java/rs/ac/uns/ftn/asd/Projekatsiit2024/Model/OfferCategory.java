package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class OfferCategory
{
	@Id
    public String Name;
    public String Description;
    public Boolean IsAccepted;
    public Boolean IsEnabled;

    @OneToMany(mappedBy = "Category")
    public List<Offer> Offers;
}
