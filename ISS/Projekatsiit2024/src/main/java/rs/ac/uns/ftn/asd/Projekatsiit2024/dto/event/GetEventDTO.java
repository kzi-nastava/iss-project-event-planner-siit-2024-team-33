package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event;

import java.sql.Date;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Event;

public class GetEventDTO {
    public Integer id;
    public String Name;
    public String Description;
    public String Place;
    public Date DateOfEvent;
    public Integer NumOfAttendees;
    
    private GetEventDTO mapToDto(Event event) {
        GetEventDTO dto = new GetEventDTO();
        dto.Name = event.getName();
        dto.Place =event.getPlace();
        dto.NumOfAttendees = event.getNumOfAttendees();
        dto.DateOfEvent = event.getDateOfEvent();
        return dto;
    }

    
}
