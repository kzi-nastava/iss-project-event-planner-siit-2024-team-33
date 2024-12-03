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
    private String Name;
    
    private int EventTypeID;
    
    private String Location;
    private Date FirstPossibleDate;
    private Date LastPossibleDate;
    private Integer NumOfAttendees;
    private List<EventType> eventTypes;
}
