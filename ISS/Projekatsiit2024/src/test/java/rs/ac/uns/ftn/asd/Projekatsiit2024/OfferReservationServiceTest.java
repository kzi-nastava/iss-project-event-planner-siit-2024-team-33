// test/unit/offerReservationServiceTest.java
package rs.ac.uns.ftn.asd.Projekatsiit2024;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.service.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.offer.OfferReservationService;

public class OfferReservationServiceTest {

    @Mock
    private OfferRepository offerRepo;

    @Mock
    private EventRepository eventRepo;

    @Mock
    private OfferReservationRepository offerReservationRepo;

    @InjectMocks
    private OfferReservationService reservationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOfferReservation_Success() {
        Offer offer = new Offer();
        offer.setOfferID(1);

        Event event = new Event();
        event.setId(2);
        event.setDateOfEvent(LocalDateTime.now().plusMonths(1));

        when(offerRepo.findById(1)).thenReturn(Optional.of(offer));
        when(eventRepo.findById(2)).thenReturn(Optional.of(event));

        OfferReservation savedReservation = new OfferReservation();
        when(offerReservationRepo.save(any())).thenReturn(savedReservation);

        OfferReservation result = reservationService.createOfferReservation(
                event.getDateOfEvent().toLocalDate(),
                1, 2,
                LocalTime.of(13, 0),
                LocalTime.of(14, 0));

        assertNotNull(result);
        verify(offerRepo).findById(1);
        verify(eventRepo).findById(2);
        verify(offerReservationRepo).save(any());
        
    }
    	//--------------------------------------START OF VALIDATION TESTS----------------------------------------------------------------
    @Test
    public void testCreateOfferReservationInvalidArgumentsDate() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createOfferReservation(null, 1, 2, LocalTime.now(), LocalTime.now());
        });

        assertTrue(exception.getMessage().contains("Date of reservation cannot be null"));
    }
    @Test
    public void testCreateOfferReservationInvalidArgumentsOfferId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createOfferReservation(LocalDate.now(), null, 2, LocalTime.now(), LocalTime.now());
        });

        assertTrue(exception.getMessage().contains("Invalid argument: Offer ID cannot be null."));
    }
    @Test
    public void testCreateOfferReservationInvalidArgumentsEventId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createOfferReservation(LocalDate.now(), 0, null, LocalTime.now(), LocalTime.now());
        });

        assertTrue(exception.getMessage().contains("Invalid argument: Event ID cannot be null."));
    }
    @Test
    public void testCreateOfferReservationInvalidArgumentsStartTime() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createOfferReservation(LocalDate.now(), 0, 2, null, LocalTime.now());
        });

        assertTrue(exception.getMessage().contains("Invalid argument: Start time cannot be null."));
    }
    public void testCreateOfferReservationInvalidArgumentsEndTime() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createOfferReservation(LocalDate.now(), 0, 2, LocalTime.now(), null);
        });

        assertTrue(exception.getMessage().contains("Invalid argument: End time cannot be null."));
    }
    @Test
    public void testCreateOfferReservationInvalidArgumentsEndTimeBeforeStartTime() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createOfferReservation(LocalDate.now(), 0, 2, LocalTime.now(), LocalTime.now().minusHours(1));
        });

        assertTrue(exception.getMessage().contains("Invalid argument: End time cannot be earlier than start time."));
    }
    @Test
    public void testCreateOfferReservationInvalidArgumentsReservationDateBeforeNow() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createOfferReservation(LocalDate.now().minusDays(1), 0, 2, LocalTime.now(), LocalTime.now());
        });

        assertTrue(exception.getMessage().contains("Invalid argument: Given date cannot be before current date."));
    }
    	//--------------------------------------END OF VALIDATION TESTS----------------------------------------------------------------
    @Test
    public void testUpdateReservation_ServiceMismatch_ThrowsException() {
        Offer offer = new Offer();
        offer.setId(10);

        OfferReservation reservation = new OfferReservation();
        reservation.setId(1);
        reservation.setOffer(offer);
        reservation.setEvent(new Event());

        when(offerReservationRepo.findById(1)).thenReturn(Optional.of(reservation));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.updateReservation(1, 99, LocalDate.now(), LocalTime.of(12, 0), LocalTime.of(13, 0));
        });

        assertEquals("Service mismatch.", ex.getMessage());
    }
    //This test checks if this offer is already reserved for this or another event.
    @Test
    public void testReserveAlreadyReservedOffer() {
        Offer offer = new Offer();
        offer.setId(10);

        Event event = new Event();
        event.setId(1);
        event.setDateOfEvent(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)));
        event.setEndOfEvent(LocalDateTime.of(LocalDate.now(), LocalTime.of(20, 0)));
        
        OfferReservation existingReservation = new OfferReservation();
        existingReservation.setId(1);
        existingReservation.setOffer(offer);
        existingReservation.setEvent(event);
        existingReservation.setDateOfReservation(LocalDate.now());
        existingReservation.setStartTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0)));
        existingReservation.setEndTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0)));
        
        //Overlapping reservation time
        LocalDateTime requestedStart = LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0));
        LocalDateTime requestedEnd = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0));
        
        when(offerRepo.findById(10)).thenReturn(Optional.of(offer));
        when(eventRepo.findById(1)).thenReturn(Optional.of(event));
        when(offerReservationRepo.findByOfferId(10)).thenReturn(List.of(existingReservation));
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.bookAService(1, 10, requestedStart, requestedEnd);
        });
        
        String expectedMessage = "This offer is already booked at that time.";
        String actualMessage = exception.getMessage();
        
        System.out.println("Exception message: " + actualMessage); 
        
        assertTrue(actualMessage.contains(expectedMessage));
    }
    //This test checks if the offer's start and end time is out of bounds for the event
    @Test
    public void testTimeOutOfEventTimeBounds() {
        Offer offer = new Offer();
        offer.setId(10);

        Event event = new Event();
        event.setId(1);
        event.setDateOfEvent(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0)));
        event.setEndOfEvent(LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 0)));
        
        //Reservation time out of bounds ----EVENT 16~18----     ----OfferReservation 12:17----
        LocalDateTime requestedStart = LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0));
        LocalDateTime requestedEnd = LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 0));
        
        when(offerRepo.findById(10)).thenReturn(Optional.of(offer));
        when(eventRepo.findById(1)).thenReturn(Optional.of(event));
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.bookAService(1, 10, requestedStart, requestedEnd);
        });
        
        String expectedMessage = "This offer's end or starting time exceeds the end or start time of this event.";
        String actualMessage = exception.getMessage();
        
        System.out.println("Exception message: " + actualMessage); 
        
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    
    //This test checks if the 1 offer we have collides with the OFFERS OF THE EVENT
    @Test
    public void testOfferReservationWithAlreadyReservedEvent() {
        Offer offer = new Offer();
        offer.setId(1);
        
        //These two offers will take all the time of the event
        Offer offerReserved1 = new Offer();
        offer.setId(2);
        Offer offerReserved2 = new Offer();
        offer.setId(3);
        
        Event event = new Event();
        event.setId(1);
        event.setDateOfEvent(LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0)));
        event.setEndOfEvent(LocalDateTime.of(LocalDate.now(), LocalTime.of(20, 0)));
        
        
        OfferReservation existingReservation1 = new OfferReservation();
        existingReservation1.setId(1);
        existingReservation1.setOffer(offerReserved1);
        existingReservation1.setEvent(event);
        existingReservation1.setDateOfReservation(LocalDate.now());
        existingReservation1.setStartTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0)));
        existingReservation1.setEndTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0)));
        
        OfferReservation existingReservation2 = new OfferReservation();
        existingReservation2.setId(2);
        existingReservation2.setOffer(offerReserved2);
        existingReservation2.setEvent(event);
        existingReservation2.setDateOfReservation(LocalDate.now());
        existingReservation2.setStartTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0)));
        existingReservation2.setEndTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(20, 0)));
        
        List<OfferReservation> reservations = new ArrayList<OfferReservation>();
        reservations.add(existingReservation2);
        reservations.add(existingReservation1);
        
        event.setReservations(reservations);
        
        //Reservation time out of bounds ----EVENT 16~18----     ----OfferReservation 12:17----
        LocalDateTime requestedStart = LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0));
        LocalDateTime requestedEnd = LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 0));
        
        when(offerRepo.findById(1)).thenReturn(Optional.of(offer));
        when(offerRepo.findById(2)).thenReturn(Optional.of(offerReserved1));
        when(offerRepo.findById(3)).thenReturn(Optional.of(offerReserved2));
        when(eventRepo.findById(1)).thenReturn(Optional.of(event));
        when(offerReservationRepo.findById(1)).thenReturn(Optional.of(existingReservation1));
        when(offerReservationRepo.findById(2)).thenReturn(Optional.of(existingReservation1));
        
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.bookAService(1, 1, requestedStart, requestedEnd);
        });
        
        String expectedMessage = "This offer's times collide with already existant offers of this event.";
        String actualMessage = exception.getMessage();
        
        System.out.println("Exception message: " + actualMessage); 
        
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    @Test
    public void BookAServiceSuccess() {
    	
    }
    
    @Test
    public void TestCreationWithoutProvidedEvent() {
    	LocalDate date = LocalDate.now();
    	LocalTime startTime = LocalTime.of(12, 0);
    	LocalTime endTime = LocalTime.of(14, 0);
    	Offer offer = new Offer();
    	int offerId = 0;
    	int eventId = 99999;
    	offer.setId(offerId);
    	
    	when(offerRepo.findById(0)).thenReturn(Optional.of(offer));
    	
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createOfferReservation(date, offerId, eventId, startTime, endTime);
        });
        
        assertEquals("No event with the given ID exists.", thrown.getMessage());
    }
    
    @Test
    public void TestCreationWithoutProvidedOffer() {
    	LocalDate date = LocalDate.now();
    	LocalTime startTime = LocalTime.of(12, 0);
    	LocalTime endTime = LocalTime.of(14, 0);
    	Event event = new Event();
    	int offerId = 0;
    	int eventId = 99999;
    	event.setId(eventId);
    	
    	when(eventRepo.findById(0)).thenReturn(Optional.of(event));
    	
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createOfferReservation(date, offerId, eventId, startTime, endTime);
        });
        
        assertEquals("No offer with the given ID exists.", thrown.getMessage());
    }
    
    @Test
    public void testServiceCancellationThrowsExceptionWhenTooLate() {
        OfferReservation reservation = new OfferReservation();
        reservation.setDateOfReservation(LocalDate.now().plusDays(1)); 

        Service service = new Service();
        service.setCancellationInHours(48);

        reservation.setOffer(service);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.cancelService(reservation);
        });

        assertEquals("Cannot cancel the service less than " + service.getCancellationInHours() + " hours before the reservation.", exception.getMessage());
    }
    
    @Test
    public void testServiceCancellationSuccess() {
        // Given
        OfferReservation reservation = new OfferReservation();
        reservation.setDateOfReservation(LocalDate.now().plusDays(3));

        Service service = new Service();
        service.setCancellationInHours(48); 

        reservation.setOffer(service);

        assertDoesNotThrow(() -> reservationService.cancelService(reservation));

        verify(offerReservationRepo, times(1)).delete(reservation);
    }


    
    @Test
    public void testfindReservationByIdAndServiceThrowsException() {
        int reservationId = 1;
        int serviceId = 100;

        OfferReservation reservation = new OfferReservation();
        Service service = new Service();
        service.setId(999); 
        reservation.setOffer(service);

        when(offerReservationRepo.findById(reservationId)).thenReturn(Optional.of(reservation));

        OfferReservation result = reservationService.findReservationByIdAndService(serviceId, reservationId);

        assertNull(result);
        verify(offerReservationRepo, times(1)).findById(reservationId);
    }

    @Test
    public void testfindReservationByIdAndService() {
        int reservationId = 1;
        int serviceId = 100;

        OfferReservation reservation = new OfferReservation();
        Service service = new Service();
        service.setId(serviceId);
        reservation.setOffer(service);

        when(offerReservationRepo.findById(reservationId)).thenReturn(Optional.of(reservation));

        OfferReservation result = reservationService.findReservationByIdAndService(serviceId, reservationId);

        assertNotNull(result);
        assertEquals(reservation, result);
        verify(offerReservationRepo, times(1)).findById(reservationId);
    }
    
    //Update Reservation
    @Test
    public void updateReservationThrowsExceptionNotFound() {
    	int reservationId = 0;
    	when(offerReservationRepo.findById(reservationId)).thenReturn(Optional.empty());
    	
    	Exception exception = assertThrows(IllegalArgumentException.class, ()->{
    		reservationService.updateReservation(reservationId, 0, LocalDate.now(), LocalTime.of(10, 11), LocalTime.of(15, 0));
    	}
    	);
    	assertEquals("Reservation not found.", exception.getMessage());
    }
    
    @Test
    public void updateReservationThrowsExceptionMismatch() {
    	int reservationId =0;
    	int correctServiceId =1;
    	int wrongServiceId =999;
    	
    	OfferReservation reservation = new OfferReservation();
    	Service offer = new Service();
    	offer.setId(correctServiceId);
    	reservation.setOffer(offer);
    	
    	Event event = new Event();
    	event.setId(2);
    	reservation.setEvent(event);
    	
    	when(offerReservationRepo.findById(reservationId)).thenReturn(Optional.of(reservation));
    	Exception exception = assertThrows(IllegalArgumentException.class, ()->{
            reservationService.updateReservation(reservationId, wrongServiceId, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(12, 0));
    	});
        assertEquals("Service mismatch.", exception.getMessage());

    }
    
    @Test
    public void testUpdateReservationSuccess() {
        int reservationId = 0;
        int serviceId = 1;

        OfferReservation reservation = new OfferReservation();
        Service offer = new Service();
        offer.setId(serviceId);
        reservation.setOffer(offer);
        Event event = new Event();
        event.setId(3);
        reservation.setEvent(event);

        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(11, 0);

        when(offerReservationRepo.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(offerReservationRepo.save(any(OfferReservation.class))).thenReturn(reservation);

        OfferReservation updated = reservationService.updateReservation(reservationId, serviceId, date, start, end);

        assertEquals(date, updated.getDateOfReservation());
        assertEquals(LocalDateTime.of(date, start), updated.getStartTime());
        assertEquals(LocalDateTime.of(date, end), updated.getEndTime());
        verify(offerReservationRepo, times(1)).save(reservation);
    }
    
    
}
