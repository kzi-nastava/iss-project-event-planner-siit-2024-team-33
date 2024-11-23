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

import java.security.Provider.Service;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

	@GetMapping("/top5")
	public ResponseEntity<List<Event>> GetTop5Events(){
		//TODO: Find and return top 5 events
		return ResponseEntity.ok(null);
	}
	
	@GetMapping("/list")
	public ResponseEntity<List<Event>> GetEventList(@RequestBody FilterEventDTO FilterParameters){
		//TODO: Find and return top 5 events
		return ResponseEntity.ok(null);
	}
	
	@PostMapping
	public ResponseEntity<Event> PostEvent(@RequestBody PostEventDTO data){
		//TODO: Edit Event and return it's details
		return ResponseEntity.ok(null);
	}
	
	@PutMapping
	public ResponseEntity<Event> PutEvent(@RequestBody PostEventDTO data){
		//TODO: Edit Event and return it's details
		return ResponseEntity.ok(null);
	}
	
	@DeleteMapping
	public ResponseEntity<Service> DeleteEvent(@RequestAttribute() Integer id){
		return ResponseEntity.ok(null);
	}

	
	
    
}
