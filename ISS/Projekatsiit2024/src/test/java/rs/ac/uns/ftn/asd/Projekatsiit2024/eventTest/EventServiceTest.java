package rs.ac.uns.ftn.asd.Projekatsiit2024.eventTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.CreateEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventActivity.CreateEventActivityDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventActivityValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.Role;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventTypeRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.event.EventService;

@TestMethodOrder(OrderAnnotation.class)
public class EventServiceTest {
    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventTypeRepository eventTypeRepository;

    private Organizer organizer;
    private UserPrincipal userPrincipal;
    private CreateEventDTO validDTO;
    private EventType defaultEventType;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        
        organizer = new Organizer();
        organizer.setEmail("organizer@test.com");
        organizer.setPassword("password");
        organizer.setName("John");
        organizer.setSurname("Doe");
        organizer.setIsDeleted(false);
        organizer.setIsVerified(true);
        Role organizerRole = new Role();
        organizerRole.setId(1);
        organizerRole.setName("ORGANIZER_ROLE");
        organizer.setRole(organizerRole);
        organizer.setResidency("Belgrade, Serbia");
        organizer.setPhoneNumber("123456789");

        userPrincipal = mock(UserPrincipal.class);
        when(userPrincipal.getUser()).thenReturn(organizer);

        //default eventType
        defaultEventType = new EventType();
        defaultEventType.setId(1);
        defaultEventType.setName("Conference");
        defaultEventType.setIsActive(true);
        when(eventTypeRepository.findById(1)).thenReturn(Optional.of(defaultEventType));

        //default DTO
        validDTO = new CreateEventDTO();
        validDTO.setName("Valid Event");
        validDTO.setDescription("Valid Description");
        validDTO.setNumOfAttendees(100);
        validDTO.setIsPrivate(false);
        validDTO.setPlace("Belgrade, Serbia");
        validDTO.setLatitude(44.8176);
        validDTO.setLongitude(20.4569);
        validDTO.setDateOfEvent(LocalDateTime.now().plusDays(1));
        validDTO.setEndOfEvent(LocalDateTime.now().plusDays(1).plusHours(2));
        validDTO.setEventTypeId(1);

