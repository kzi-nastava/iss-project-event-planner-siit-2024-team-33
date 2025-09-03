package rs.ac.uns.ftn.asd.Projekatsiit2024.repository.offer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.BudgetItem;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferReservation;

public interface OfferReservationRepository extends JpaRepository<OfferReservation,Integer>{

    List<OfferReservation> findByEvent(Event event);

    List<OfferReservation> findByDateOfReservation(LocalDate reservationDate);

    @Query("SELECT r FROM OfferReservation r WHERE r.dateOfReservation = :reservationDate AND r.startTime BETWEEN :startTime AND :endTime")
    List<OfferReservation> findByDateOfReservationAndStartTimeBetween(
        @Param("reservationDate") LocalDate reservationDate,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    @Query("SELECT r FROM OfferReservation r WHERE r.offer.offerID = :offerId")
    List<OfferReservation> findByOfferId(@Param("offerId") Integer offerId);
    
    @Query("SELECT res.offer FROM OfferReservation res WHERE res.offer.category=:#{#bi.budgetCategory} AND res.event=:#{#bi.event}")
    List<Offer> findByBudgetItem(@Param("bi") BudgetItem bi);
    
    @Query("SELECT res FROM OfferReservation res WHERE res.offer.id=:offerId AND res.event.id=:eventId")
    OfferReservation findByEventAndOffer(@Param("offerId") Integer offerId, @Param("eventId") Integer eventId);
    
    List<OfferReservation> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT res FROM OfferReservation res WHERE res.offer.offerID=:offerId AND res.event.organizer.email=:email")
    List<OfferReservation> findAllByOffer_IdAndEvent_Organizer_Email(Integer offerId, String email);
    
    @Query("""
    	    SELECT DISTINCT r.event
    	    FROM OfferReservation r
    	    WHERE r.offer.type = 'SERVICE'
    	      AND r.offer.provider.id = :providerId
    	      AND (
    	          (r.startTime BETWEEN :startOfDay AND :endOfDay)
    	          OR
    	          (r.endTime BETWEEN :startOfDay AND :endOfDay)
    	      )
    	""")
	List<Event> findEventsWithProviderServiceReservationsOnDate(
	    @Param("providerId") Integer providerId,
	    @Param("startOfDay") LocalDateTime startOfDay,
	    @Param("endOfDay") LocalDateTime endOfDay
	);
    
    
    @Query("""
    	    SELECT COUNT(r) > 0
    	    FROM OfferReservation r
    	    WHERE TYPE(r.offer) = Service
    	      AND r.offer.provider.id = :providerId
    	      AND r.endTime > CURRENT_TIMESTAMP
    	""")
    boolean existsUpcomingServiceReservationsByProviderId(@Param("providerId") Integer providerId);

}
