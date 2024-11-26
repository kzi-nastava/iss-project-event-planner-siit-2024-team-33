package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Provider extends AuthentifiedUser
{
    public String Description;
    public String PhoneNumber;
    public String ProviderName;
    public String Residency;

    @OneToMany(mappedBy = "Provider")
    public List<Offer> Offers;
}