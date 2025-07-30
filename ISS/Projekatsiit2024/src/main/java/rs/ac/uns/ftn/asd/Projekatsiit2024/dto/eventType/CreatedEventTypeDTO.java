package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;

@Getter
@Setter
public class CreatedEventTypeDTO {
	private Integer id;
	private String name;
	private String description;
	private Boolean isActive;
    private Set<Integer> recommendedCategories;
    
    public CreatedEventTypeDTO(EventType eventType) {
    	this.setId(eventType.getId());
    	this.setName(eventType.getName());
    	this.setDescription(eventType.getDescription());
    	this.setIsActive(eventType.getIsActive());
    	this.setRecommendedCategories(eventType.getRecommendedCategories().stream()
    			.map(OfferCategory::getId).collect(Collectors.toSet()));
    }
}
