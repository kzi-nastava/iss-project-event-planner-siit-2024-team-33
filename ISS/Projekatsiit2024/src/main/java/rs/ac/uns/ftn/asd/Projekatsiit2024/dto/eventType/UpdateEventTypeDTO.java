package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateEventTypeDTO {
	private String description;
    private Set<Integer> recommendedCategoriesIds = new HashSet<>();
}
