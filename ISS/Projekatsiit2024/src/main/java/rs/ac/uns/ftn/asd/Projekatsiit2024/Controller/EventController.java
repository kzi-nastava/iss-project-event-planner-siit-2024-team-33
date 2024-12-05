package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.BudgetItem;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.EventService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.FilterEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.GetEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.MinimalEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.PostEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.UpdateEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.GetServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation.GetServiceReservationDTO;

@RestController
@RequestMapping("/api/events")
public class EventController {
	
	@Autowired
	EventService eventService;
	
    @GetMapping(value = "/top5", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MinimalEventDTO>> GetTop5Events(@RequestParam Integer id) {
        List<MinimalEventDTO> eventsDTO = new ArrayList<>();
        List<Event> events = eventService.getTop5OpenEvents(id);
        
        for(Event ev:events) {
        	MinimalEventDTO minEve = new MinimalEventDTO(ev);
        	eventsDTO.add(minEve);
        }
        
        return new ResponseEntity<>(eventsDTO, HttpStatus.OK);
    }
    
    @GetMapping(value = "/rest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MinimalEventDTO>> GetAllEvents(@RequestParam Integer id) {
        List<Event> events = eventService.getRestEvents(id);
        
        List<MinimalEventDTO> eventsDTO = events.stream()
                .map(MinimalEventDTO::new)
                .toList();
  
        return new ResponseEntity<>(eventsDTO, HttpStatus.OK);
    }

    @GetMapping(value="/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MinimalEventDTO>> GetEventList(@ModelAttribute  FilterEventDTO fP, @RequestParam Integer id) throws ParseException {
        List<Event> events = eventService.getFilteredEvents(fP.getName(), fP.getLocation(),
        													fP.getNumOfAttendees(), fP.getFirstPossibleDate(),
        													fP.getLastPossibleDate(), fP.getEventTypes(), id);

        List<MinimalEventDTO> eventsDTO = events.stream()
                .map(MinimalEventDTO::new)
                .toList();      
        
        return new ResponseEntity<>(eventsDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
    }

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
    
    @GetMapping(value = "/organizer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MinimalEventDTO>> GetEventsForOrganizer(@PathVariable Integer id) {
        List<MinimalEventDTO> eventsDTO = new ArrayList<>();
        List<Event> events = eventService.geteventsByOrganizerID(id);
        
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
    
}
