package rs.ac.uns.ftn.asd.Projekatsiit2024.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.CreateEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.CreatedEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.FilterEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.GetEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.MinimalEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.UpdateEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.MinimalEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.GetServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation.GetServiceReservationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.EventService;


@RestController
@RequestMapping("/api/events")
public class EventController {
	
	@Autowired
	EventService eventService;
	@Autowired
	AuthentifiedUserRepository userRepo;
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreatedEventDTO> createEvent(@RequestBody CreateEventDTO eventDTO) throws Exception {
		
		Event event = eventService.createEvent(eventDTO);
		CreatedEventDTO savedEvent = new CreatedEventDTO(event);

		return new ResponseEntity<CreatedEventDTO>(savedEvent, HttpStatus.CREATED);
	}
	
	
    @GetMapping(value = "/top5", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MinimalEventDTO>> GetTop5Events() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails principal = (UserDetails) auth.getPrincipal();
		String email = principal.getUsername();
	
		AuthentifiedUser user = userRepo.findByEmail(email);
		
		int userId = user.getId();
        List<MinimalEventDTO> eventsDTO = new ArrayList<>();
        List<Event> events = eventService.getTop5OpenEvents(userId);
        
        for(Event ev:events) {
        	MinimalEventDTO minEve = new MinimalEventDTO(ev);
        	eventsDTO.add(minEve);
        }
        
        return new ResponseEntity<>(eventsDTO, HttpStatus.OK);
    }
    
    @GetMapping(value = "/rest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MinimalEventDTO>> GetAllEvents() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails principal = (UserDetails) auth.getPrincipal();
		String email = principal.getUsername();
	
		AuthentifiedUser user = userRepo.findByEmail(email);
		
		int userId= user.getId();

    	List<Event> events = eventService.getRestEvents(userId);
        
        List<MinimalEventDTO> eventsDTO = events.stream()
                .map(MinimalEventDTO::new)
                .toList();
  
        return new ResponseEntity<>(eventsDTO, HttpStatus.OK);
    }
    

    @GetMapping(value = "/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<MinimalEventDTO>> getPaginatedEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) auth.getPrincipal();
        String email = principal.getUsername();
        AuthentifiedUser user = userRepo.findByEmail(email);
        int userId = user.getId();

        Page<Event> eventsPage = eventService.getRestEventsPaginated(userId, page, size);
        Page<MinimalEventDTO> dtoPage = eventsPage.map(MinimalEventDTO::new);

        return ResponseEntity.ok(dtoPage);
    }

    
    @GetMapping(value="/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MinimalEventDTO>> GetEventList(@RequestParam(name="name" ,required=false)  String name,
												    		@RequestParam(name="location" ,required=false)  String location,
												    		@RequestParam(name="numOfAttendees" ,required=false)  Integer numOfAttendees,
												    		@RequestParam(name="firstPossibleDate" ,required=false)  String firstPossibleDate,
												    		@RequestParam(name="lastPossibleDate" ,required=false)  String lastPossibleDate,
												    		@RequestParam(name="eventTypes" ,required=false)  List<Integer> eventTypes) throws ParseException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails principal = (UserDetails) auth.getPrincipal();
		String email = principal.getUsername();
	
		AuthentifiedUser user = userRepo.findByEmail(email);
		
		int id = user.getId();
    	List<Event> events = eventService.getFilteredEvents(name, location, numOfAttendees, firstPossibleDate,
        													lastPossibleDate,eventTypes,id);

        List<MinimalEventDTO> eventsDTO = events.stream()
                .map(MinimalEventDTO::new)
                .toList();      
        
        return new ResponseEntity<>(eventsDTO, HttpStatus.OK);
    }

    /*@PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity PostEvent(@RequestBody PostEventDTO data) {
		try {
			Event e = null;
			if(data.getOrganizer().getId() != null) {
				e = eventService.createEvent(data.getName(), data.getDescription(), data.getPlace(),
						data.getLatitude(), data.getLongitude(),data.getDateOfEvent(), data.getEndOfEvent(),
						data.getNumOfAttendees(), data.getIsPrivate(), data.getPrice(), data.getPicture(),
						data.getOrganizer().getId(), data.getEventTypes());
			}
			return ResponseEntity.ok(new GetEventDTO(e));
		} catch (IllegalArgumentException err) {
			return ResponseEntity.badRequest().body(err.getMessage());
		}
    }*/
    
    

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetEventDTO> PutEvent(@PathVariable Integer id, @RequestBody UpdateEventDTO data) {
    	Event updatedEvent = eventService.editEvent(
                id,
                data.getName(),data.getDescription(),data.getPlace(),data.getLatitude(),
                data.getLongitude(),data.getDateOfEvent(),data.getEndOfEvent(),data.getNumOfAttendees(),
                data.getIsPrivate(),data.getPrice(),data.getPicture(),data.getEventTypes()
        );

        GetEventDTO updatedEventDTO = new GetEventDTO(updatedEvent);
        return new ResponseEntity<>(updatedEventDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> DeleteEvent(@PathVariable Integer id) {
        eventService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
     }
    
    @GetMapping(value = "/organizer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MinimalEventDTO>> GetEventsForOrganizer() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails principal = (UserDetails) auth.getPrincipal();
		String email = principal.getUsername();
	
		AuthentifiedUser user = userRepo.findByEmail(email);
		
		int userId= user.getId();
    	List<MinimalEventDTO> eventsDTO = new ArrayList<>();
        List<Event> events = eventService.geteventsByOrganizerID(userId);
        
        for (Event ev : events) {
            MinimalEventDTO minEve = new MinimalEventDTO(ev);
            eventsDTO.add(minEve);
        }

        return new ResponseEntity<>(eventsDTO, HttpStatus.OK);
    }

    
    		//Will do if needed.
//    @GetMapping(value = "/{eventId}/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<GetServiceReservationDTO>> getAllReservationsForEvent(@PathVariable int eventId) {
//        List<GetServiceReservationDTO> reservations = new ArrayList<>();
//
//        return new ResponseEntity<>(reservations, HttpStatus.OK);
//    }
    
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
}
