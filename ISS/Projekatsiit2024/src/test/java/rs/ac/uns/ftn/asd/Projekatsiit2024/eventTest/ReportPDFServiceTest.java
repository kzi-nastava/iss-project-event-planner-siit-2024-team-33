package rs.ac.uns.ftn.asd.Projekatsiit2024.eventTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventActivity;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.reportPDF.ReportPDFService;

@ExtendWith(MockitoExtension.class)
public class ReportPDFServiceTest {
	
	@InjectMocks
	private ReportPDFService reportPDFService;
	
	@Mock
	private EventRepository eventRepository;
	
	@Mock
	private UserPrincipal userPrincipal;
	
	private Event event;
	private EventType eventType;
	private AuthentifiedUser attendee;
	
	
	@BeforeEach
	void setup() {
		eventType = new EventType();
        eventType.setId(1);
        eventType.setName("Conference");
        eventType.setDescription("This is event type for conference.");
        eventType.setIsActive(true);
        
        attendee = new AuthentifiedUser();
        attendee.setId(101);
        attendee.setEmail("attendee@test.com");
        
        event = new Event();
        event.setId(1);
        event.setName("Test Event");
        event.setDescription("Event description");
        event.setNumOfAttendees(50);
        event.setIsPrivate(false);
        event.setPlace("Belgrade, Serbia");
        event.setDateOfEvent(LocalDateTime.now().plusDays(1));
        event.setEndOfEvent(LocalDateTime.now().plusDays(1).plusHours(3));
        event.setEventType(eventType);
        event.setListOfAttendees(Set.of(attendee));
        
        EventActivity activity = new EventActivity();
        activity.setId(201);
        activity.setName("Opening");
        activity.setDescription("Welcome speech");
        activity.setStartingTime(event.getDateOfEvent().plusHours(1));
        activity.setEndingTime(event.getDateOfEvent().plusHours(2));
        activity.setLocation("Main Hall");
        event.setEventActivities(Set.of(activity));
	}
	
	@Test
    void createEventDetailsReport_ShouldReturnValidPdf_WhenEventPublic() throws Exception {
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));

        byte[] pdfBytes = reportPDFService.createEventDetailsReport(1, userPrincipal);

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);

        try (PDDocument doc = PDDocument.load(pdfBytes)) {
            assertTrue(doc.getNumberOfPages() > 0);
        }
    }

    @Test
    void createEventDetailsReport_ShouldContainEventDetailsInPdf() throws Exception {
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));

        byte[] pdfBytes = reportPDFService.createEventDetailsReport(1, userPrincipal);

        try (PDDocument doc = PDDocument.load(pdfBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);

            assertTrue(text.contains("Test Event"));
            assertTrue(text.contains("Conference"));
            assertTrue(text.contains("attendee@test.com"));
            assertTrue(text.contains("Belgrade, Serbia"));
            assertTrue(text.contains("Welcome speech"));
        }
    }

    @Test
    void createEventDetailsReport_ShouldReturnPdf_WhenPrivateEventAndUserIsAttendee() throws Exception {
        event.setIsPrivate(true);

        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        when(userPrincipal.getUser()).thenReturn(attendee);

        byte[] pdfBytes = reportPDFService.createEventDetailsReport(1, userPrincipal);

        assertNotNull(pdfBytes);
        try (PDDocument doc = PDDocument.load(pdfBytes)) {
            assertTrue(doc.getNumberOfPages() > 0);
        }
    }
    
    @Test
    void createEventDetailsReport_ShouldThrowException_WhenPrivateEventAndUserNotAttendee() {
        event.setIsPrivate(true);

        AuthentifiedUser notAttendee = new AuthentifiedUser();
        notAttendee.setId(999);
        notAttendee.setEmail("other@test.com");

        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        when(userPrincipal.getUser()).thenReturn(notAttendee);

        assertThrows(
            EventValidationException.class,
            () -> reportPDFService.createEventDetailsReport(1, userPrincipal)
        );
    }
	
    @Test
    void createEventDetailsReport_ShouldThrowException_WhenPrivateEventAndUserNotLoggedIn() {
        event.setIsPrivate(true);

        when(eventRepository.findById(1)).thenReturn(Optional.of(event));

        assertThrows(
            EventValidationException.class,
            () -> reportPDFService.createEventDetailsReport(1, null)
        );
    }
    
    @Test
    void createEventDetailsReport_ShouldThrowException_WhenEventNotFound() {
        when(eventRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(
            EventValidationException.class,
            () -> reportPDFService.createEventDetailsReport(999, userPrincipal)
        );
    }
    
    @Test
    void createEventDetailsReport_ShouldContainAllEventActivities() throws Exception {
        EventActivity activity2 = new EventActivity();
        activity2.setId(202);
        activity2.setName("Closing");
        activity2.setDescription("Goodbye speech");
        activity2.setStartingTime(event.getDateOfEvent().plusHours(2));
        activity2.setEndingTime(event.getDateOfEvent().plusHours(3));
        activity2.setLocation("Main Hall");

        event.setEventActivities(Set.of(
            event.getEventActivities().iterator().next(),
            activity2
        ));

        when(eventRepository.findById(1)).thenReturn(Optional.of(event));

        byte[] pdfBytes = reportPDFService.createEventDetailsReport(1, userPrincipal);

        try (PDDocument doc = PDDocument.load(pdfBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);

            assertTrue(text.contains("Opening"));
            assertTrue(text.contains("Welcome speech"));
            assertTrue(text.contains("Closing"));
            assertTrue(text.contains("Goodbye speech"));
        }
    }
}
