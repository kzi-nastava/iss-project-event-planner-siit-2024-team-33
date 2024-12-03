package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation;

import java.sql.Date;
import java.sql.Time;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Service;
@Setter
@Getter
public class GetServiceReservationDTO {
    private int reservationId;
    private String ServiceName;
    private String EventName;
    private String ProviderName;
    private Date ReservationDate;
    private Time StartTime;
    private Time EndTime;
    //status either String or Availibility enum
    private Availability Status;
    
    
    public GetServiceReservationDTO(OfferReservation OR) {
        this.reservationId = OR.getId();
        this.ServiceName = ((Service) OR.getOffer()).getName();
        this.EventName = OR.getEvent().getName();
        this.ProviderName = ((Service) OR.getOffer()).getProvider().getName();
        this.ReservationDate = OR.getDateOfReservation();
        this.StartTime = OR.getStartTime();
        this.EndTime = OR.getEndTime();
        this.Status =Availability.AVAILABLE; 
    }
    
    
}
