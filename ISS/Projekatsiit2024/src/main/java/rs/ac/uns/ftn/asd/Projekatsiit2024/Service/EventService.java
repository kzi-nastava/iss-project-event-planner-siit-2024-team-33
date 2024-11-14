package rs.ac.uns.ftn.asd.Projekatsiit2024.Service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.EventRepository;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getTop5OpenEvents(String city) {
        return eventRepository.findTop5ByCityAndIsPrivateFalse(city);
    }

    public List<Event> getAllOpenEvents(String city, String searchQuery, String sortBy) {
        return eventRepository.findAllByCityAndIsPrivateFalse(city, searchQuery, sortBy);
    }
}
