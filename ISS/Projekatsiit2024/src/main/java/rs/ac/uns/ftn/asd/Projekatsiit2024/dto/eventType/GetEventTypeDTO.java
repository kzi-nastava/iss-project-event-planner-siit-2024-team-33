package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferCategory;

@Getter
@Setter
public class GetEventTypeDTO {
	private Integer id;
	private String name;
	private String description;
	private Boolean isActive;
    private List<OfferCategory> recommendedCategories;
    
    public GetEventTypeDTO(EventType eventType) {
    	this.setId(eventType.getId());
    	this.setName(eventType.getName());
    	this.setDescription(eventType.getDescription());
    	this.setIsActive(eventType.getIsActive());
    	this.setRecommendedCategories(eventType.getRecommendedCategories());
    }
}
