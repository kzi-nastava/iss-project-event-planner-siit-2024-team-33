package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation;

import java.sql.Date;
import java.sql.Time;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferReservation;

public class CreatedServiceReservationDTO {
    private int reservationId;
    private String ServiceName;
    private String EventName;
    private Date ReservationDate;
    private Time StartTime;
    private Time EndTime;
    
    public CreatedServiceReservationDTO(OfferReservation OR){
    	
    }
}
