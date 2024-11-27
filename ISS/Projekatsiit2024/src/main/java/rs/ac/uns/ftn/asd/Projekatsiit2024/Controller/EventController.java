package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.EventService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.FilterEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.PostEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.GetServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.PostServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.ServiceFilterDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation.GetServiceReservationDTO;

import java.security.Provider.Service;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

	@GetMapping("/top5")
	public ResponseEntity<List<Event>> GetTop5Events(){
		//TODO: Find and return top 5 events
		return ResponseEntity.ok(null);
	}
	
	@GetMapping
	public ResponseEntity<List<Event>> GetEventList(@RequestBody FilterEventDTO FilterParameters){
		//TODO: Find and return top 5 events
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/{id}")
	public ResponseEntity<Event> PostEvent(@PathVariable Integer id, @RequestBody PostEventDTO data){
		//TODO: Edit Event and return it's details
		return ResponseEntity.ok(null);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Event> PutEvent(@PathVariable Integer id, @RequestBody PostEventDTO data){
		//TODO: Edit Event and return it's details
		return ResponseEntity.ok(null);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Service> DeleteEvent(@PathVariable Integer id){
		return ResponseEntity.ok(null);
	}


    @GetMapping("/{eventId}/reservations")
    public ResponseEntity<List<GetServiceReservationDTO>> getAllReservationsForEvent(@PathVariable int eventId) {
        return ResponseEntity.ok(null);
    }
}
