package rs.ac.uns.ftn.asd.Projekatsiit2024.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.CreateEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.CreatedEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.GetEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.MinimalEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.UpdateEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.UpdatedEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.EventTypeRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.EventTypeService;

@RestController
@RequestMapping("/api/events/types")
public class EventTypeController {
	@Autowired
	private EventTypeRepository eventTypeRepo;
	
	@Autowired 
	EventTypeService eventTypeService;
	
	
	@GetMapping("/active")
	public ResponseEntity getEventTypes() {
		List<EventType> types = eventTypeRepo.getActiveEventTypes();
		List<MinimalEventTypeDTO> minTypes = types.stream().map(type -> new MinimalEventTypeDTO(type)).toList();
		return ResponseEntity.ok(minTypes);
	}
	
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<GetEventTypeDTO>> getUsers() {
		Collection<GetEventTypeDTO> eventTypes = new ArrayList<>();
		eventTypes = eventTypeService.getEventTypes();

		return new ResponseEntity<Collection<GetEventTypeDTO>>(eventTypes, HttpStatus.OK);
	}
	
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreatedEventTypeDTO> createEventType(@RequestBody CreateEventTypeDTO eventTypeDTO) {
		
		EventType eventType = eventTypeService.createEventType(eventTypeDTO);
		CreatedEventTypeDTO savedEventType = new CreatedEventTypeDTO(eventType);

		return new ResponseEntity<CreatedEventTypeDTO>(savedEventType, HttpStatus.CREATED);
	}
	
	
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UpdatedEventTypeDTO> updateEventType(@RequestBody UpdateEventTypeDTO eventTypeDTO, @PathVariable Integer id)
			throws Exception {
		
		EventType eventType = eventTypeService.updateEventType(id, eventTypeDTO);
		UpdatedEventTypeDTO updatedEventType = new UpdatedEventTypeDTO(eventType);

		return new ResponseEntity<UpdatedEventTypeDTO>(updatedEventType, HttpStatus.OK);
	}
	
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteEventType(@PathVariable("id") Integer id) {
		EventType eventType = eventTypeService.deleteEventType(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
}
