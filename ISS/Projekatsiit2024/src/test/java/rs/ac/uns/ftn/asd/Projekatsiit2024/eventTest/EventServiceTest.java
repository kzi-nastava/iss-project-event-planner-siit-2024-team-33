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

        // Organizer
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

        // UserPrincipal
        userPrincipal = mock(UserPrincipal.class);
        when(userPrincipal.getUser()).thenReturn(organizer);

        // Default EventType
        defaultEventType = new EventType();
        defaultEventType.setId(1);
        defaultEventType.setName("Conference");
        defaultEventType.setIsActive(true);
        when(eventTypeRepository.findById(1)).thenReturn(Optional.of(defaultEventType));

        // Default DTO
        validDTO = new CreateEventDTO();
        validDTO.setName("Valid Event");
        validDTO.setDescription("Valid Description");
        validDTO.setNumOfAttendees(100);
        validDTO.setIsPrivate(false);
        validDTO.setPlace("Belgrade, Serbia");
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
        validDTO.setName("abc"); // too short

        assertThrows(EventValidationException.class,
            () -> eventService.createEvent(userPrincipal, validDTO));
    }

    @Test
    @Order(5)
    void createEvent_WhenDescriptionTooLong_ShouldThrowException() {
        validDTO.setDescription("a".repeat(300)); // too long

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
        validDTO.setPlace("InvalidPlaceFormat"); // not "City, Country"

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
}
