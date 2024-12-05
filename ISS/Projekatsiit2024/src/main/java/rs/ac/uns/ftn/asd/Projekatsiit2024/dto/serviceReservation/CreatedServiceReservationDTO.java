package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Service;
@Setter
@Getter
public class CreatedServiceReservationDTO {
    private int reservationId;
    private String ServiceName;
    private String EventName;
    private LocalDate ReservationDate;
    private LocalTime StartTime;
    private LocalTime EndTime;
    
    public CreatedServiceReservationDTO(OfferReservation OR) {
        this.reservationId = OR.getId();
        this.ServiceName = ((Service) OR.getOffer()).getName();
        this.EventName = OR.getEvent().getName();
        this.ReservationDate = OR.getDateOfReservation();
        this.StartTime = OR.getStartTime().toLocalTime();
        this.EndTime = OR.getEndTime().toLocalTime();
    }

}
