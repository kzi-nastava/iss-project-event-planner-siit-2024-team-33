package rs.ac.uns.ftn.asd.Projekatsiit2024.controller.event;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.CreateEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.CreatedEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.GetEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.MinimalEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.UpdateEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.UpdatedEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offerCategory.MinimalOfferCategoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventTypeValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventTypeRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.event.EventTypeService;

@RestController
@RequestMapping("/api/eventTypes")
public class EventTypeController {
	@Autowired
	private EventTypeRepository eventTypeRepo;
	
	@Autowired 
	EventTypeService eventTypeService;
	
	@GetMapping("/active")
	public ResponseEntity<List<MinimalEventTypeDTO>> getEventTypes() {
		List<EventType> types = eventTypeRepo.getActiveEventTypes();
		List<MinimalEventTypeDTO> minTypes = types.stream().map(type -> new MinimalEventTypeDTO(type)).toList();
		return ResponseEntity.ok(minTypes);
	}
	
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<GetEventTypeDTO>> readEventTypes(
			@PageableDefault(size = 10, sort = "id") Pageable pageable) {
		Page<GetEventTypeDTO> eventTypes = eventTypeService.readEventTypes(pageable);

		return new ResponseEntity<Page<GetEventTypeDTO>>(eventTypes, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<GetEventTypeDTO>> readProviderEventTypes(
			@AuthenticationPrincipal UserPrincipal userPrincipal,
			@PageableDefault(size = 10) Pageable pageable) {
		Page<GetEventTypeDTO> eventTypes = eventTypeService.readProviderEventTypes(userPrincipal, pageable);

		return new ResponseEntity<Page<GetEventTypeDTO>>(eventTypes, HttpStatus.OK);
	}
	
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreatedEventTypeDTO> createEventType(@RequestBody CreateEventTypeDTO eventTypeDTO) 
			throws EventTypeValidationException {
		EventType eventType = eventTypeService.createEventType(eventTypeDTO);

		return new ResponseEntity<CreatedEventTypeDTO>(new CreatedEventTypeDTO(eventType), HttpStatus.CREATED);
	}
	
	
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UpdatedEventTypeDTO> updateEventType(@RequestBody UpdateEventTypeDTO eventTypeDTO, @PathVariable Integer id)
			throws EventTypeValidationException {
		EventType eventType = eventTypeService.updateEventType(id, eventTypeDTO);

		return new ResponseEntity<UpdatedEventTypeDTO>(new UpdatedEventTypeDTO(eventType), HttpStatus.OK);
	}
	
	@PutMapping(value = "/{id}/activation")
	public ResponseEntity<UpdatedEventTypeDTO> activateEventType(@PathVariable("id") Integer id) throws EventTypeValidationException {
		EventType eventType = eventTypeService.activateEventType(id);
		return ResponseEntity.ok(new UpdatedEventTypeDTO(eventType));
	}
	
	
	@PutMapping(value = "/{id}/deactivation")
	public ResponseEntity<UpdatedEventTypeDTO> deactivateEventType(@PathVariable("id") Integer id) throws EventTypeValidationException {
		EventType eventType = eventTypeService.deactivateEventType(id);
		return ResponseEntity.ok(new UpdatedEventTypeDTO(eventType));
	}
	
	
	@GetMapping("/exists")
	public ResponseEntity<Boolean> checkIfExists(@RequestParam String name) {
		boolean exists = eventTypeService.existsByName(name);
		return ResponseEntity.ok(exists);
	}
	
	
	@GetMapping("/{eventTypeId}/offerCategories")
	public ResponseEntity<List<MinimalOfferCategoryDTO>> getRecommendedCategories(@PathVariable Integer eventTypeId) 
			throws EventTypeValidationException {
	    Set<OfferCategory> categories = eventTypeService.getRecommendedByEventType(eventTypeId);
	    List<MinimalOfferCategoryDTO> dtos = categories.stream().map(MinimalOfferCategoryDTO::new).toList();
	    return ResponseEntity.ok(dtos);
	}
}