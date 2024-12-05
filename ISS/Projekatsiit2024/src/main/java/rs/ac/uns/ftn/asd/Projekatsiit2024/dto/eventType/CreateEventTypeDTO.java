package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEventTypeDTO {
	private String name;
	private String description;
	private Boolean isActive;
    private List<Integer> recommendedCategoriesIds;
}
