package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferCategory;


@Getter
@Setter
public class UpdateEventTypeDTO {
	private String description;
    private List<Integer> recommendedCategoriesIds;
}
