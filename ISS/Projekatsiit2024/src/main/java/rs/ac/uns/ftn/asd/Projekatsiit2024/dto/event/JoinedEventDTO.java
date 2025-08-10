package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event;

import lombok.Getter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;

@Getter
public class JoinedEventDTO {
	private Integer id;
	private Integer numOfAttendees;
	private Integer numOfCurrentlyApplied;
	
	public JoinedEventDTO(Event event) {
		this.id = event.getId();
		this.numOfAttendees = event.getNumOfAttendees();
		this.numOfCurrentlyApplied = event.getListOfAttendees().size();
	}
}
