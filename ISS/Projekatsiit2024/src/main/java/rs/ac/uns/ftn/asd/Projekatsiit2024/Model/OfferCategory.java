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
	private Integer id;
    private String name;
    private String description;
    private Boolean isAccepted;
    private Boolean isEnabled;
    
    public OfferCategory(String name, String description, Boolean isAccepted, Boolean isEnabled) {
		this.name = name;
		this.description = description;
		this.isAccepted = isAccepted;
		this.isEnabled = isEnabled;
	}
    
    public OfferCategory() {}

    @OneToMany(mappedBy = "category")
    private List<Offer> offers;
}
