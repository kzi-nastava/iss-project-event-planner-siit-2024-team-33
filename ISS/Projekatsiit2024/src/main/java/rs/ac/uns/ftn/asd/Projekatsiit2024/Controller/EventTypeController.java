package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.EventTypeRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.MinimalEventTypeDTO;

@RestController
@RequestMapping("/api/events/types")
public class EventTypeController {
	@Autowired
	private EventTypeRepository eventTypeRepo;
	
	@GetMapping
	public ResponseEntity getEventTypes() {
		List<EventType> types = eventTypeRepo.findAll();
		return ResponseEntity.ok(types.stream().map(type -> new MinimalEventTypeDTO(type)).toList());
	}
}
