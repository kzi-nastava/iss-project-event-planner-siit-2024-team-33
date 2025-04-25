package rs.ac.uns.ftn.asd.Projekatsiit2024.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Provider extends AuthentifiedUser {
    private String description;
    private String phoneNumber;
    private String providerName;
    private String residency;

    @OneToMany(mappedBy = "provider")
    private List<Offer> offers;

    @OneToMany(mappedBy = "provider")
    private List<ProviderRating> ratings;
}
