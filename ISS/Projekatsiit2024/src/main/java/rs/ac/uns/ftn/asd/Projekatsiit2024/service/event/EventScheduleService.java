package rs.ac.uns.ftn.asd.Projekatsiit2024.service.event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.MinimalEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.AuthentifiedUserValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.offer.OfferReservationRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;

@Service
public class EventScheduleService {
	
	@Autowired
	AuthentifiedUserRepository userRepository;
	@Autowired
	EventRepository eventRepository;
	@Autowired
	OfferReservationRepository offerReservationRepository;

	public List<MinimalEventDTO> getUserEventsByDate(UserPrincipal userPrincipal, LocalDate date) {
		Optional<AuthentifiedUser> optionalUser = userRepository.findById(userPrincipal.getUser().getId());
		
		if (optionalUser.isEmpty())
			throw new AuthentifiedUserValidationException("No such user can access events.");
		
		AuthentifiedUser user = optionalUser.get();
		
		if (user.getRole().getName().equals("ADMIN_ROLE") || 
			user.getRole().getName().equals("AUSER_ROLE"))
			return this.getAUserOrAdminEventsByDate(user, date);
		else if (user.getRole().getName().equals("ORGANIZER_ROLE"))
			return this.getOrganizerEventsByDate(user, date);
		else if (user.getRole().getName().equals("PROVIDER_ROLE"))
			return this.getProviderEventsByDate(user, date);
		
		throw new AuthentifiedUserValidationException("No such user can access events.");
	}
	
	
	private List<MinimalEventDTO> getAUserOrAdminEventsByDate(AuthentifiedUser user, LocalDate date) {
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
		
		List<Event> joinedEvents = eventRepository.findByParticipantAndDate(user.getId(), startOfDay, endOfDay);

	    Set<Event> combined = new HashSet<>();
	    combined.addAll(joinedEvents);

	    return combined.stream()
	                   .map(MinimalEventDTO::new)
	                   .collect(Collectors.toList());
	}
	
	private List<MinimalEventDTO> getOrganizerEventsByDate(AuthentifiedUser user, LocalDate date) {
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
		
		List<Event> createdEvents = eventRepository.findByCreatorAndDate(user.getId(), startOfDay, endOfDay);
	    List<Event> joinedEvents = eventRepository.findByParticipantAndDate(user.getId(), startOfDay, endOfDay);

	    Set<Event> combined = new HashSet<>();
	    combined.addAll(createdEvents);
	    combined.addAll(joinedEvents);

	    return combined.stream()
	                   .map(MinimalEventDTO::new)
	                   .collect(Collectors.toList());
	}
	
	
	private List<MinimalEventDTO> getProviderEventsByDate(AuthentifiedUser user, LocalDate date) {
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
		
		List<Event> eventsWithReservations = 
				offerReservationRepository.findEventsWithProviderServiceReservationsOnDate(user.getId(), startOfDay, endOfDay);
	    List<Event> joinedEvents = eventRepository.findByParticipantAndDate(user.getId(), startOfDay, endOfDay);

	    Set<Event> combined = new HashSet<>();
	    combined.addAll(eventsWithReservations);
	    combined.addAll(joinedEvents);

	    return combined.stream()
	                   .map(MinimalEventDTO::new)
	                   .collect(Collectors.toList());
	}
}
