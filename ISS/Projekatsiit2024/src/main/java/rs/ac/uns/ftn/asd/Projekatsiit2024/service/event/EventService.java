package rs.ac.uns.ftn.asd.Projekatsiit2024.service.event;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventActivity;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventTypeRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.CreateEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventActivity.CreateEventActivityDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventActivityValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventValidationException;

@Service
public class EventService {
	
	@Autowired
    private EventRepository eventRepository;
	@Autowired
	private AuthentifiedUserRepository userRepo;
    @Autowired
    private EventTypeRepository eventTypeRepository;
    
    
    @Transactional(propagation = Propagation.REQUIRED)
    public Event createEvent(UserPrincipal userPrincipal, CreateEventDTO eventDTO) throws 
    	EventValidationException, EventActivityValidationException {
        //organizer which creates the event
		Organizer organizer = (Organizer) userPrincipal.getUser();
		
		//putting essential data into event
		Event event = this.createSimpleEvent(organizer, eventDTO);
        
        //TODO:for budget and reservations
        //TODO:for private invitations
        
        //making activities for an event
        Set<EventActivity> eventActivities = new HashSet<>();
        EventActivity eventActivity = new EventActivity();
        for (CreateEventActivityDTO eventActivityDTO : eventDTO.getEventActivities()) {
        	eventActivity = this.createEventActivity(eventActivityDTO, event);
        	eventActivities.add(eventActivity);
        }
        event.setEventActivities(eventActivities);
        
        Event createdEvent = eventRepository.save(event);
        
        return createdEvent;
    }
    
    
    private Event createSimpleEvent(Organizer organizer, CreateEventDTO eventDTO) throws 
    	EventValidationException {
    	
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
		event.setOrganizer(organizer);
        //event type for event
        Optional<EventType> eventType = eventTypeRepository.findById(eventDTO.getEventTypeId());
        if (eventType.isEmpty() && !eventType.get().getIsActive())
        	throw new EventValidationException("No such event type can be used to create an event.");
        event.setEventType(eventType.get());
        
        //check event data
        isEventDataCorrect(event);
        
        return event;
    }
    
    
    private boolean isEventDataCorrect(Event event) throws EventValidationException {
    	if (!Pattern.matches("^.{5,50}$", event.getName()))
			throw new EventValidationException("Name can't be less than 5 or over 50 characters long.");
    	
    	if (!Pattern.matches("^.{5,800}$", event.getDescription()))
			throw new EventValidationException("Description can't be less than 5 or over 800 characters long.");
    	
    	if (event.getNumOfAttendees() < 0)
    		throw new EventValidationException("Number of attendees can't be less than 0.");
    	
    	if (!Pattern.matches("^[A-Za-z][A-Za-z\\-\\' ]*[A-Za-z], [A-Za-z][A-Za-z\\-\\' ]*[A-Za-z]$", event.getPlace()) || event.getPlace().length() > 150)
    	    throw new EventValidationException("Place must be in the format 'City, Country' with no leading/trailing spaces and only letters, spaces, hyphens, or apostrophes.");
    	
    	if (event.getDateOfEvent().isBefore(LocalDateTime.now()) && 
    			event.getEndOfEvent().isBefore(LocalDateTime.now()))
    		throw new EventValidationException("Event can't be in the past.");
    	
    	if (event.getDateOfEvent().isAfter(event.getEndOfEvent())) {
    		throw new EventValidationException("Event starting time can't be after ending time.");
    	}
    	
    	//TODO:latitude and longtitude validation
    	
    	return true;
    }
    
    
    private EventActivity createEventActivity(CreateEventActivityDTO eventActivityDTO, Event event) 
    		throws EventActivityValidationException {
    	
    	EventActivity eventActivity = new EventActivity();
    	eventActivity.setName(eventActivityDTO.getName());
    	eventActivity.setDescription(eventActivityDTO.getDescription());
    	eventActivity.setStartingTime(eventActivityDTO.getStartingTime());
    	eventActivity.setEndingTime(eventActivityDTO.getEndingTime());
    	eventActivity.setLocation(eventActivityDTO.getLocation());
    	eventActivity.setEvent(event);
    	
    	isEventActivityDataCorrect(eventActivity);
    	
    	return eventActivity; 
    }
    
    
    private boolean isEventActivityDataCorrect(EventActivity eventActivity) throws EventActivityValidationException {
    	if (!Pattern.matches("^.{5,50}$", eventActivity.getName()))
			throw new EventActivityValidationException("Name can't be less than 5 or over 50 characters long.");
    	
    	if (!Pattern.matches("^.{5,80}$", eventActivity.getDescription()))
			throw new EventActivityValidationException("Description can't be less than 5 or over 80 characters long.");
    	
    	if (eventActivity.getEvent().getDateOfEvent().isAfter(eventActivity.getStartingTime()) || 
    			eventActivity.getEvent().getEndOfEvent().isBefore(eventActivity.getStartingTime())) 
    		throw new EventActivityValidationException("Starting time of event activity"
    				+ " can't be outside of time when event is taking place.");
    	
    	if (eventActivity.getEvent().getDateOfEvent().isAfter(eventActivity.getEndingTime()) || 
    			eventActivity.getEvent().getEndOfEvent().isBefore(eventActivity.getEndingTime())) 
    		throw new EventActivityValidationException("Ending time of event activity"
    				+ " can't be outside of time when event is taking place.");
    	
    	if (eventActivity.getStartingTime().isAfter(eventActivity.getEndingTime())) 
    		throw new EventActivityValidationException("Starting time can't be before"
    				+ " the ending time of an event activity.");
    	
    	if (!Pattern.matches("^.{2,50}$", eventActivity.getName()))
			throw new EventActivityValidationException("Location can't be less than 2 or over 50 characters long.");
    	
    	return true;
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
	            .filter(event -> !Boolean.TRUE.equals(event.isOver()))
	            .filter(event -> event.getOrganizer() == null || !blockedUsers.contains(event.getOrganizer()))
	            .sorted((e1, e2) -> Integer.compare(e2.getNumOfAttendees(), e1.getNumOfAttendees()))
	            .limit(5)
	            .toList();
	    
	    return filteredEvents;
    }
    
    
    
    
    
