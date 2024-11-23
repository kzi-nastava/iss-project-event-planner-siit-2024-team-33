package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event;

import java.sql.Date;

public class FilterEventDTO {
    private String Name;
    
    private int EventTypeID;
    
    private String Location;
    private Date FirstPossibleDate;
    private Date LastPossibleDate;
    private Integer NumOfAttendees;
}
