package rs.ac.uns.ftn.asd.Projekatsiit2024.service.favorites;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.MinimalEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;

@Service
public class FavoritesService {
	
	@Autowired
	AuthentifiedUserRepository userRepository;
	
	@Autowired
	EventRepository eventRepository;

	
	
	public Page<MinimalEventDTO> getFavoriteEvents(UserPrincipal userPrincipal, Pageable pageable) {
	    Integer userId = userPrincipal.getUser().getId();
	    return userRepository.findFavoriteEventsByUserId(userId, pageable)
	    		.map(MinimalEventDTO::new);
	}
	
	
	
	
	public boolean isEventFavorited(UserPrincipal userPrincipal, Integer eventId) {
		if (!eventRepository.existsById(eventId)) {
		    throw new EventValidationException("No such event exists.");
		}
		
	    Integer userId = userPrincipal.getUser().getId();
	    return userRepository.existsEventInFavorites(userId, eventId);
	}
	
	
	

	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Event addFavoriteEvent(Integer eventId, UserPrincipal userPrincipal) 
			throws EventValidationException {
		Optional<AuthentifiedUser> optionalUser = 
				userRepository.findById(userPrincipal.getUser().getId());
		AuthentifiedUser user = optionalUser.get();
		
		Optional<Event> optionalEvent = eventRepository.findById(eventId);
		
		if (optionalEvent.isEmpty())
			throw new EventValidationException("No such event exists.");
		
		Event event = optionalEvent.get();
		
		canEventBeInFavorites(user, event);
		
		user.getFavoriteEvents().add(event);
		userRepository.save(user);
		
		return event;
	}
	
	private boolean canEventBeInFavorites(AuthentifiedUser user, Event event) throws EventValidationException {
		boolean isAlreadyFavorite = user.getFavoriteEvents()
		        .stream()
		        .anyMatch(favoriteEvent -> favoriteEvent.getId().equals(event.getId()));
		if (isAlreadyFavorite)
			throw new EventValidationException("Event is already in list of your favorite events.");
		
		
		if (event.getIsPrivate()) {
    		//if event is private and user is not one of the attendees of this event
    		boolean isAttendee = event.getListOfAttendees()
    		        .stream()
    		        .anyMatch(attend -> attend.getId().equals(user.getId()));
    		if (!isAttendee)
    			throw new EventValidationException("You are not on the list of attendees for this private event.");
    	}
		
		return true;
	}

	
	
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Event removeFavoriteEvent(Integer eventId, UserPrincipal userPrincipal) 
			throws EventValidationException {
		Optional<AuthentifiedUser> optionalUser = 
				userRepository.findById(userPrincipal.getUser().getId());
		AuthentifiedUser user = optionalUser.get();
		
		Optional<Event> optionalEvent = eventRepository.findById(eventId);
		
		if (optionalEvent.isEmpty())
			throw new EventValidationException("No such event exists.");
		
		Event event = optionalEvent.get();
		
		canEventBeRemovedFromFavorites(user, event);
		
		user.getFavoriteEvents().removeIf(e -> e.getId().equals(eventId));
		userRepository.save(user);
		
		return event;
	}
	
	private boolean canEventBeRemovedFromFavorites(AuthentifiedUser user, Event event) throws EventValidationException {
		boolean isInFavorite = user.getFavoriteEvents()
		        .stream()
		        .anyMatch(favoriteEvent -> favoriteEvent.getId().equals(event.getId()));
		if (!isInFavorite)
			throw new EventValidationException("Event is not in the list of your favorite events.");
		
		
		if (event.getIsPrivate()) {
    		//if event is private and user is not one of the attendees of this event
    		boolean isAttendee = event.getListOfAttendees()
    		        .stream()
    		        .anyMatch(attend -> attend.getId().equals(user.getId()));
    		if (!isAttendee)
    			throw new EventValidationException("You are not on the list of attendees for this private event.");
    	}
		
		return true;
	}
}
