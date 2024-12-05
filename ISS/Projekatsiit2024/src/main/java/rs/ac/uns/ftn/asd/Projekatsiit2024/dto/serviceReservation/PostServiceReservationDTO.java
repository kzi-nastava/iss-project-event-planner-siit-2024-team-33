package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation;

import java.sql.Date;
import java.sql.Time;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class PostServiceReservationDTO {
    private Integer eventId;
  
    private String reservationDate;
    private String startTime;
    private String endTime;
}
