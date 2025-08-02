package rs.ac.uns.ftn.asd.Projekatsiit2024.service.event;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventActivity;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Invitation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.InvitationStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.InvitationRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventTypeRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.invitationService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.CreateEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.MinimalEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventActivity.CreateEventActivityDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.invitation.PostInvitationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventActivityValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventUserValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventValidationException;

@Service
public class EventService {
	private static final int ALL_EVENT_TYPE_ID = 1;

	@Autowired
    private EventRepository eventRepository;
	@Autowired
	private AuthentifiedUserRepository userRepo;
    @Autowired
    private EventTypeRepository eventTypeRepository;
    @Autowired
    private invitationService invitationService;
    @Autowired
    private InvitationRepository invitationRepo;
    
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
        
        if(event.getIsPrivate()) {
			Set<Invitation> invitations = new HashSet<>();
		    PostInvitationDTO invData = new PostInvitationDTO();
		    
		    invData.setEventId(event.getId());
		    invData.setEmailAddresses(eventDTO.getPrivateInvitations().stream().toList());
		    invData.setMessage(event.getDescription());
		    
		    invitationService.createInvitations(invData, organizer, new Date() );
		    
			event.setPrivateInvitations(invitations);
		}
        
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
    	
    	if (!Pattern.matches("^.{5,250}$", event.getDescription()))
			throw new EventValidationException("Description can't be less than 5 or over 250 characters long.");
    	
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
    
    
    
    
	    @Transactional(propagation = Propagation.REQUIRED)
	    public Event joinEvent(Integer eventId, AuthentifiedUser user) 
	    		throws EventValidationException {
	    	Optional<Event> optionalEvent = eventRepository.findById(eventId);
	    	
	    	if (optionalEvent.isEmpty())
	    		throw new EventValidationException("No event exists with such id.");
	    	
	    	Event event = optionalEvent.get();
	    	isJoiningEventPossible(user, event);
	    	
	    	//So if user ignores the invitation on mainpage, he can still join an event, if so, the status of invitation must go to ACCEPTED
	    	//If he ever denied it, he won't be able to see the event.
	    	if (Boolean.TRUE.equals(event.getIsPrivate())) {
	            Optional<Invitation> invitationOpt = invitationRepo
	                .findByEventAndInvitedUser(event, user.getEmail());

	            if (invitationOpt.isPresent()) {
	                Invitation invitation = invitationOpt.get();
	                if (invitation.getStatus() == InvitationStatus.PENDING) {
	                    invitation.setStatus(InvitationStatus.ACCEPTED);
	                    invitation.setInvitedUser(user.getEmail());
	                    invitationRepo.save(invitation);
	                } else if (invitation.getStatus() == InvitationStatus.DENIED) {
	                    throw new EventValidationException("You have rejected this invitation.");
	                }
	            } else {
	                throw new EventValidationException("You cannot join a private event without an invitation.");
	            }
	        }
	    	
	    	event.getListOfAttendees().add(user);
	    	
	    	return eventRepository.save(event);
		}
    
    private boolean isJoiningEventPossible(AuthentifiedUser user, Event event) 
    		throws EventValidationException {
    	if (event.getNumOfAttendees() == event.getListOfAttendees().size())
    		throw new EventValidationException("There is no more place left to join event.", 
    				"NO_PLACE");
    	
    	if (user.getId().equals(event.getOrganizer().getId()))
    		throw new EventValidationException("Organizer can't join it's own event.", 
    				"SELF_JOIN");
    	
    	if (event.getListOfAttendees().contains(user))
    		throw new EventValidationException("You have already joined this event.", 
    				"ALREADY_JOINED");
    	
    	if (event.getDateOfEvent().isBefore(LocalDateTime.now()))
    		throw new EventValidationException("Can't join event after it already started.", 
    				"EVENT_STARTED");
    	
    	//private event
    	if (event.getIsPrivate()) {
    		boolean isInvited = event.getPrivateInvitations()
    		        .stream()
    		        .anyMatch(inv -> inv.getInvitedUser().equals(user.getEmail()));	
    		if (!isInvited)
    			throw new EventValidationException("You are not on the list of invitations for this event.", 
    					"EVENT_PRIVATE");
    	}
    	
    	return true;
    }
    
    
    
    
    
    @Transactional(propagation = Propagation.REQUIRED)
	public Event getEventDetails(Integer eventId, UserPrincipal userPrincipal) throws EventValidationException {
		Optional<Event> optionalEvent = eventRepository.findById(eventId);
    	
    	if (optionalEvent.isEmpty())
    		throw new EventValidationException("No event exists with such id.");
    	
    	Event event = optionalEvent.get();
		
		isUserAllowedToGetData(userPrincipal, event);
		
		return event;
	}
    
