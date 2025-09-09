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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.offerReservation.ServiceBookingException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.service.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;

import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.offer.OfferRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.offer.OfferReservationRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.offer.OfferReservationService;

public class OfferReservationServiceTest {

    @Mock
    private OfferRepository offerRepo;

    @Mock
    private EventRepository eventRepo;
    @Mock
    private AuthentifiedUserRepository userRepo;
    @Mock
    private OfferReservationRepository offerReservationRepo;

    @InjectMocks
    private OfferReservationService reservationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("createOfferReservation - throws error when offer does not exist")
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
    @DisplayName("createOfferReservation - throws error when event does not exist")
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
    @DisplayName("createOfferReservation - should create offer reservation")
    public void testCreateOfferReservation_Success() {
        Provider provider = new Provider();
        provider.setId(1);
        provider.setEmail("provider@example.com");
        Organizer organizer = new Organizer();
        organizer.setId(2);
        organizer.setEmail("organizer@example.com");
        
        Offer offer = new Offer();
        offer.setOfferID(1);
        offer.setProvider(provider);
        
        Event event = new Event();
        event.setId(2);
        event.setDateOfEvent(LocalDateTime.now().plusMonths(1));
        event.setOrganizer(organizer);
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
    @DisplayName("createOfferReservation - throws error when date of reservation hasn't been provided.")
    public void testCreateOfferReservationInvalidArgumentsDate() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createOfferReservation(null, 1, 2, LocalTime.now(), LocalTime.now());
        });

        assertTrue(exception.getMessage().contains("Date of reservation cannot be null"));
    }
    
    @Test
    @DisplayName("createOfferReservation - throws error when offer has not been provided")
    public void testCreateOfferReservationInvalidArgumentsOfferId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createOfferReservation(LocalDate.now(), null, 2, LocalTime.now(), LocalTime.now());
        });

        assertTrue(exception.getMessage().contains("Invalid argument: Offer ID cannot be null."));
    }
    
    @Test
    @DisplayName("createOfferReservation - throws error when event has not been provided")
    public void testCreateOfferReservationInvalidArgumentsEventId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createOfferReservation(LocalDate.now(), 0, null, LocalTime.now(), LocalTime.now());
        });

        assertTrue(exception.getMessage().contains("Invalid argument: Event ID cannot be null."));
    }
    
    @Test
    @DisplayName("createOfferReservation - throws error when starting time of reservation is not provided")
    public void testCreateOfferReservationInvalidArgumentsStartTime() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createOfferReservation(LocalDate.now(), 0, 2, null, LocalTime.now());
        });

        assertTrue(exception.getMessage().contains("Invalid argument: Start time cannot be null."));
    }
    
    @Test
    @DisplayName("createOfferReservation - throws error if reservation end time is before start time")
    public void testCreateOfferReservationInvalidArgumentsEndTime() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createOfferReservation(LocalDate.now(), 0, 2, LocalTime.now(), null);
        });

        assertTrue(exception.getMessage().contains("Invalid argument: End time cannot be null."));
    }
    
    @Test
    @DisplayName("createOfferReservation - throws error if reservation end time is before start time")
    public void testCreateOfferReservationInvalidArgumentsEndTimeBeforeStartTime() {
    	Exception exception = assertThrows(IllegalArgumentException.class, () -> {
    	    reservationService.createOfferReservation(
    	        LocalDate.now().plusDays(3),
    	        1,
    	        2,
    	        LocalTime.of(14, 0),
    	        LocalTime.of(13, 0)
    	    );
    	});

    	assertTrue(exception.getMessage().contains("End time cannot be earlier than start time"));
    }
    @Test
    @DisplayName("createOfferReservation - throws error if reservation date is before today")
    public void testCreateOfferReservationInvalidArgumentsReservationDateBeforeNow() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createOfferReservation(LocalDate.now().minusDays(1), 0, 2, LocalTime.now(), LocalTime.now());
        });

        assertTrue(exception.getMessage().contains("Invalid argument: Given date cannot be before current date."));
    }
    	//--------------------------------------END OF VALIDATION TESTS----------------------------------------------------------------
    
    //This test checks if this offer is already reserved for this or another event.
    @Test
    @DisplayName("bookAService - throws error if reservation for 1 offer is already booked for THIS or ANOTHER service")
    public void testReserveAlreadyReservedOffer() {
        Provider provider = new Provider();
        provider.setId(1);
        provider.setEmail("provider@example.com");
        Organizer organizer = new Organizer();
        organizer.setId(2);
        organizer.setEmail("organizer@example.com");
        
        Offer offer = new Offer();
        offer.setId(10);
        offer.setProvider(provider);

        Event event = new Event();
        event.setId(1);
        event.setDateOfEvent(LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(10, 0)));
        event.setEndOfEvent(LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(20, 0)));
        event.setReservations(new ArrayList<>());
        event.setOrganizer(organizer);

        
        Event eventSecond = new Event();
        eventSecond.setId(2);
        eventSecond.setDateOfEvent(LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(10, 0)));
        eventSecond.setEndOfEvent(LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(20, 0)));
        eventSecond.setReservations(new ArrayList<>());
        
        OfferReservation existingReservation = new OfferReservation();
        existingReservation.setId(1);
        existingReservation.setOffer(offer);
        existingReservation.setEvent(eventSecond);
        existingReservation.setDateOfReservation(LocalDate.now().plusDays(3));
        existingReservation.setStartTime(LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(12, 0)));
        existingReservation.setEndTime(LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(14, 0)));
        
        //Overlapping reservation time
        LocalDateTime requestedStart = LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(12, 0));
        LocalDateTime requestedEnd = LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(14, 0));
        
        when(offerRepo.findById(10)).thenReturn(Optional.of(offer));
        when(eventRepo.findById(1)).thenReturn(Optional.of(event));
        when(eventRepo.findById(2)).thenReturn(Optional.of(eventSecond));
        when(userRepo.findById(1)).thenReturn(Optional.of(provider));
        when(userRepo.findById(2)).thenReturn(Optional.of(organizer));
        when(offerReservationRepo.save(any())).thenReturn(existingReservation);
        when(offerReservationRepo.findByOfferId(offer.getOfferID()))
        .thenReturn(List.of(existingReservation));        
        Exception exception = assertThrows(ServiceBookingException.class, () -> {
            reservationService.bookAService(1, 10, requestedStart, requestedEnd);
        });
        
        String expectedMessage = "This offer is already booked at that time.";
        String actualMessage = exception.getMessage();
        
        System.out.println("Exception message: " + actualMessage); 
        
        assertTrue(actualMessage.contains(expectedMessage));
    }
    //This test checks if the offer's start and end time is out of bounds for the event
    @Test
    @DisplayName("bookAService - throws error if offer start-end time is out of bonds of event start-end time")
    public void testTimeOutOfEventTimeBounds() {
        Offer offer = new Offer();
        offer.setId(10);

        Event event = new Event();
        event.setId(1);
        event.setDateOfEvent(LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(16, 0)));
        event.setEndOfEvent(LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(18, 0)));
        
        //Reservation time out of bounds ----EVENT 16~18----     ----OfferReservation 12:17----
        LocalDateTime requestedStart = LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(12, 0));
        LocalDateTime requestedEnd = LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(17, 0));
        
        when(offerRepo.findById(offer.getId())).thenReturn(Optional.of(offer));
        when(eventRepo.findById(1)).thenReturn(Optional.of(event));
        
        Exception exception = assertThrows(ServiceBookingException.class, () -> {
            reservationService.bookAService(1, 10, requestedStart, requestedEnd);
        });
        
        String expectedMessage = "This offer's end or starting time exceeds the end or start time of this event.";
        String actualMessage = exception.getMessage();
        
        System.out.println("Exception message: " + actualMessage); 
        
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    
    //This test checks if the 1 offer we have collides with the OFFERS OF THE EVENT
    @Test
    @DisplayName("bookAService - Should throw exception when booking offer overlaps with fully booked event")
    public void testOfferReservationWithAlreadyReservedEvent() {
        Offer offer = new Offer();
        offer.setId(1);
        
        //These two offers will take all the time of the event
        Offer offerReserved1 = new Offer();
        offerReserved1.setId(2);
        Offer offerReserved2 = new Offer();
        offerReserved2.setId(3);
        
        Event event = new Event();
        event.setId(1);
        event.setDateOfEvent(LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(12, 0)));
        event.setEndOfEvent(LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(20, 0)));
        
        
        OfferReservation existingReservation1 = new OfferReservation();
        existingReservation1.setId(1);
        existingReservation1.setOffer(offerReserved1);
        existingReservation1.setEvent(event);
        existingReservation1.setDateOfReservation(LocalDate.now().plusDays(3));
        existingReservation1.setStartTime(LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(12, 0)));
        existingReservation1.setEndTime(LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(16, 0)));
        
        OfferReservation existingReservation2 = new OfferReservation();
        existingReservation2.setId(2);
        existingReservation2.setOffer(offerReserved2);
        existingReservation2.setEvent(event);
        existingReservation2.setDateOfReservation(LocalDate.now().plusDays(3));
        existingReservation2.setStartTime(LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(16, 0)));
        existingReservation2.setEndTime(LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(20, 0)));
        
        List<OfferReservation> reservations = new ArrayList<OfferReservation>();
        reservations.add(existingReservation2);
        reservations.add(existingReservation1);
        
        event.setReservations(reservations);
        
        LocalDateTime requestedStart = LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(12, 0));
        LocalDateTime requestedEnd = LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(17, 0));
        
        when(offerRepo.findById(1)).thenReturn(Optional.of(offer));
        when(offerRepo.findById(2)).thenReturn(Optional.of(offerReserved1));
        when(offerRepo.findById(3)).thenReturn(Optional.of(offerReserved2));
        when(eventRepo.findById(1)).thenReturn(Optional.of(event));
        when(offerReservationRepo.findById(1)).thenReturn(Optional.of(existingReservation1));
        when(offerReservationRepo.findById(2)).thenReturn(Optional.of(existingReservation2));
        
        
        Exception exception = assertThrows(ServiceBookingException.class, () -> {
            reservationService.bookAService(1, 1, requestedStart, requestedEnd);
        });
        
        String expectedMessage = "This offer's times collide with already existant offers of this event.";
        String actualMessage = exception.getMessage();
        
        System.out.println("Exception message: " + actualMessage); 
        
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    @Test
    @DisplayName("bookAService- shouldn't throw error, no time collision at all.")
    public void BookAServiceSuccess() {
        Provider provider = new Provider();
        provider.setId(1);
        provider.setEmail("provider@example.com");
        Organizer organizer = new Organizer();
        organizer.setId(2);
        organizer.setEmail("organizer@example.com");
        
    	Offer offer = new Offer();
        offer.setId(1);
        offer.setProvider(provider);
        //Already reserved one
        Offer offerReserved1 = new Offer();
        offerReserved1.setId(2);
        
        Event event = new Event();
        event.setId(1);
        event.setDateOfEvent(LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(12, 0)));
        event.setEndOfEvent(LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(20, 0)));
        event.setReservations(new ArrayList<>());
        event.setOrganizer(organizer);
        
        OfferReservation existingReservation1 = new OfferReservation();
        existingReservation1.setId(1);
        existingReservation1.setOffer(offerReserved1);
        existingReservation1.setEvent(event);
        existingReservation1.setDateOfReservation(LocalDate.now().plusDays(3));
        existingReservation1.setStartTime(LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(12, 0)));
        existingReservation1.setEndTime(LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(16, 0)));
     
        List<OfferReservation> reservations = new ArrayList<OfferReservation>();
        reservations.add(existingReservation1);
        
        event.setReservations(reservations);
        
        //Reservation time out of bounds ----EVENT 16~18----     ----OfferReservation 12:17----
        LocalDateTime requestedStart = LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(16, 15));
        LocalDateTime requestedEnd = LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.of(17, 15));
        
        when(offerRepo.findById(1)).thenReturn(Optional.of(offer));
        when(eventRepo.findById(1)).thenReturn(Optional.of(event));
        
        OfferReservation saved = new OfferReservation();
        when(offerReservationRepo.save(any())).thenReturn(saved);
        
        OfferReservation result = reservationService.bookAService(1, 1, requestedStart, requestedEnd);
        
        assertNotNull(result);
        verify(offerReservationRepo, times(1)).save(any());
    }
    
    
    
    
    @Test
    @DisplayName("cancelService - throws error when we try to cancel service after possible cancelation time passed.")
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
    @DisplayName("cancelService - succeeds because we cancel the reservation before cancelation ticks down")
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
    @DisplayName("findReservationByIdAndService - suceeds if provided the correct ids")
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
    
    //Update reservations
    
    @Test
    @DisplayName("updateReservation - throws error if no reservations were selected")
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
    @DisplayName("updateReservation - throws error if we've been provided wrong service id")
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
    @DisplayName("updateReservation - succeeds when we've been provided correct IDs and correct reservations")
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
