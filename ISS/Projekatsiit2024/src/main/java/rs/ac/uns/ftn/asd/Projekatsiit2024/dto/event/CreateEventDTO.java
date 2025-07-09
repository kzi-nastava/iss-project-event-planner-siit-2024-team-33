package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventActivity.CreateEventActivityDTO;

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
    
    private List<CreateEventActivityDTO> eventActivities; 
    
    //TODO: add list of users if private
    //TODO: add everything needed for the budget
}
