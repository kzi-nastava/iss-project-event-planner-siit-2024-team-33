package rs.ac.uns.ftn.asd.Projekatsiit2024.model;

import java.util.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class OfferReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 

    private LocalDate dateOfReservation;

    @ManyToOne
    private Offer offer; 

    @ManyToOne
    private Event event; 
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    
}
