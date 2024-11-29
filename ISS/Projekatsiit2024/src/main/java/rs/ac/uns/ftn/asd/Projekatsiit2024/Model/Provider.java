package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Provider extends AuthentifiedUser {
    private String description;
    private String phoneNumber;
    private String providerName;
    private String residency;

    @OneToMany(mappedBy = "provider")
    private List<Offer> offers;

}