    public List<Event> getTop5OpenEventsUnauthorized() {
        
        List<Event> allEvents = eventRepository.findAll();
        
        List<Event> filteredEvents = allEvents.stream()
                .filter(event -> !Boolean.TRUE.equals(event.isOver()) &&
                		!Boolean.TRUE.equals(event.getIsPrivate())			&&
                		!Boolean.TRUE.equals(event.isOver())          &&
                		event.getDateOfEvent().isAfter(LocalDateTime.now()))
                .sorted((e1, e2) -> Integer.compare(e2.getNumOfAttendees(), e1.getNumOfAttendees()))
                .limit(5)
                .toList();
        return filteredEvents;
    }
    
    
    

    
    //Paginated like the ones above
    //Might filter the ones you MIGHT like more to be in the first rows
    public Page<Event> getRestEventsPaginated(int userId, int page, int size) {

        List<Event> events = getRestEvents(userId);
        int start = page*size;
        int end = Math.min((start+size), events.size());
        
        List<Event> paginatedEvents = events.subList(start, end);
    	
        return new PageImpl<>(paginatedEvents, PageRequest.of(page, size), events.size());
    }
    

    public Page<Event> getFilteredEvents(String name, String location, Integer numberOfAttendees, 
            String before, String after, List<Integer> eventTypes, 
            Integer id, int page, int size) throws ParseException {

		List<Event> events = getRestEvents( id);
		
		LocalDate beforeDate = (before == null || before.isEmpty()) ? null : LocalDate.parse(before);
		LocalDate afterDate = (after == null || after.isEmpty()) ? null : LocalDate.parse(after);
		
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
		
		if (numberOfAttendees != null && numberOfAttendees > 0) {
		events = events.stream()
		.filter(event -> event.getNumOfAttendees() >= numberOfAttendees)
		.toList();
		}
		
		if (beforeDate != null) {
		events = events.stream()
		.filter(event -> event.getDateOfEvent() != null &&
		    event.getDateOfEvent().toLocalDate().isBefore(beforeDate))
		.toList();
		}
		
		if (afterDate != null) {
		events = events.stream()
		.filter(event -> event.getDateOfEvent() != null &&
		    event.getDateOfEvent().toLocalDate().isAfter(afterDate))
		.toList();
		}
		
		if (eventTypes != null && !eventTypes.isEmpty()) {
		events = events.stream()
		.filter(event -> event.getEventType() != null && 
				eventTypes.contains(event.getEventType().getId()))
		.toList();
		}
		
		int start = page * size;
		int end = Math.min(start + size, events.size());
		
		List<Event> pageContent = (start >= events.size()) ? Collections.emptyList() : events.subList(start, end);
		
		return new PageImpl<>(pageContent, PageRequest.of(page, size), events.size());
	}

    
    public Page<Event> getFilteredEventsUnauthorized(String name, String location, Integer numberOfAttendees, 
	        String before, String after, List<Integer> eventTypes, 
	        int page, int size) throws ParseException {
    	List<Event> allEvents = eventRepository.findAll();
    	
		List<Event> events = getRestEventsUnauthorized(allEvents);
		
		LocalDate beforeDate = (before == null || before.isEmpty()) ? null : LocalDate.parse(before);
		LocalDate afterDate = (after == null || after.isEmpty()) ? null : LocalDate.parse(after);
		
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
		
		if (numberOfAttendees != null && numberOfAttendees > 0) {
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
			.filter(event -> event.getEventType() != null && eventTypes.contains(event.getEventType().getId()))
			.toList();
		}
		
		int start = page * size;
		int end = Math.min(start + size, events.size());
		
		List<Event> pageContent = (start >= events.size()) ? Collections.emptyList() : events.subList(start, end);
		
		return new PageImpl<>(pageContent, PageRequest.of(page, size), events.size());
	}
    
    public List<Event> getRestEvents(Integer id) {
        Optional<AuthentifiedUser> optionalUser = userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("");
        }
        
        AuthentifiedUser user = optionalUser.get();
        List<AuthentifiedUser> blockedUsers = user.getBlockedUsers();

        //String city = user.getCity();
        
        List<Event> allEvents = eventRepository.findAll();
    	List<Event> top5Events = getTop5OpenEvents(id);
        
        List<Event> events = allEvents.stream()
//              .filter(event -> city.equalsIgnoreCase(event.getPlace()))
              .filter(event -> !Boolean.TRUE.equals(event.isOver()))
              .filter(event -> event.getOrganizer() == null || !blockedUsers.contains(event.getOrganizer()))
              .filter(event -> !top5Events.contains(event))
              .sorted((e1, e2) -> Integer.compare(e2.getNumOfAttendees(), e1.getNumOfAttendees()))
              .toList();
        
    	return events;
    }
    
    public List<Event> getRestEventsUnauthorized(List<Event> allEvents) {
    	
    	List<Event> top5Events = getTop5OpenEventsUnauthorized();
    	
        List<Event> events = allEvents.stream()
              .filter(event -> !Boolean.TRUE.equals(event.isOver()))
              .filter(event -> !top5Events.contains(event))
              .sorted((e1, e2) -> Integer.compare(e2.getNumOfAttendees(), e1.getNumOfAttendees()))
              .toList(); 
    	
    	return events;
    }

}