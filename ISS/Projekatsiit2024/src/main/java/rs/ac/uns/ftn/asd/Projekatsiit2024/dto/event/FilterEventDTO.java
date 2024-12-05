package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event;

import java.sql.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Organizer;

@Setter
@Getter
public class FilterEventDTO {
    private String name;
    
    
    private String location;
    private String firstPossibleDate;
    private String lastPossibleDate;
    private Integer numOfAttendees;
    private List<EventType> eventTypes;
}
