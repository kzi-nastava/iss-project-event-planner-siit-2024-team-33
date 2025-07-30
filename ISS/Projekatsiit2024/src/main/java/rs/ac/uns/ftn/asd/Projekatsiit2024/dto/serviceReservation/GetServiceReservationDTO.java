package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation;

import java.util.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.service.Service;
@Setter
@Getter
public class GetServiceReservationDTO {
    private int reservationId;
    private String ServiceName;
    private String EventName;
    private String ProviderName;
    private LocalDate ReservationDate;
    private LocalTime StartTime;
    private LocalTime EndTime;
    //status either String or Availibility enum
    private Availability Status;
    
    
    public GetServiceReservationDTO(OfferReservation OR) {
        this.reservationId = OR.getId();
        this.ServiceName = ((Service) OR.getOffer()).getName();
        this.EventName = OR.getEvent().getName();
        this.ProviderName = ((Service) OR.getOffer()).getProvider().getName();
        this.ReservationDate = OR.getDateOfReservation();
        this.StartTime = OR.getStartTime().toLocalTime();
        this.EndTime = OR.getEndTime().toLocalTime();
        this.Status =Availability.AVAILABLE; 
    }
    
    
}
