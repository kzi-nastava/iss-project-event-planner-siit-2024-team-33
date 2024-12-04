package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.websocket.server.PathParam;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.EventTypeRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.MinimalEventTypeDTO;

@RestController
@RequestMapping("api/events/types")
public class EventTypeController {
	@Autowired
	private EventTypeRepository eventTypeRepo;
	
	@GetMapping
	public ResponseEntity getEventTypes(@RequestParam("isActive") Boolean isActive) {
		List<EventType> types = new ArrayList<EventType>();
		if(isActive == null)
			types = eventTypeRepo.findAll();
		if(isActive)
			types = eventTypeRepo.getActiveEventTypes();
		List<MinimalEventTypeDTO> minTypes = types.stream().map(type -> new MinimalEventTypeDTO(type)).toList();
		return ResponseEntity.ok(minTypes);
	}
}
