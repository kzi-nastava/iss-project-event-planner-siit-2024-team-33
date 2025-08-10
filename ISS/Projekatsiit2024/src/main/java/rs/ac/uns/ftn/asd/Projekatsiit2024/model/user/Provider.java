package rs.ac.uns.ftn.asd.Projekatsiit2024.model.user;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.ProviderRating;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;

@Entity
@Getter
@Setter
public class Provider extends AuthentifiedUser {
    private String residency;
    private String phoneNumber;
    private String providerName;
    private String description;
    private List<String> pictures = new ArrayList<>();

    @OneToMany(mappedBy = "provider")
    private List<Offer> offers = new ArrayList<>();

    @OneToMany(mappedBy = "provider")
    private List<ProviderRating> ratings = new ArrayList<>();
}
