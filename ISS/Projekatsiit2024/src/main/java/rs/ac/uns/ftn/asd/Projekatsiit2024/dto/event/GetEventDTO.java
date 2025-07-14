package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;

@Setter
@Getter
public class GetEventDTO {
    private String name;
    private String description;
    private int numOfAttendees;
    private Boolean isPrivate;
    private String place;
    private Double latitude;
    private Double longitude;
    private LocalDateTime dateOfEvent;
    private LocalDateTime endOfEvent;
    private String picture;
    private Integer price;
    private Organizer organizer;
    private EventType eventType;
	
	public GetEventDTO(Event e) {
	    this.name = e.getName();
	    this.description = e.getDescription();
	    this.numOfAttendees = e.getNumOfAttendees();
	    this.isPrivate = e.getIsPrivate();
	    this.place = e.getPlace();
	    this.latitude = e.getLatitude();
	    this.longitude = e.getLongitude();
	    this.dateOfEvent = e.getDateOfEvent();
	    this.endOfEvent = e.getEndOfEvent();
	    this.organizer = e.getOrganizer();
	    this.eventType = e.getEventType();
	}
}
