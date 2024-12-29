package rs.ac.uns.ftn.asd.Projekatsiit2024.Repository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.BudgetItem;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferReservation;

public interface OfferReservationRepository extends JpaRepository<OfferReservation,Integer>{

    List<OfferReservation> findByEvent(Event event);

    List<OfferReservation> findByDateOfReservation(Date reservationDate);

    @Query("SELECT r FROM OfferReservation r WHERE r.dateOfReservation = :reservationDate AND r.startTime BETWEEN :startTime AND :endTime")
    List<OfferReservation> findByDateOfReservationAndStartTimeBetween(
        @Param("reservationDate") Date reservationDate,
        @Param("startTime") Time startTime,
        @Param("endTime") Time endTime
    );
    
    @Query("SELECT r FROM OfferReservation r WHERE r.offer.id = :offerId")
    List<OfferReservation> findByOfferId(@Param("offerId") Integer offerId);
    
    @Query("SELECT res.offer FROM OfferReservation res WHERE res.offer.category=:#{#bi.budgetCategory} AND res.event=:#{#bi.event}")
    List<Offer> findByBudgetItem(@Param("bi") BudgetItem bi);
    
    @Query("SELECT res FROM OfferReservation res WHERE res.offer.id=:offerId AND res.event.id=:eventId")
    OfferReservation findByEventAndOffer(@Param("offerId") Integer offerId, @Param("eventId") Integer eventId);

}
