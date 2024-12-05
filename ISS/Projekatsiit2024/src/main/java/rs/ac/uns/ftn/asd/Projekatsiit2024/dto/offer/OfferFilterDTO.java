package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offer;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.MinimalEventTypeDTO;

@Setter
@Getter
public class OfferFilterDTO {
	private Boolean isProduct; 
	private Boolean isService; 
	private String name;
	private String category; 
	private Integer lowestPrice;
	private Availability isAvailable;
	private List<MinimalEventTypeDTO> eventTypes;
}
