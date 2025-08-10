package rs.ac.uns.ftn.asd.Projekatsiit2024.service.event;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.CreateEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.GetEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.UpdateEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventTypeValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferCategoryRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventTypeRepository;

@Service
public class EventTypeService {
	
	@Autowired
    private EventTypeRepository eventTypeRepository;
	
	@Autowired
	private OfferCategoryRepository offerCategoryRepository;
	
	
	
	public Page<GetEventTypeDTO> readEventTypes(Pageable pageable) {
		return eventTypeRepository.findAll(pageable).map(GetEventTypeDTO::new);
	}
	
	
	public Page<GetEventTypeDTO> readProviderEventTypes(UserPrincipal userPrincipal, Pageable pageable) {
		return eventTypeRepository.findDistinctByProviderId(userPrincipal.getUser().getId(), pageable).map(GetEventTypeDTO::new);
	}

	
	@Transactional(propagation = Propagation.REQUIRED)
    public EventType createEventType(CreateEventTypeDTO eventTypeDTO) throws EventTypeValidationException {
        
		EventType eventType = new EventType();
        eventType.setName(eventTypeDTO.getName());
        eventType.setDescription(eventTypeDTO.getDescription());
        eventType.setIsActive(true);
        
        //adding offer categories which are recommended for event type
        Set<OfferCategory> offerCategories = new HashSet<>();
        for (Integer id : eventTypeDTO.getRecommendedCategoriesIds()) {
        	
        	Optional<OfferCategory> offerCategory = offerCategoryRepository.getAvailableOfferCategory(id);
        	if (offerCategory.isEmpty())
        		throw new EventTypeValidationException("No such offer category can"
        				+ " be used for creation of event type.");
        	
        	offerCategories.add(offerCategory.get());
        }
        eventType.setRecommendedCategories(offerCategories);
        
        //validating event type
        isCreatedEventTypeValid(eventType);
        
        return eventTypeRepository.save(eventType);
    }
	
	private boolean isCreatedEventTypeValid(EventType eventType) throws EventTypeValidationException {
		
		if (!Pattern.matches("^.{5,24}$", eventType.getName()))
			throw new EventTypeValidationException("Name can't be under 5 or over 24 characters long.");
		
		if (eventTypeRepository.findByName(eventType.getName()).isPresent())
            throw new EventTypeValidationException("That event type name is already taken.");
    	
    	if (!Pattern.matches("^.{5,80}$", eventType.getDescription()))
			throw new EventTypeValidationException("Description can't be under 5 or over 80 characters long.");
		
		return true;
	}
	
	
	
	
	@Transactional(propagation = Propagation.REQUIRED)
    public EventType updateEventType(Integer eventTypeId, UpdateEventTypeDTO eventTypeDTO) 
		throws EventTypeValidationException {
        
		Optional<EventType> optionalEventType = eventTypeRepository.findById(eventTypeId);
		if (optionalEventType.isEmpty()) {
			throw new EventTypeValidationException("No event type exists with such id.");
    	}
		if (optionalEventType.get().getId() == 1) {
			throw new EventTypeValidationException("You cannot change default event type.");
    	}
        
        EventType eventType = optionalEventType.get();
        eventType.setDescription(eventTypeDTO.getDescription());
        //adding offer categories which are recommended for event type
        Set<OfferCategory> offerCategories = new HashSet<>();
        for (Integer id : eventTypeDTO.getRecommendedCategoriesIds()) {
        	
        	Optional<OfferCategory> offerCategory = offerCategoryRepository.getAvailableOfferCategory(id);
        	if (offerCategory.isEmpty())
        		throw new EventTypeValidationException("No such offer category can"
        				+ " be used for update of event type.");
        	
        	offerCategories.add(offerCategory.get());
        }
        eventType.setRecommendedCategories(offerCategories);
        
        //validating event type
        isUpdatedEventTypeValid(eventType);
        
        return eventTypeRepository.save(eventType);
    }
	
	private boolean isUpdatedEventTypeValid(EventType eventType) throws EventTypeValidationException {
		
		if (eventType.getDescription() == null || !Pattern.matches("^.{5,80}$", eventType.getDescription()))
			throw new EventTypeValidationException("Description can't be under 5 or over 80 characters long.");
		
		return true;
	}
	
	
	
	
	@Transactional(propagation = Propagation.REQUIRED)
    public EventType activateEventType(Integer eventTypeId) throws EventTypeValidationException {
        
		Optional<EventType> optionalEventType = eventTypeRepository.findById(eventTypeId);
		if (optionalEventType.isEmpty()) {
			throw new EventTypeValidationException("No event type exists with such id.");
    	}
		if (optionalEventType.get().getId() == 1) {
			throw new EventTypeValidationException("You cannot change default event type.");
    	}
		
		EventType eventType = optionalEventType.get();
		if (eventType.getIsActive() == true) {
			throw new EventTypeValidationException("Event type is already active.");
		}
		eventType.setIsActive(true);
        
        return eventTypeRepository.save(eventType);
    }
	
	
	
	
	@Transactional(propagation = Propagation.REQUIRED)
    public EventType deactivateEventType(Integer eventTypeId) throws EventTypeValidationException {
        
		Optional<EventType> optionalEventType = eventTypeRepository.findById(eventTypeId);
		if (optionalEventType.isEmpty()) {
			throw new EventTypeValidationException("No event type exists with such id.");
    	}
		if (optionalEventType.get().getId() == 1) {
			throw new EventTypeValidationException("You cannot change default event type.");
    	}
		
		EventType eventType = optionalEventType.get();
		if (eventType.getIsActive() == false) {
			throw new EventTypeValidationException("Event type is already not active.");
		}
		eventType.setIsActive(false);
        
        return eventTypeRepository.save(eventType);
    }


	
	public boolean existsByName(String name) {
		return eventTypeRepository.existsByNameIgnoreCase(name.trim());
	}

	

	public Set<OfferCategory> getRecommendedByEventType(Integer eventTypeId) 
			throws EventTypeValidationException {
		Optional<EventType> optionalEventType = eventTypeRepository.findById(eventTypeId);
		if (optionalEventType.isEmpty() || optionalEventType.get().getIsActive() == false) {
			throw new EventTypeValidationException("No event type exists with such id.");
    	}
		
		EventType eventType = optionalEventType.get();
		Set<OfferCategory> availableCategories = eventType.getRecommendedCategories()
	    .stream()
	    .filter(oc -> Boolean.TRUE.equals(oc.getIsEnabled()) && Boolean.TRUE.equals(oc.getIsAccepted()))
	    .collect(Collectors.toSet());
		return availableCategories;
	}
}