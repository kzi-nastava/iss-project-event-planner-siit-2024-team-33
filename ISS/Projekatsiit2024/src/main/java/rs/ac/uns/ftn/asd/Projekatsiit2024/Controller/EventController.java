package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.EventService;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/top5")
    public List<Event> getTop5OpenEvents(@RequestParam String city) {
        return eventService.getTop5OpenEvents(city);
    }

    @GetMapping("/all")
    public List<Event> getAllOpenEvents(
            @RequestParam String city,
            @RequestParam(required = false) String searchQuery,
            @RequestParam(required = false) String sortBy) {
        return eventService.getAllOpenEvents(city, searchQuery, sortBy);
    }
}
