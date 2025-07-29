package rs.ac.uns.ftn.asd.Projekatsiit2024.controller.event;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.CreateEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.CreatedEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.DetailedEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.JoinedEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.MinimalEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventActivityValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventUserValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.reportPDF.PdfGenerationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.event.EventService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.reportPDF.ReportPDFService;


@RestController
@RequestMapping("/api/events")
public class EventController {
	
	@Autowired
	EventService eventService;
	@Autowired
	AuthentifiedUserRepository userRepo;
	@Autowired
	ReportPDFService reportPDFService;
	
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreatedEventDTO> createEvent(@AuthenticationPrincipal UserPrincipal userPrincipal, 
			@RequestBody CreateEventDTO event) throws EventValidationException, EventActivityValidationException {
		Event createdEvent = eventService.createEvent(userPrincipal, event);
		
		return ResponseEntity.ok(new CreatedEventDTO(createdEvent));
	}
	
	
	@PostMapping(value = "/{eventId}/join", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JoinedEventDTO> joinEvent(@AuthenticationPrincipal UserPrincipal userPrincipal, 
			@PathVariable Integer eventId) throws EventValidationException {
    	AuthentifiedUser user = userPrincipal.getUser();

		Event updatedEvent = eventService.joinEvent(eventId, user);
		
		return ResponseEntity.ok(new JoinedEventDTO(updatedEvent));
	}
	
  
	@GetMapping
	public ResponseEntity<Page<MinimalEventDTO>> readEventTypes(
			@PageableDefault(size = 10, sort = "id") Pageable pageable, 
			@AuthenticationPrincipal UserPrincipal userPrincipal) 
					throws EventUserValidationException {
		Page<MinimalEventDTO> events = eventService.readEvents(pageable, userPrincipal);

		return new ResponseEntity<Page<MinimalEventDTO>>(events, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DetailedEventDTO> getEventDetails(@AuthenticationPrincipal UserPrincipal userPrincipal, 
			@PathVariable Integer eventId) throws EventValidationException {
		
		Event event = eventService.getEventDetails(eventId, userPrincipal);
		
		return ResponseEntity.ok(new DetailedEventDTO(event));
	}
	
	
	@GetMapping(value = "/{eventId}/reports/details", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> getEventDetailsPDF(@AuthenticationPrincipal UserPrincipal userPrincipal, 
			@PathVariable Integer eventId) throws EventValidationException, PdfGenerationException {
		
		 byte[] pdfBytes = reportPDFService.createEventDetailsReport(eventId, userPrincipal);

		    return ResponseEntity.ok()
		            .header("Content-Disposition", "attachment; filename=event-details.pdf")
		            .contentType(MediaType.APPLICATION_PDF)
		            .body(pdfBytes);
	}
	
	
	@GetMapping(value = "/{eventId}/reports/statistics", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> getEventStatisticsPDF(@AuthenticationPrincipal UserPrincipal userPrincipal, 
			@PathVariable Integer eventId) throws EventValidationException, PdfGenerationException {
		
		 byte[] pdfBytes = reportPDFService.createEventStatisticsReport(eventId, userPrincipal);

		    return ResponseEntity.ok()
		            .header("Content-Disposition", "attachment; filename=event-details.pdf")
		            .contentType(MediaType.APPLICATION_PDF)
		            .body(pdfBytes);
	}
	
	
	
	
	
	@GetMapping(value = "/top5", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MinimalEventDTO>> getTop5Events(@AuthenticationPrincipal UserPrincipal userPrincipal) {
	    Integer userId = (userPrincipal != null) ? userPrincipal.getUser().getId() : null;

	    List<Event> events = eventService.getTop5OpenEvents(userId);

	    List<MinimalEventDTO> eventsDTO = events.stream()
	        .map(MinimalEventDTO::new)
	        .toList();

	    return ResponseEntity.ok(eventsDTO);
	}
    

    
    @GetMapping(value = "/rest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MinimalEventDTO>> GetAllEvents(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    	AuthentifiedUser user = userPrincipal.getUser();
		
		int userId= user.getId();

    	List<Event> events = eventService.getRestEvents(userId);
        
        List<MinimalEventDTO> eventsDTO = events.stream()
                .map(MinimalEventDTO::new)
                .toList();
  
        return new ResponseEntity<>(eventsDTO, HttpStatus.OK);
    }
    

    @GetMapping(value = "/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<MinimalEventDTO>> getPaginatedEvents(@AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

    	AuthentifiedUser user = userPrincipal.getUser();

        int userId = user.getId();

        Page<Event> eventsPage = eventService.getRestEventsPaginated(userId, page, size);
        Page<MinimalEventDTO> dtoPage = eventsPage.map(MinimalEventDTO::new);

        return ResponseEntity.ok(dtoPage);
    }

    

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<MinimalEventDTO>> getEventList(@AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "location", required = false) String location,
            @RequestParam(name = "numOfAttendees", required = false) Integer numOfAttendees,
            @RequestParam(name = "firstPossibleDate", required = false) String firstPossibleDate,
            @RequestParam(name = "lastPossibleDate", required = false) String lastPossibleDate,
            @RequestParam(name = "eventTypes", required = false) List<Integer> eventTypes,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "8") int size
    ) throws ParseException {

        Integer userId = null;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            UserDetails principal = (UserDetails) auth.getPrincipal();
            String email = principal.getUsername();
            AuthentifiedUser user = userRepo.findByEmail(email);
            userId = user.getId();
        }

        Page<Event> filteredEvents = eventService.getFilteredEvents(
                name, location, numOfAttendees,
                firstPossibleDate, lastPossibleDate, eventTypes,
                userId, page, size
        );

        Page<MinimalEventDTO> eventDTOs = filteredEvents.map(MinimalEventDTO::new);
        return new ResponseEntity<>(eventDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/organizer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MinimalEventDTO>> GetEventsForOrganizer(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        AuthentifiedUser user = userPrincipal.getUser();
        int userId = user.getId();

        List<Event> events = eventService.geteventsByOrganizerID(userId);

        List<MinimalEventDTO> eventsDTO = events.stream()
                .map(MinimalEventDTO::new)
                .toList();

        return new ResponseEntity<>(eventsDTO, HttpStatus.OK);
    }
    
    @GetMapping(value = "/organizer/updated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MinimalEventDTO>> GetEventsForOrganizerUpdated(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        AuthentifiedUser user = userPrincipal.getUser();
        int userId = user.getId();

        List<Event> events = eventService.geteventsByOrganizerIDUpdated(userId);

        List<MinimalEventDTO> eventsDTO = events.stream()
                .map(MinimalEventDTO::new)
                .toList();

        return new ResponseEntity<>(eventsDTO, HttpStatus.OK);
    }
}
