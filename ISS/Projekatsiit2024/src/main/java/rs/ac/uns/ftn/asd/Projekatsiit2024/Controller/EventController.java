package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.FilterEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.GetEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.PostEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.UpdateEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation.GetServiceReservationDTO;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @GetMapping(value = "/top5", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GetEventDTO>> GetTop5Events() {
        List<GetEventDTO> events = new ArrayList<>();
        
        // Simulate top 5 events
        for (int i = 1; i <= 5; i++) {
            GetEventDTO event = new GetEventDTO();
            event.id = i;
            event.Name = "Top Event " + i;
            event.Description = "Description for top event " + i;
            event.Place = "Place " + i;
            event.DateOfEvent = new Date(System.currentTimeMillis());
            event.NumOfAttendees = 100 + i;
            events.add(event);
        }

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GetEventDTO>> GetEventList(@RequestBody FilterEventDTO filterParameters) {
        List<GetEventDTO> events = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            GetEventDTO event = new GetEventDTO();
            event.id = i;
            event.Name = "Top Event " + i;
            event.Description = "Description for top event " + i;
            event.Place = "Place " + i;
            event.DateOfEvent = new Date(System.currentTimeMillis());
            event.NumOfAttendees = 100 + i;
            events.add(event);
        }

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetEventDTO> PostEvent(@PathVariable Integer id, @RequestBody PostEventDTO data) {
        GetEventDTO createdEvent = new GetEventDTO();
        
        createdEvent.id = 1;
        createdEvent.Name = "Top Event " + 1;
        createdEvent.Description = "Description for top event " + 1;
        createdEvent.Place = "Place " + 1;
        createdEvent.DateOfEvent = new Date(System.currentTimeMillis());
        createdEvent.NumOfAttendees = 100 + 1;

        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetEventDTO> PutEvent(@PathVariable Integer id, @RequestBody UpdateEventDTO data) {
        GetEventDTO updatedEvent = new GetEventDTO();

        updatedEvent.id = 1;
        updatedEvent.Name = "Top Event " + 1;
        updatedEvent.Description = "Description for top event " + 1;
        updatedEvent.Place = "Place " + 1;
        updatedEvent.DateOfEvent = new Date(System.currentTimeMillis());
        updatedEvent.NumOfAttendees = 100 + 1;

        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> DeleteEvent(@PathVariable Integer id) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{eventId}/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GetServiceReservationDTO>> getAllReservationsForEvent(@PathVariable int eventId) {
        List<GetServiceReservationDTO> reservations = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            GetServiceReservationDTO reservation = new GetServiceReservationDTO(null);
            reservations.add(reservation);
        }

        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }
}
