package rs.ac.uns.ftn.asd.Projekatsiit2024.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.OrganizerRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.ProviderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
	@Autowired
    private EventRepository eventRepository;
	@Autowired
	private AuthentifiedUserRepository userRepo;
    

    public List<Event> getTop5OpenEvents(String city, Integer id) {
        Optional<AuthentifiedUser> optionalUser = userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("");
        }
        
        AuthentifiedUser user = optionalUser.get();
        List<AuthentifiedUser> blockedUsers = user.getBlockedUsers();

        List<Event> allEvents = eventRepository.findAll();

        List<Event> filteredEvents = allEvents.stream()
                .filter(event -> city.equalsIgnoreCase(event.getPlace()))
                .filter(event -> event.getOrganizer() == null || !blockedUsers.contains(event.getOrganizer()))
                .sorted((e1, e2) -> Integer.compare(e2.getNumOfAttendees(), e1.getNumOfAttendees()))
                .limit(5)
                .toList();
        return filteredEvents;
    }
    
    public List<Event> getRestEvents(String city, Integer id) {
        Optional<AuthentifiedUser> optionalUser = userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("");
        }
        
        AuthentifiedUser user = optionalUser.get();
        List<AuthentifiedUser> blockedUsers = user.getBlockedUsers();

        List<Event> allEvents = eventRepository.findAll();

        List<Event> top5Events = allEvents.stream()
                .filter(event -> city.equalsIgnoreCase(event.getPlace()))
                .filter(event -> event.getOrganizer() == null || !blockedUsers.contains(event.getOrganizer()))
                .sorted((e1, e2) -> Integer.compare(e2.getNumOfAttendees(), e1.getNumOfAttendees()))
                .limit(5)
                .toList();

        List<Event> restEvents = allEvents.stream()
                .filter(event -> city.equalsIgnoreCase(event.getPlace()))
                .filter(event -> event.getOrganizer() == null || !blockedUsers.contains(event.getOrganizer()))
                .filter(event -> !top5Events.contains(event))
                .sorted((e1, e2) -> Integer.compare(e2.getNumOfAttendees(), e1.getNumOfAttendees()))
                .toList();

        return restEvents;
    }



    public List<Event> getAllOpenEvents(String city, String searchQuery, String sortBy) {
//        return eventRepository.findAllByCityAndIsPrivateFalse(city, searchQuery, sortBy);
    	return null;
    }
}
