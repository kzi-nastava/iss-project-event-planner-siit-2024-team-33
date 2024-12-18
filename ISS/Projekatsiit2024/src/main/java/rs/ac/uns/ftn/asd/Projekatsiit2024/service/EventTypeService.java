package rs.ac.uns.ftn.asd.Projekatsiit2024.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.CreateEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.GetEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.UpdateEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.EventTypeRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferCategoryRepository;

@Service
public class EventTypeService {
	
	@Autowired
    private EventTypeRepository eventTypeRepository;
	
	@Autowired
	private OfferCategoryRepository offerCategoryRepository;
	
	
	public Collection<GetEventTypeDTO> getEventTypes() {
		Collection<GetEventTypeDTO> eventTypes = new ArrayList();
		for (EventType eventType : eventTypeRepository.findAll())
		{
			eventTypes.add(new GetEventTypeDTO(eventType));
		}
		return eventTypes;
	}
	
	@Transactional
    public EventType createEventType(CreateEventTypeDTO eventTypeDTO) {
        
		EventType eventType = new EventType();
        
        isCreatedEventTypeValid(eventTypeDTO);
        
        //creating event type
        eventType.setName(eventTypeDTO.getName());
        eventType.setDescription(eventTypeDTO.getDescription());
        eventType.setIsActive(eventTypeDTO.getIsActive());
        for (Integer id : eventTypeDTO.getRecommendedCategoriesIds()) {
        	Optional<OfferCategory> offerCategoryOptional = offerCategoryRepository.findById(id);
        	
        	if (offerCategoryOptional.isPresent()) {
        		OfferCategory offerCategory = offerCategoryOptional.get();
        		eventType.getRecommendedCategories().add(offerCategory);
        	}
        }
        
        return eventTypeRepository.saveAndFlush(eventType);
    }
	
	//TODO: validation of data
	private boolean isCreatedEventTypeValid(CreateEventTypeDTO eventTypeDTO) {
		
		return true;
	}
	
	
	@Transactional
    public EventType updateEventType(Integer eventTypeId, UpdateEventTypeDTO eventTypeDTO) {
        
		Optional<EventType> eventTypeOptional = eventTypeRepository.findById(eventTypeId);
		if (!eventTypeOptional.isPresent()) {
    		//TODO: will throw exception
    	}
		
		EventType eventType = eventTypeOptional.get();
        
        isUpdatedEventTypeValid(eventTypeDTO);
        
        //updating event type
        eventType.setDescription(eventTypeDTO.getDescription());
        eventType.setRecommendedCategories(new ArrayList<>());
        for (Integer id : eventTypeDTO.getRecommendedCategoriesIds()) {
        	Optional<OfferCategory> offerCategoryOptional = offerCategoryRepository.findById(id);
        	
        	if (offerCategoryOptional.isPresent()) {
        		OfferCategory offerCategory = offerCategoryOptional.get();
        		eventType.getRecommendedCategories().add(offerCategory);
        	}
        }
        
        return eventTypeRepository.saveAndFlush(eventType);
    }
	
	//TODO: validation of data
	private boolean isUpdatedEventTypeValid(UpdateEventTypeDTO eventTypeDTO) {
		
		return true;
	}
	
	
	@Transactional
    public EventType deleteEventType(Integer eventTypeId) {
        
		Optional<EventType> eventTypeOptional = eventTypeRepository.findById(eventTypeId);
		if (!eventTypeOptional.isPresent()) {
    		//TODO: will throw exception
    	}
		
		EventType eventType = eventTypeOptional.get();
		eventType.setIsDeleted(true);
        
        return eventTypeRepository.saveAndFlush(eventType);
    }
	
	
}
