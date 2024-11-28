package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation;

import java.sql.Date;
import java.sql.Time;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Service;

public class GetServiceReservationDTO {
    private int reservationId;
    private String ServiceName;
    private String EventName;
    private String ProviderName;
    private Date ReservationDate;
    private Time StartTime;
    private Time EndTime;
    //status either String or Availibility enum
    private String Status;
    
    
	public GetServiceReservationDTO(OfferReservation OR) {
		
	}
}
