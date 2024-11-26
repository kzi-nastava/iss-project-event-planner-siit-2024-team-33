package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class EventType
{
	@Id
    public String Name ;
    public String Description ;
    public Boolean IsActive;
    
    @ManyToMany
    public List<OfferCategory> RecommendedCategories;
}