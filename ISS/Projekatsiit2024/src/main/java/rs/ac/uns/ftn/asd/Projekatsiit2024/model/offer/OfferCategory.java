package rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	@Column(unique = true)
    private String name;
    private String description;
    private Boolean isAccepted;
    private Boolean isEnabled;
    @Enumerated(EnumType.STRING)
    private OfferType offerType;
    
    @OneToMany(mappedBy = "category")
    private List<Offer> offers;
    
    
    
    
    
    
    
    
    public OfferCategory(String name, String description, Boolean isAccepted, Boolean isEnabled) {
		this.name = name;
		this.description = description;
		this.isAccepted = isAccepted;
		this.isEnabled = isEnabled;
	}
    
    public OfferCategory() {}
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfferCategory)) return false;
        OfferCategory oc = (OfferCategory) o;
        return this.getId() == oc.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
