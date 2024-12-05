package rs.ac.uns.ftn.asd.Projekatsiit2024.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.ParseException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.BudgetItem;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.OrganizerRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.ProviderRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.MinimalEventTypeDTO;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
	@Autowired
    private EventRepository eventRepository;
	@Autowired
	private AuthentifiedUserRepository userRepo;
    @Autowired
    private OrganizerRepository organRepo;
    
    
    private void validateEventArguments(
            String name,
            String place,
            Date dateOfEvent,
            Date endOfEvent,
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

        if (dateOfEvent == null || dateOfEvent.before(new Date(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("");
        }

        if (endOfEvent != null && endOfEvent.before(dateOfEvent)) {
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
    
    public List<Event> getFilteredEvents(String name, String location, Integer numberOfAttendees, String before, String after, List<Integer> eventTypes, Integer id) throws java.text.ParseException {
        List<Event> allevents = eventRepository.findAll();

        List<Event> events = getRestEvents(allevents, id);
        
        													//Might want to change to MM-dd-yyyy
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Date beforeDate = (before != null && !before.isEmpty()) ? (Date) dateFormat.parse(before) : null;
        final Date afterDate = (after != null && !after.isEmpty()) ? (Date) dateFormat.parse(after) : null;

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
                .filter(event -> event.getDateOfEvent() != null && event.getDateOfEvent().before(beforeDate))
                .toList();
        }

        if (afterDate != null) {
            events = events.stream()
                .filter(event -> event.getDateOfEvent() != null && event.getDateOfEvent().after(afterDate))
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
        Date currentDate = new Date(System.currentTimeMillis());

        allEvents.stream()
                .filter(event -> event.getDateOfEvent() != null && event.getDateOfEvent().before(currentDate))
                .forEach(event -> {
                    event.setItsJoever(true);
                    eventRepository.save(event);
                });
    }


    public List<Event> getAllOpenEvents(String city, String searchQuery, String sortBy) {
//        return eventRepository.findAllByCityAndIsPrivateFalse(city, searchQuery, sortBy);
    	return null;
    }
    

    public Event createEvent(
            String name,
            String description,
            String place,
            Double latitude,
            Double longitude,
            Date dateOfEvent,
            Date endOfEvent,
            int numOfAttendees,
            Boolean isPrivate,
            Integer price,
            String picutre,
            Integer organizerId,
            List<EventType> eventTypes) {

        validateEventArguments(name, place, dateOfEvent, endOfEvent,price , organizerId, eventTypes);

        Organizer organizer = organRepo.findById(organizerId)
                .orElseThrow(() -> new RuntimeException(""));

        Event event = new Event();
        event.setName(name);
        event.setDescription(description);
        event.setPlace(place);
        event.setLatitude(latitude);
        event.setLongitude(longitude);
        event.setDateOfEvent(dateOfEvent);
        event.setEndOfEvent(endOfEvent);
        event.setNumOfAttendees(numOfAttendees);
        event.setIsPrivate(isPrivate);
        event.setItsJoever(false);
        event.setOrganizer(organizer);
        event.setPicture(picutre);
        event.setPrice(price);
        event.setEventTypes(eventTypes);
        event.setReservations(new ArrayList<>());
        event.setBudgetItems(new ArrayList<>());
        eventRepository.save(event);
        return event;
    }

    public Event createAndFlushEvent(
            String name,
            String description,
            String place,
            Double latitude,
            Double longitude,
            Date dateOfEvent,
            Date endOfEvent,
            int numOfAttendees,
            Boolean isPrivate,
            Integer price,
            String picture,
            Integer organizerId,
            List<EventType> eventTypes) {

        Event event = createEvent(name, description, place, latitude, longitude, dateOfEvent, endOfEvent,
                numOfAttendees, isPrivate, price ,picture, organizerId, eventTypes);
        eventRepository.flush();
        return event;
    }

    public Event createSuggestion(
            String name,
            String description,
            String place,
            Double latitude,
            Double longitude,
            Date dateOfEvent,
            Date endOfEvent,
            int numOfAttendees,
            Boolean isPrivate,
            Integer price,
            String picture,
            Integer organizerId,
            List<EventType> eventTypes) {

        validateEventArguments(name, place, dateOfEvent, endOfEvent,price, organizerId, eventTypes);

        Organizer organizer = organRepo.findById(organizerId)
                .orElseThrow(() -> new RuntimeException(""));

        Event event = new Event();
        event.setName(name);
        event.setDescription(description);
        event.setPlace(place);
        event.setLatitude(latitude);
        event.setLongitude(longitude);
        event.setDateOfEvent(dateOfEvent);
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
            Date dateOfEvent,
            Date endOfEvent,
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
        if (dateOfEvent != null) event.setDateOfEvent(dateOfEvent);
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