	private boolean isUserAllowedToGetData(UserPrincipal userPrincipal, Event event) throws EventValidationException {
    	if (event.getIsPrivate()) {
    		//if event is private and user is logged out
    		if (userPrincipal == null)
    			throw new EventValidationException("You have to be logged in into account which has"
    					+ " permition to view this private event.");
    		
    		AuthentifiedUser user = userPrincipal.getUser();
    		
    		//if event is private and user is not one of the attendees of this event
    		boolean isAttendee = event.getListOfAttendees()
    		        .stream()
    		        .anyMatch(attend -> attend.getId().equals(user.getId()));
    		//if event is private and user is not organizer of this event
    		boolean isOrganizer = event.getOrganizer().getId().equals(user.getId());
    		//if event is private and user is not admin
    		boolean isAdmin = user.getRole().getName().equals("ADMIN_ROLE");
    		
    		if (!isAttendee && !isOrganizer && !isAdmin)
    			throw new EventValidationException("You are not on the list of attendees for this private event nor you are "
    					+ "organizer of this event.");
    	}
    	
    	return true;
	}
    
    
    
    
	public Page<MinimalEventDTO> readEvents(Pageable pageable, UserPrincipal userPrincipal) 
		throws EventUserValidationException {
		AuthentifiedUser user = userPrincipal.getUser();
		
		//if user is admin give him all events
		if (user.getRole().getName().equals("ADMIN_ROLE"))
			return eventRepository.findAll(pageable).map(MinimalEventDTO::new);
		
		//if user is organizer give him just his events
		if (user.getRole().getName().equals("ORGANIZER_ROLE")) {
			Organizer organizer = (Organizer) user;
	        return eventRepository.findByOrganizer(organizer, pageable).map(MinimalEventDTO::new);
		}
		
		//if anything else throw error
		throw new EventUserValidationException("You can't see events if you are not admin or organizer.");
			
	}
	
