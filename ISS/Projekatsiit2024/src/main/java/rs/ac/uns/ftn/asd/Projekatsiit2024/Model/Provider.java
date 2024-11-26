package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.util.List;

import jakarta.persistence.OneToMany;

public class Provider
{
    public String Description;
    public String PhoneNumber;
    public String ProviderName;
    public String Residency;

    @OneToMany(mappedBy = "Provider")
    public List<Offer> Offers;
}