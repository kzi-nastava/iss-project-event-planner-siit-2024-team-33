package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event;

import java.util.Date;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.EventType;
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
    private Boolean itsJoever;
    private String picture;
    private Integer price;
    private Organizer organizer;
    private List<EventType> eventTypes;
    
    
	public GetEventDTO(String name, String description, int numOfAttendees, Boolean isPrivate, String place,
			Double latitude, Double longitude, LocalDateTime dateOfEvent, LocalDateTime endOfEvent, Boolean itsJoever, String picture,
			Integer price, Organizer organizer, List<EventType> eventTypes) {
		super();
		this.name = name;
		this.description = description;
		this.numOfAttendees = numOfAttendees;
		this.isPrivate = isPrivate;
		this.place = place;
		this.latitude = latitude;
		this.longitude = longitude;
		this.dateOfEvent = dateOfEvent;
		this.endOfEvent = endOfEvent;
		this.itsJoever = itsJoever;
		this.picture = picture;
		this.price = price;
		this.organizer = organizer;
		this.eventTypes = eventTypes;
	}
	
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
	    this.itsJoever = e.getItsJoever();
	    this.picture = e.getPicture();
	    this.price = e.getPrice();
	    this.organizer = e.getOrganizer();
	    this.eventTypes = e.getEventTypes();
	}

    
    

    
}
