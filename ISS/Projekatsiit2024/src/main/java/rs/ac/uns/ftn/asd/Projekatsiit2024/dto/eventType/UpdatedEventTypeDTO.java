package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.OfferCategory;

@Getter
@Setter
public class UpdatedEventTypeDTO {
	private Integer id;
	private String description;
    private List<OfferCategory> recommendedCategories;
    
    public UpdatedEventTypeDTO(EventType eventType) {
    	this.setId(eventType.getId());
    	this.setDescription(eventType.getDescription());
    	this.setRecommendedCategories(eventType.getRecommendedCategories());
    }
}