        CreateEventActivityDTO activity = new CreateEventActivityDTO();
        activity.setName("Activity");
        activity.setDescription("Activity Desc");
        activity.setStartingTime(LocalDateTime.now().plusDays(1));
        activity.setEndingTime(LocalDateTime.now().plusDays(1).plusHours(1));
        activity.setLocation("Hall 1");
        validDTO.getEventActivities().add(activity);
    }

    @Test
    @Order(1)
    void createEvent_ShouldReturnSavedEvent() {
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Event savedEvent = eventService.createEvent(userPrincipal, validDTO);

        assertNotNull(savedEvent);
        assertEquals(validDTO.getName(), savedEvent.getName());
        assertEquals(organizer, savedEvent.getOrganizer());
        assertEquals(defaultEventType, savedEvent.getEventType());
        assertEquals(1, savedEvent.getEventActivities().size());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    @Order(2)
    void createEvent_WhenEventTypeNotFound_ShouldThrowException() {
        validDTO.setEventTypeId(999);
        when(eventTypeRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EventValidationException.class,
            () -> eventService.createEvent(userPrincipal, validDTO));
    }

    @Test
    @Order(3)
    void createEvent_WhenEventTypeInactive_ShouldThrowException() {
        EventType inactiveType = new EventType();
        inactiveType.setId(1);
        inactiveType.setIsActive(false);
        when(eventTypeRepository.findById(1)).thenReturn(Optional.of(inactiveType));

        assertThrows(EventValidationException.class,
            () -> eventService.createEvent(userPrincipal, validDTO));
    }

    @Test
    @Order(4)
    void createEvent_WhenNameTooShort_ShouldThrowException() {
        validDTO.setName("abc");

        assertThrows(EventValidationException.class,
            () -> eventService.createEvent(userPrincipal, validDTO));
    }

    @Test
    @Order(5)
    void createEvent_WhenDescriptionTooLong_ShouldThrowException() {
        validDTO.setDescription("a".repeat(300));

        assertThrows(EventValidationException.class,
            () -> eventService.createEvent(userPrincipal, validDTO));
    }

    @Test
    @Order(6)
    void createEvent_WhenNegativeAttendees_ShouldThrowException() {
        validDTO.setNumOfAttendees(-5);

        assertThrows(EventValidationException.class,
            () -> eventService.createEvent(userPrincipal, validDTO));
    }

    @Test
    @Order(7)
    void createEvent_WhenInvalidPlace_ShouldThrowException() {
        validDTO.setPlace("InvalidPlaceFormat");

        assertThrows(EventValidationException.class,
            () -> eventService.createEvent(userPrincipal, validDTO));
    }

    @Test
    @Order(8)
    void createEvent_WhenStartAfterEnd_ShouldThrowException() {
        validDTO.setDateOfEvent(LocalDateTime.now().plusDays(1).plusHours(5));
        validDTO.setEndOfEvent(LocalDateTime.now().plusDays(1));

        assertThrows(EventValidationException.class,
            () -> eventService.createEvent(userPrincipal, validDTO));
    }

    @Test
    @Order(9)
    void createEvent_WhenEventInPast_ShouldThrowException() {
        validDTO.setDateOfEvent(LocalDateTime.now().minusDays(1));
        validDTO.setEndOfEvent(LocalDateTime.now().minusHours(1));

        assertThrows(EventValidationException.class,
            () -> eventService.createEvent(userPrincipal, validDTO));
    }

    @Test
    @Order(10)
    void createEvent_WhenActivityIsNull_ShouldThrowException() {
        validDTO.getEventActivities().add(null);

        assertThrows(EventActivityValidationException.class,
            () -> eventService.createEvent(userPrincipal, validDTO));
    }
    
    @Test
    @Order(11)
    void createEvent_WhenActivityTimeOutsideEvent_ShouldThrowException() {
        CreateEventActivityDTO earlyActivity = new CreateEventActivityDTO();
        earlyActivity.setName("Early Activity");
        earlyActivity.setDescription("Starts before event");
        earlyActivity.setStartingTime(validDTO.getDateOfEvent().minusHours(1));
        earlyActivity.setEndingTime(validDTO.getDateOfEvent().plusMinutes(30));
        earlyActivity.setLocation("Hall 2");

        validDTO.getEventActivities().clear();
        validDTO.getEventActivities().add(earlyActivity);

        EventActivityValidationException ex1 = assertThrows(
            EventActivityValidationException.class,
            () -> eventService.createEvent(userPrincipal, validDTO)
        );
        assertTrue(ex1.getMessage().contains("can't be outside of time when event is taking place"));

        CreateEventActivityDTO lateActivity = new CreateEventActivityDTO();
        lateActivity.setName("Late Activity");
        lateActivity.setDescription("Ends after event");
        lateActivity.setStartingTime(validDTO.getDateOfEvent().plusHours(1));
        lateActivity.setEndingTime(validDTO.getEndOfEvent().plusHours(1));
        lateActivity.setLocation("Hall 3");

        validDTO.getEventActivities().clear();
        validDTO.getEventActivities().add(lateActivity);

        EventActivityValidationException ex2 = assertThrows(
            EventActivityValidationException.class,
            () -> eventService.createEvent(userPrincipal, validDTO)
        );
        assertTrue(ex2.getMessage().contains("can't be outside of time when event is taking place"));
    }
    
    @Test
    @Order(12)
    void createEvent_WhenLatitudeOrLongitudeInvalid_ShouldThrowException() {
        validDTO.setLatitude(-91.0);
        validDTO.setLongitude(20.0);

        EventValidationException ex1 = assertThrows(
            EventValidationException.class,
            () -> eventService.createEvent(userPrincipal, validDTO)
        );
        assertTrue(ex1.getMessage().contains("Latitude must be between -90 and 90"));

        validDTO.setLatitude(91.0);
        validDTO.setLongitude(20.0);

        EventValidationException ex2 = assertThrows(
            EventValidationException.class,
            () -> eventService.createEvent(userPrincipal, validDTO)
        );
        assertTrue(ex2.getMessage().contains("Latitude must be between -90 and 90"));

        validDTO.setLatitude(44.8176);
        validDTO.setLongitude(-181.0);

        EventValidationException ex3 = assertThrows(
            EventValidationException.class,
            () -> eventService.createEvent(userPrincipal, validDTO)
        );
        assertTrue(ex3.getMessage().contains("Longitude must be between -180 and 180"));

        validDTO.setLongitude(181.0);

        EventValidationException ex4 = assertThrows(
            EventValidationException.class,
            () -> eventService.createEvent(userPrincipal, validDTO)
        );
        assertTrue(ex4.getMessage().contains("Longitude must be between -180 and 180"));
    }
}
