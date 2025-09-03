package rs.ac.uns.ftn.asd.Projekatsiit2024;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.BudgetItem;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.offer.OfferRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.offer.OfferReservationRepository;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class OfferReservationRepositoryTest {

    @Autowired
    private OfferReservationRepository repository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private OfferRepository offerRepository;


    @Test
    public void findByEventTest() {
        Event event = new Event();
        event = eventRepository.save(event);

        OfferReservation res = new OfferReservation();
        res.setEvent(event);
        repository.save(res);

        List<OfferReservation> result = repository.findByEvent(event);
        assertFalse(result.isEmpty());
    }

    @Test
    public void EmptyfindByEventTest() {
        Event event = new Event();
        event.setDateOfEvent(LocalDateTime.now());
        eventRepository.save(event);

        assertTrue(repository.findByEvent(event).isEmpty());
    }

    @Test
    public void findByDateOfReservationTest() {
        OfferReservation res = new OfferReservation();
        LocalDate today = LocalDate.now();
        res.setDateOfReservation(today);
        repository.saveAndFlush(res); 

        List<OfferReservation> result = repository.findByDateOfReservation(today);
        assertFalse(result.isEmpty());
    }


    @Test
    public void findByDateOfReservationAndStartTimeBetweenTest() {
        LocalDate today = LocalDate.now();
        LocalTime startTime = LocalTime.of(12, 0);
        LocalTime endTime = LocalTime.of(13, 0);

        OfferReservation res = new OfferReservation();
        res.setDateOfReservation(today);
        res.setStartTime(LocalDateTime.of(today, startTime));
        res.setEndTime(LocalDateTime.of(today, endTime));
        repository.saveAndFlush(res);

        List<OfferReservation> result = repository.findByDateOfReservationAndStartTimeBetween(
                today,
                LocalDateTime.of(today, LocalTime.of(11, 0)),
                LocalDateTime.of(today, LocalTime.of(13, 0))
        );

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }



    @Test
    public void emptyfindByDateOfReservationAndStartTimeBetweenTest() {
        LocalDate today = LocalDate.now();

        List<OfferReservation> result = repository.findByDateOfReservationAndStartTimeBetween(
                today,
                LocalDateTime.of(today, LocalTime.of(11, 0)),
                LocalDateTime.of(today, LocalTime.of(13, 0))
        );

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    public void findByOfferIdTest() {
        Offer offer = new Offer();
        offer = offerRepository.save(offer);

        OfferReservation res = new OfferReservation();
        res.setOffer(offer);
        res.setDateOfReservation(LocalDate.now());
        repository.save(res);

        List<OfferReservation> result = repository.findByOfferId(offer.getId());
        assertFalse(result.isEmpty());
    }

    @Test
    public void EmptyfindByOfferIdTest() {
        assertTrue(repository.findByOfferId(12345).isEmpty());
    }

    // Ovo pada, ovu funkciju u repozitorijumu je Milos radio wtf je ovo
    @Test
    public void findByBudgetItemTest() {
        Event event = eventRepository.save(new Event());
        Offer offer = offerRepository.save(new Offer());

        OfferReservation res = new OfferReservation();
        res.setEvent(event);
        res.setOffer(offer);
        res.setDateOfReservation(LocalDate.now());
        repository.save(res);

        BudgetItem item = new BudgetItem();
        item.setEvent(event);
        item.setBudgetCategory(offer.getCategory());

        List<Offer> result = repository.findByBudgetItem(item);
        assertFalse(result.isEmpty());
    }


    @Test
    public void findByEventAndOfferTest() {
        Event event = eventRepository.save(new Event());
        Offer offer = offerRepository.save(new Offer());

        OfferReservation res = new OfferReservation();
        res.setEvent(event);
        res.setOffer(offer);
        repository.save(res);

        OfferReservation found = repository.findByEventAndOffer(offer.getId(), event.getId());
        assertNotNull(found);
    }

}
