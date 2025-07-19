package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event;

import java.time.LocalDateTime;

import lombok.Getter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.MinimalEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.organizer.MinimalOrganizerDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;

@Getter
public class DetailedEventDTO {
	private Integer id;
    private String name;
    private String description;
    private Integer numOfAttendees;
    private Integer numOfCurrentlyApplied;
    private String place;
    private LocalDateTime dateOfEvent;
    private LocalDateTime endOfEvent;
    private Double latitude;
    private Double longitude;
    private MinimalOrganizerDTO minimalOrganizer;
    private MinimalEventTypeDTO minimalEventType;
    
    public DetailedEventDTO(Event event) {
    	this.id = event.getId();
    	this.name = event.getName();
    	this.description = event.getDescription();
    	this.numOfAttendees = event.getNumOfAttendees();
    	this.numOfCurrentlyApplied = event.getListOfAttendees().size();
    	this.place = event.getPlace();
    	this.dateOfEvent = event.getDateOfEvent();
    	this.endOfEvent = event.getEndOfEvent();
    	this.latitude = event.getLatitude();
    	this.longitude = event.getLongitude();
    	this.minimalOrganizer = new MinimalOrganizerDTO(event.getOrganizer());
    	this.minimalEventType = new MinimalEventTypeDTO(event.getEventType());
    }
}
