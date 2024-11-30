package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class OfferCategory
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
    public String name;
    public String description;
    public Boolean isAccepted;
    public Boolean isEnabled;

    @OneToMany(mappedBy = "category")
    private List<Offer> offers;
}
