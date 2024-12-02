package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.EventService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.FilterEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.GetEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.PostEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.UpdateEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation.GetServiceReservationDTO;

@RestController
@RequestMapping("/api/events")
public class EventController {
	
	@Autowired
	EventService eventService;
	
    @GetMapping(value = "/top5", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GetEventDTO>> GetTop5Events() {
        List<GetEventDTO> events = new ArrayList<>();

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GetEventDTO>> GetEventList(@RequestBody FilterEventDTO filterParameters) {
        List<GetEventDTO> events = new ArrayList<>();

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetEventDTO> PostEvent(@PathVariable Integer id, @RequestBody PostEventDTO data) {
        GetEventDTO createdEvent = new GetEventDTO();

        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetEventDTO> PutEvent(@PathVariable Integer id, @RequestBody UpdateEventDTO data) {
        GetEventDTO updatedEvent = new GetEventDTO();

        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> DeleteEvent(@PathVariable Integer id) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{eventId}/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GetServiceReservationDTO>> getAllReservationsForEvent(@PathVariable int eventId) {
        List<GetServiceReservationDTO> reservations = new ArrayList<>();

        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }
}
