package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;


@Setter
@Getter
public class PostEventDTO {
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
    
}