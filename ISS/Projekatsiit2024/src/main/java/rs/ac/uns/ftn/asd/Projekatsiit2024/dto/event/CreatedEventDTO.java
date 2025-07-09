package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventActivity.CreatedEventActivityDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;

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
    private LocalDateTime dateOfEvent;
    private LocalDateTime endOfEvent;
    private EventType eventType;
    
    private List<CreatedEventActivityDTO> eventActivities;
    
    
    //TODO: add for private list of people in event if needed
    //TODO: add for budget and organization if needed
    
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
    	this.setEventType(event.getEventType());
    	this.setEventActivities(event.getEventActivities().stream()
    			.map(CreatedEventActivityDTO::new).collect(Collectors.toList()));
    }
}
