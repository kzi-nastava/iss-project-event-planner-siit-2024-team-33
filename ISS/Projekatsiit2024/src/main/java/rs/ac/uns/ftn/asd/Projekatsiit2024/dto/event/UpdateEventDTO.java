package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;

@Setter
@Getter
public class UpdateEventDTO {
    private String name;
    private String description;
    private String place;
    private Double latitude;
    private Double longitude;
    private LocalDateTime dateOfEvent;
    private LocalDateTime endOfEvent;
    private int numOfAttendees;
    private Boolean isPrivate;
    private Integer price;
    private String picture;
    private List<EventType> eventTypes;


}
