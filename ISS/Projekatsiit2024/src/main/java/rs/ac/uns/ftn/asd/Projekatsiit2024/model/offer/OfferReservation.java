package rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;

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
