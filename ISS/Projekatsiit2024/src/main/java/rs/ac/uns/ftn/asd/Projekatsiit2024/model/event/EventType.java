package rs.ac.uns.ftn.asd.Projekatsiit2024.model.event;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;

@Entity
@Setter
@Getter
public class EventType
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(unique = true)
	private String name;
	private String description;
	private Boolean isActive;
    
    @ManyToMany
    private Set<OfferCategory> recommendedCategories = new HashSet<>();
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventType)) return false;
        EventType et = (EventType) o;
        return this.getId() == et.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}