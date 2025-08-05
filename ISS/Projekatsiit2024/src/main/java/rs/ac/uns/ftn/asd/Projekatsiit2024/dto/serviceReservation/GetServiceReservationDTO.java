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
    private String serviceName;
    private String eventName;
    private String providerName;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    //status either String or Availibility enum
    private Availability status;
    
    
    public GetServiceReservationDTO(OfferReservation OR) {
        this.reservationId = OR.getId();
        this.serviceName = ((Service) OR.getOffer()).getName();
        this.eventName = OR.getEvent().getName();
        this.providerName = ((Service) OR.getOffer()).getProvider().getName();
        this.reservationDate = OR.getDateOfReservation();
        this.startTime = OR.getStartTime().toLocalTime();
        this.endTime = OR.getEndTime().toLocalTime();
        this.status =Availability.AVAILABLE; 
    }
    
    
}
