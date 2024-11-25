package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation;

import java.sql.Date;
import java.sql.Time;

public class PostServiceReservationDTO {
    private int serviceId;
    private int eventId;
  
    private Date ReservationDate;
    private Time StartTime;
    private Time EndTime;
}
