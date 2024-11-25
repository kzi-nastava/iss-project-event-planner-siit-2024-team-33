package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation;

import java.sql.Date;
import java.sql.Time;

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
}
