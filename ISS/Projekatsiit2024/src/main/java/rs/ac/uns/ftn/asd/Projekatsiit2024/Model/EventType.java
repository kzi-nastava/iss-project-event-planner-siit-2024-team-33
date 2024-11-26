package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.util.List;

import jakarta.persistence.ManyToMany;

public class EventType
{
    public String Name ;
    public String Description ;
    public Boolean IsActive;
    
    @ManyToMany
    public List<OfferCategory> RecommendedCategories;
}