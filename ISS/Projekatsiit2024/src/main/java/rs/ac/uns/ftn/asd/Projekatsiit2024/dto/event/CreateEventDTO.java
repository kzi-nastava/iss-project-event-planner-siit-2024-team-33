package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventActivity.CreateEventActivityDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Invitation;

@Getter
@Setter
public class CreateEventDTO {
    private String name;
    private String description;
    private int numOfAttendees;
    private Boolean isPrivate;
    private String place;
    private LocalDateTime dateOfEvent;
    private LocalDateTime endOfEvent;
    private Integer eventTypeId;
    
    private Double latitude;
    private Double longitude;
    
    private Set<CreateEventActivityDTO> eventActivities = new HashSet<>(); 
    private Set<String> privateInvitations;
    //TODO: add everything needed for the budget
}