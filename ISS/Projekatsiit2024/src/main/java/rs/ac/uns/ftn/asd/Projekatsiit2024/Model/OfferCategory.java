package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class OfferCategory
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer ID;
    public String Name;
    public String Description;
    public Boolean IsAccepted;
    public Boolean IsEnabled;

    @OneToMany(mappedBy = "Category")
    public List<Offer> Offers;
}