	public List<Event> getTop5OpenEvents(Integer userId) {
	    List<Event> allEvents = eventRepository.findAll();

	    if (userId == null) {
	        return allEvents.stream()
	            .filter(event -> !Boolean.TRUE.equals(event.isOver()))
	            .filter(event -> !Boolean.TRUE.equals(event.getIsPrivate()))
	            .filter(event -> event.getDateOfEvent().isAfter(LocalDateTime.now()))
	            .sorted(Comparator.comparingInt(Event::getNumOfAttendees).reversed())
	            .limit(5)
	            .toList();
	    }

	    Optional<AuthentifiedUser> optionalUser = userRepo.findById(userId);
	    if (optionalUser.isEmpty()) {
	        throw new IllegalArgumentException("User not found");
	    }

	    AuthentifiedUser user = optionalUser.get();
	    List<Invitation> invitations = invitationRepo.findAll();
	    List<AuthentifiedUser> blockedUsers = user.getBlockedUsers();

	    String residency = null;
	    if (user instanceof Organizer organizer) {
	        residency = organizer.getResidency();
	    } else if (user instanceof Provider provider) {
	        residency = provider.getResidency();
	    }

	    final String finalResidency = residency;

	    return allEvents.stream()
	        .filter(event -> !Boolean.TRUE.equals(event.isOver()))
	        .filter(event -> finalResidency == null || finalResidency.equalsIgnoreCase(event.getPlace()))
	        .filter(event -> event.getOrganizer() == null || !blockedUsers.contains(event.getOrganizer()))
	        .filter(event -> isEventVisibleForUser(user, event))
	        .filter(event -> canUserSeeEvent(user, event, invitations))
	        .sorted(Comparator.comparingInt(Event::getNumOfAttendees).reversed())
	        .limit(5)
	        .toList();
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
    @Transactional
    public Page<Event> getFilteredEvents(
            String name,
            String location,
            Integer numberOfAttendees,
            String before,
            String after,
            List<Integer> eventTypes,
            Integer id, 
            int page,
            int size
    ) throws ParseException {

        Stream<Event> events = eventRepository.findAll().stream();

        LocalDate beforeDate = (before == null || before.isEmpty()) ? null : LocalDate.parse(before);
        LocalDate afterDate = (after == null || after.isEmpty()) ? null : LocalDate.parse(after);

        if (name != null && !name.isEmpty()) {
            events = events.filter(event -> event.getName() != null &&
                    event.getName().toLowerCase().contains(name.toLowerCase()));
        }

        if (location != null && !location.isEmpty()) {
            events = events.filter(event -> event.getPlace() != null &&
                    event.getPlace().toLowerCase().contains(location.toLowerCase()));
        }

        if (numberOfAttendees != null && numberOfAttendees > 0) {
            events = events.filter(event ->
                    event.getNumOfAttendees() != null &&
                            event.getNumOfAttendees() >= numberOfAttendees);
        }

        if (beforeDate != null) {
            events = events.filter(event ->
                    event.getDateOfEvent() != null &&
                            event.getDateOfEvent().toLocalDate().isBefore(beforeDate));
        }

        if (afterDate != null) {
            events = events.filter(event ->
                    event.getDateOfEvent() != null &&
                            event.getDateOfEvent().toLocalDate().isAfter(afterDate));
        }

        if (eventTypes != null && !eventTypes.isEmpty()) {
            if (eventTypes.contains(ALL_EVENT_TYPE_ID)) {
                events = events.filter(event -> event.getEventType() != null);
            } else {
                events = events.filter(event ->
                        event.getEventType() != null &&
                                eventTypes.contains(event.getEventType().getId()));
            }
        }

        if (id != null) {
            Optional<AuthentifiedUser> optionalUser = userRepo.findById(id);
            if (optionalUser.isPresent()) {
                AuthentifiedUser user = optionalUser.get();
                List<AuthentifiedUser> blockedUsers = user.getBlockedUsers();
                events = events.filter(event ->
                        event.getOrganizer() == null || !blockedUsers.contains(event.getOrganizer()));
            }
        }
        
        if (id != null) {
            Optional<AuthentifiedUser> optionalUser = userRepo.findById(id);
            if (optionalUser.isPresent()) {
                AuthentifiedUser user = optionalUser.get();
                List<Invitation> invitations = invitationRepo.findAll();

                events = events
                    .filter(event -> isEventVisibleForUser(user, event))
                    .filter(event -> canUserSeeEvent(user, event, invitations)); 
            }
        } else {
            events = events.filter(event -> !Boolean.TRUE.equals(event.getIsPrivate()));
        }

        List<Event> filteredEvents = events.toList();
        int start = page * size;
        int end = Math.min(start + size, filteredEvents.size());

        List<Event> pageContent = (start >= filteredEvents.size()) ? Collections.emptyList() :
                filteredEvents.subList(start, end);

        return new PageImpl<>(pageContent, PageRequest.of(page, size), filteredEvents.size());
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
        
    	List<Invitation> invitations = invitationRepo.findAll();
    	
        List<Event> events = allEvents.stream()
//              .filter(event -> city.equalsIgnoreCase(event.getPlace()))
              .filter(event -> !Boolean.TRUE.equals(event.isOver()))
	          .filter(event -> isEventVisibleForUser(user, event))
              .filter(event -> !top5Events.contains(event))
              .filter(event -> canUserSeeEvent(user, event, invitations))
              .sorted((e1, e2) -> Integer.compare(e2.getNumOfAttendees(), e1.getNumOfAttendees()))
              .toList();
        
    	return events;
    }
    
    
    public List<Event> geteventsByOrganizerID(Integer id){
    	List<Event> events = eventRepository.findByOrganizerId(id);
    	
    	return events;
    }
    
    public List<Event> geteventsByOrganizerIDUpdated(Integer id){
    	List<Event> events = eventRepository.findByOrganizerId(id);
    	List<Event> ret = new ArrayList<Event>();
    	
    	for(Event event:events) {
    		if(!event.getDateOfEvent().isBefore(LocalDateTime.now())) {
    			ret.add(event);
    		}
    	}
    	return ret;
    }
    
    private boolean isEventVisibleForUser(AuthentifiedUser user, Event event) {
        AuthentifiedUser organizer = event.getOrganizer();

        if (organizer == null) {
            return true; // no organizer, always visible
        }

        String role = user.getRole().getName();

        switch (role) {
            case "AUSER_ROLE":
                // User blocks organizer, can't see his stuff
                if (user.getBlockedUsers().contains(organizer)) return false;
                // Organizer blocked user, can't see.
                if (organizer.getBlockedUsers().contains(user)) return false;
                return true;

            case "ORGANIZER_ROLE":
            	//Organizer can see other organizer's events, despite being blocked/blocking them.
            	//When he blocks someone they can't see shit
                return true;

            case "PROVIDER_ROLE":
                // Provider blocked this organizer? Can't see
                if (organizer.getBlockedUsers().contains(user)) return false;
                return true;

            case "ADMIN_ROLE":
                return true; // Admin sees everything

            default:
                return true;
        }
    }
    
    private boolean canUserSeeEvent(AuthentifiedUser user, Event event, List<Invitation> invitations) {
        if (!Boolean.TRUE.equals(event.getIsPrivate())) {
            return true;
        }

        return invitations.stream()
                .anyMatch(inv -> inv.getEvent().equals(event) &&
                                 user.getEmail().equalsIgnoreCase(inv.getInvitedUser()) &&
                                 inv.getStatus() != InvitationStatus.DENIED);
    }

    
}