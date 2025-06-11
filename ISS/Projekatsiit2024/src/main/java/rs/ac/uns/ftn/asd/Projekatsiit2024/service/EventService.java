package rs.ac.uns.ftn.asd.Projekatsiit2024.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.MinimalEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.EventTypeRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.OrganizerRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.ProviderRepository;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.CreateEventDTO;

@Service
public class EventService {
	@Autowired
    private EventRepository eventRepository;
	@Autowired
	private AuthentifiedUserRepository userRepo;
    @Autowired
    private OrganizerRepository organRepo;
    @Autowired
    private EventTypeRepository eventTypeRepository;
    
    
    private void validateEventArguments(
            String name,
            String place,
            LocalDateTime startOfEvent,
            LocalDateTime endOfEvent,
            Integer organizerId,
            Integer price,
            List<EventType> eventTypes) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("");
        }
        
        if (price == null || price<0) {
            throw new IllegalArgumentException("");
        }
        
        if (place == null || place.isBlank()) {
            throw new IllegalArgumentException("");
        }

        if (startOfEvent == null || startOfEvent.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("");
        }

        if (endOfEvent != null && endOfEvent.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("");
        }

        if (organizerId == null) {
            throw new IllegalArgumentException("");
        }

        if (eventTypes == null || eventTypes.isEmpty()) {
            throw new IllegalArgumentException("");
        }

        if (eventTypes.stream().anyMatch(eventType -> !eventType.getIsActive())) {
            throw new IllegalArgumentException("");
        }
    }

    public List<Event> getTop5OpenEvents(Integer id) {
        Optional<AuthentifiedUser> optionalUser = userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("");
        }
        
        AuthentifiedUser user = optionalUser.get();
        List<AuthentifiedUser> blockedUsers = user.getBlockedUsers();

        List<Event> allEvents = eventRepository.findAll();
        String city = user.getCity();
        List<Event> filteredEvents = allEvents.stream()
                .filter(event -> city.equalsIgnoreCase(event.getPlace()))
                .filter(event -> !Boolean.TRUE.equals(event.getItsJoever()))
                .filter(event -> event.getOrganizer() == null || !blockedUsers.contains(event.getOrganizer()))
                .sorted((e1, e2) -> Integer.compare(e2.getNumOfAttendees(), e1.getNumOfAttendees()))
                .limit(5)
                .toList();
        return filteredEvents;
    }
    
    //TODO: Check if to get events for the city or ALL events.
    public List<Event> getRestEvents(Integer id) {
        List<Event> allEvents = eventRepository.findAll();
        List<Event> events = getRestEvents(allEvents, id);

        return events;
    }
    
    //Paginated like the ones above
    //Might filter the ones you MIGHT like more to be in the first rows
    public Page<Event> getRestEventsPaginated(int userId, int page, int size) {
        List<Event> allEvents = eventRepository.findAll();
        List<Event> events = getRestEvents(allEvents, userId);
        int start = page*size;
        int end = Math.min((start+size), events.size());
        
        List<Event> paginatedEvents = events.subList(start, end);
    	
        return new PageImpl<>(paginatedEvents, PageRequest.of(page, size), events.size());
    }
    
    public List<Event> getFilteredEvents(String name, String location, Integer numberOfAttendees, String before, String after, List<Integer> eventTypes, Integer id) throws java.text.ParseException {
        List<Event> allevents = eventRepository.findAll();

        List<Event> events = getRestEvents(allevents, id);
        

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        LocalDate beforeDate = (before == null || before.isEmpty())? null : LocalDate.parse(before);
        LocalDate afterDate = (after == null || after.isEmpty())? null : LocalDate.parse(after);

        if (name != null && !name.isEmpty()) {
            events = events.stream()
                .filter(event -> event.getName() != null && event.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
        }

        if (location != null && !location.isEmpty()) {
            events = events.stream()
                .filter(event -> event.getPlace() != null && event.getPlace().toLowerCase().contains(location.toLowerCase()))
                .toList();
        }

        if (numberOfAttendees > 0) {
            events = events.stream()
                .filter(event -> event.getNumOfAttendees() >= numberOfAttendees)
                .toList();
        }

        if (beforeDate != null) {
            events = events.stream()
                .filter(event -> event.getDateOfEvent() != null && event.getDateOfEvent().toLocalDate().isBefore(beforeDate))
                .toList();
        }

        if (afterDate != null) {
            events = events.stream()
                .filter(event -> event.getDateOfEvent() != null && event.getDateOfEvent().toLocalDate().isAfter(afterDate))
                .toList();
        }
        
        if (eventTypes != null && !eventTypes.isEmpty()) {
            events = events.stream()
                .filter(event -> event.getEventTypes() != null && event.getEventTypes().stream().anyMatch(eventTypes::contains))
                .toList();
        }
        
        events = events.stream().limit(10).toList();

        return events;
    }

    public void hidePassedEvents() {
        List<Event> allEvents = eventRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        allEvents.stream()
                .filter(event -> event.getDateOfEvent() != null && event.getDateOfEvent().toLocalDate().isBefore(currentDate))
                .forEach(event -> {
                    event.setItsJoever(true);
                    eventRepository.save(event);
                });
    }


    public List<Event> getAllOpenEvents(String city, String searchQuery, String sortBy) {
//        return eventRepository.findAllByCityAndIsPrivateFalse(city, searchQuery, sortBy);
    	return null;
    }
    
    
    @Transactional
    public Event createEvent(CreateEventDTO eventDTO) {
        Organizer organizer = organRepo.findById(eventDTO.getOrganizerId())
                .orElseThrow(() -> new RuntimeException(""));
        
        
        //TODO: data validation

        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDescription(eventDTO.getDescription());
        event.setNumOfAttendees(eventDTO.getNumOfAttendees());
        event.setIsPrivate(eventDTO.getIsPrivate());
        event.setPlace(eventDTO.getPlace());
        event.setLatitude(eventDTO.getLatitude());
        event.setLongitude(eventDTO.getLongitude());
        event.setDateOfEvent(eventDTO.getDateOfEvent());
        event.setEndOfEvent(eventDTO.getEndOfEvent());
        event.setItsJoever(false);
        event.setOrganizer(organizer);
        event.setPicture(eventDTO.getPicture());
        event.setPrice(eventDTO.getPrice());
        
        ArrayList<EventType> eventTypes = new ArrayList<>(); 
        for (Integer id : eventDTO.getEventTypesId()) {
        	Optional<EventType> eventTypeOptional = eventTypeRepository.findById(id);
        	
        	if (eventTypeOptional.isPresent()) {
        		EventType eventType = eventTypeOptional.get();
        		event.getEventTypes().add(eventType);
        	}
        }
        
        event.setEventTypes(eventTypes);
        
        eventRepository.save(event);
        return event;
    }

    public Event createAndFlushEvent(CreateEventDTO eventDTO) {

        Event event = createEvent(eventDTO);
        eventRepository.flush();
        return event;
    }

    public Event createSuggestion(
            String name,
            String description,
            String place,
            Double latitude,
            Double longitude,
            LocalDateTime startOfEvent,
            LocalDateTime endOfEvent,
            int numOfAttendees,
            Boolean isPrivate,
            Integer price,
            String picture,
            Integer organizerId,
            List<EventType> eventTypes) {

        validateEventArguments(name, place, startOfEvent, endOfEvent,price, organizerId, eventTypes);

        Organizer organizer = organRepo.findById(organizerId)
                .orElseThrow(() -> new RuntimeException(""));

        Event event = new Event();
        event.setName(name);
        event.setDescription(description);
        event.setPlace(place);
        event.setLatitude(latitude);
        event.setLongitude(longitude);
        event.setDateOfEvent(startOfEvent);
        event.setEndOfEvent(endOfEvent);
        event.setNumOfAttendees(numOfAttendees);
        event.setIsPrivate(isPrivate);
        event.setPrice(price);
        event.setPicture(picture);
        event.setOrganizer(organizer);
        event.setEventTypes(eventTypes);
        event.setItsJoever(false);
        eventRepository.save(event);
        return event;
    }
    
    public Event editEvent(
            Integer id,
            String name,
            String description,
            String place,
            Double latitude,
            Double longitude,
            LocalDateTime startOfEvent,
            LocalDateTime endOfEvent,
            int numOfAttendees,
            Boolean isPrivate,
            Integer price,
            String picture,
            List<EventType> eventTypes) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event with ID " + id + " not found"));

        if (name != null) event.setName(name);
        if (description != null) event.setDescription(description);
        if (place != null) event.setPlace(place);
        if (latitude != null) event.setLatitude(latitude);
        if (longitude != null) event.setLongitude(longitude);
        if (startOfEvent != null) event.setDateOfEvent(startOfEvent);
        if (endOfEvent != null) event.setEndOfEvent(endOfEvent);
        event.setNumOfAttendees(numOfAttendees);
        if (isPrivate != null) event.setIsPrivate(isPrivate);
        if (price != null) event.setPrice(price);
        if (picture != null) event.setPicture(picture);
        if (eventTypes != null) event.setEventTypes(eventTypes);

        eventRepository.save(event);
        return event;
    }
    public void deleteEvent(Integer id) {
        Optional<Event> event = eventRepository.findById(id);
        
        if(event.isEmpty()) {
        	throw new IllegalArgumentException("");
        }
    	
        eventRepository.delete(event.get());
    }
    
    public List<Event> geteventsByOrganizerID(Integer id){
    	List<Event> events = eventRepository.findByOrganizerId(id);
    	
    	return events;
    }
    
    
    private List<Event> getRestEvents(List<Event> allEvents, Integer id) {
        Optional<AuthentifiedUser> optionalUser = userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("");
        }
        
        AuthentifiedUser user = optionalUser.get();
        List<AuthentifiedUser> blockedUsers = user.getBlockedUsers();

        String city = user.getCity();
        
    	
    	List<Event> top5Events = allEvents.stream()
                .filter(event -> city.equalsIgnoreCase(event.getPlace()))
                .filter(event -> !Boolean.TRUE.equals(event.getItsJoever()))
                .filter(event -> event.getOrganizer() == null || !blockedUsers.contains(event.getOrganizer()))
                .sorted((e1, e2) -> Integer.compare(e2.getNumOfAttendees(), e1.getNumOfAttendees()))
                .limit(5)
                .toList();
        
        List<Event> events = allEvents.stream()
//              .filter(event -> city.equalsIgnoreCase(event.getPlace()))
              .filter(event -> !Boolean.TRUE.equals(event.getItsJoever()))
              .filter(event -> event.getOrganizer() == null || !blockedUsers.contains(event.getOrganizer()))
              .filter(event -> !top5Events.contains(event))
              .sorted((e1, e2) -> Integer.compare(e2.getNumOfAttendees(), e1.getNumOfAttendees()))
              .toList(); 
    	
    	
    	return events;
    }

}
