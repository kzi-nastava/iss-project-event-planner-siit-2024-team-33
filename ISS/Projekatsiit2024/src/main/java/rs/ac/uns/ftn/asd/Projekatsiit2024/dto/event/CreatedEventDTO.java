package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event;

import java.sql.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Organizer;

@Getter
@Setter
public class CreatedEventDTO {
	private Integer id;
	private String name;
    private String description;
    private int numOfAttendees;
    private Boolean isPrivate;
    private String place;
    private Double latitude;
    private Double longitude;
    private Date dateOfEvent;
    private Date endOfEvent;
    private String picture;
    private Integer price;
    private Organizer organizer;
    private List<EventType> eventTypes;
    
    public CreatedEventDTO(Event event) {
    	this.setId(event.getId());
    	this.setName(event.getName());
    	this.setDescription(event.getDescription());
    	this.setNumOfAttendees(event.getNumOfAttendees());
    	this.setIsPrivate(event.getIsPrivate());
    	this.setPlace(event.getPlace());
    	this.setLatitude(event.getLatitude());
    	this.setLongitude(event.getLongitude());
    	this.setDateOfEvent(event.getDateOfEvent());
    	this.setEndOfEvent(event.getEndOfEvent());
    	this.setPicture(event.getPicture());
    	this.setPrice(event.getPrice());
    	this.setOrganizer(event.getOrganizer());
    	this.setEventTypes(event.getEventTypes());
    }
}
