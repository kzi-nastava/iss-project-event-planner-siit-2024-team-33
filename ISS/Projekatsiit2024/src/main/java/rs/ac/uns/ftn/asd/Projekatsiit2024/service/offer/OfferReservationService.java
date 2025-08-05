package rs.ac.uns.ftn.asd.Projekatsiit2024.service.offer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.offerReservation.ServiceBookingException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.product.Product;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferReservationRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.invitationService;

@Service
public class OfferReservationService {

    @Autowired
    private OfferRepository offerRepo;

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private OfferReservationRepository offerReservationRepo;
    		
    @Autowired
    private invitationService invitationService;

    private void validateArguments(
    		LocalDate dateOfReservation,
            Integer offerId,
            Integer eventId,
            LocalTime startTime,
            LocalTime endTime) {
    
        if (dateOfReservation == null) 
            throw new IllegalArgumentException("Invalid argument: Date of reservation cannot be null.");

        if (offerId == null) 
            throw new IllegalArgumentException("Invalid argument: Offer ID cannot be null.");

        if (eventId == null) 
            throw new IllegalArgumentException("Invalid argument: Event ID cannot be null.");

        if (startTime == null) 
            throw new IllegalArgumentException("Invalid argument: Start time cannot be null.");

        if (endTime == null) 
            throw new IllegalArgumentException("Invalid argument: End time cannot be null.");

        if (endTime.isBefore(startTime)) 
            throw new IllegalArgumentException("Invalid argument: End time cannot be earlier than start time.");
        
    	if (dateOfReservation.isBefore(LocalDate.now()))
    		throw new IllegalArgumentException("Invalid argument: Given date cannot be before current date.");
    }

    public OfferReservation createProductReservation(Product product, Event event) {
    	OfferReservation reservation = new OfferReservation();

    	reservation.setDateOfReservation(event.getDateOfEvent().toLocalDate());
    	reservation.setEvent(event);
    	reservation.setOffer(product);
    	reservation.setStartTime(event.getDateOfEvent());
    	reservation.setEndTime(event.getDateOfEvent());
    	
    	reservation = offerReservationRepo.save(reservation);
    	return reservation;
    }
    
    public void cancelProductReservation(Product product, Event event) {
    	OfferReservation reservation = offerReservationRepo.findByEventAndOffer(product.getId(), event.getId());
    	if(reservation != null) {
    		offerReservationRepo.delete(reservation);
    	}
    }
    
    public OfferReservation createOfferReservation(
            LocalDate dateOfReservation,
            Integer offerId,
            Integer eventId,
            LocalTime startTime,
            LocalTime endTime) {

        validateArguments(dateOfReservation, offerId, eventId, startTime, endTime);

        Offer offer = offerRepo.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("No offer with the given ID exists."));

        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("No event with the given ID exists."));

        OfferReservation offerReservation = new OfferReservation();
        offerReservation.setDateOfReservation(dateOfReservation);
        offerReservation.setOffer(offer);
        offerReservation.setEvent(event);
        offerReservation.setStartTime(LocalDateTime.of(dateOfReservation, startTime));
        offerReservation.setEndTime(LocalDateTime.of(dateOfReservation, endTime));
        
        Provider provider = offer.getProvider();
        AuthentifiedUser organizer = event.getOrganizer();
        String providerStr = "Your offer: " + offer.getName() + ", got reserved for " + dateOfReservation+ ", for an evet: " +event.getName()+ ". by Organizer: " + organizer.getEmail();
        String organizerStr = "You successfully reserved an offer: " +offer.getName() +", for an event: "+ event.getName() + "\n Offer you reserved is owned by provider: " +provider.getEmail()+".";
        invitationService.sendEmail(null, null, provider.getEmail(), "Your offer has been reserved", providerStr);
        invitationService.sendEmail(null, null, organizer.getEmail(), "You have successfully reserved an offer.", organizerStr);
        offerReservationRepo.save(offerReservation);

        return offerReservation;
    }
    	//No use as of now
    public OfferReservation createAndFlushOfferReservation(
            LocalDate dateOfReservation,
            Integer offerId,
            Integer eventId,
            LocalTime startTime,
            LocalTime endTime) {

        OfferReservation offerReservation = createOfferReservation(dateOfReservation, offerId, eventId, startTime, endTime);
        offerReservationRepo.flush();

        return offerReservation;
    }
    
    
    public OfferReservation bookAService(Integer eventId, Integer offerId, LocalDateTime offerStartTime, LocalDateTime offerEndTime) { 
    	Event event = getEventByID(eventId);
    	Offer offer = getOfferByID(offerId);
    	
        List<OfferReservation> reservations = offerReservationRepo.findByOfferId(offer.getOfferID());

        LocalDateTime eventStartTime = event.getDateOfEvent();
        LocalDateTime eventEndTime = event.getEndOfEvent();

      
        if (offerStartTime.isBefore(eventStartTime) || offerEndTime.isAfter(eventEndTime) || offerEndTime.isBefore(offerStartTime)) {
            throw new ServiceBookingException("This offer's end or starting time exceeds the end or start time of this event.", "BAD_TIME_RANGE");
        }

      
        for (OfferReservation reserv : reservations) {
            if (reserv.getDateOfReservation().equals(event.getDateOfEvent().toLocalDate())) {
                if (isTimeColliding(offerStartTime, offerEndTime, reserv.getStartTime(), reserv.getEndTime())) {
                    throw new ServiceBookingException("This offer is already booked at that time.", "TIME_COLLISION_WITH_ANOTHER_EVENT");
                }
            }
        }

        for (OfferReservation eventReserv : event.getReservations()) {
            if (isTimeColliding(offerStartTime, offerEndTime, eventReserv.getStartTime(), eventReserv.getEndTime())) {
                throw new ServiceBookingException("This offer's times collide with already existant offers of this event.","TIME_COLLISION_WITH_ANOTHER_OFFER_AT_SAME_EVENT");
            }
        }
        
        if (offer instanceof rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.service.Service service) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime reservationDeadline = event.getDateOfEvent().minusHours(service.getReservationInHours());

            if (now.isAfter(reservationDeadline)) {
                throw new ServiceBookingException(
                    "You can no longer book this service. Reservations must be made at least "
                    + service.getReservationInHours() + " hours before the event starts.",
                    "TOO_LATE_FOR_RESERVATION"
                );
            }
        }


        return createOfferReservation(event.getDateOfEvent().toLocalDate(),offer.getId(),event.getId(),offerStartTime.toLocalTime(),offerEndTime.toLocalTime());
    }

    private boolean isTimeColliding(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2); 
    }

    public void cancelService(OfferReservation reservation) {
        rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.service.Service service = (rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.service.Service) reservation.getOffer();
//        long timeUntilCancellation = reservation.getDateOfReservation().minus(LocalDate.now());
        long timeUntilCancellation = ChronoUnit.MILLIS.between(LocalDate.now().atStartOfDay(), reservation.getDateOfReservation().atStartOfDay());
        long cancellationDeadline = service.getCancellationInHours() * 60 * 60 * 1000;

        if (timeUntilCancellation < cancellationDeadline) {
            throw new IllegalArgumentException("Cannot cancel the service less than " + service.getCancellationInHours() + " hours before the reservation.");
        }

        offerReservationRepo.delete(reservation);
    }
    
    
    
	public OfferReservation findReservationByIdAndService(Integer serviceId, int reservationId) {
	    return offerReservationRepo.findById(reservationId)
	            .filter(r -> r.getOffer().getId().equals(serviceId))
	            .orElse(null);
	}
	
	public OfferReservation updateReservation(
	        Integer reservationId,
	        Integer serviceId,
	        LocalDate reservationDate,
	        LocalTime startTime,
	        LocalTime endTime) {

	    OfferReservation reservation = offerReservationRepo.findById(reservationId)
	            .orElseThrow(() -> new IllegalArgumentException("Reservation not found."));

	    if (!reservation.getOffer().getId().equals(serviceId)) {
	        throw new IllegalArgumentException("Service mismatch.");
	    }

	    validateArguments(reservationDate, serviceId, reservation.getEvent().getId(), startTime, endTime);

	    reservation.setDateOfReservation(reservationDate);
	    reservation.setStartTime(LocalDateTime.of(reservationDate, startTime));
	    if (endTime.isAfter(startTime)){
	    	  reservation.setEndTime(LocalDateTime.of(reservationDate,endTime));
    	}else{
    	  reservation.setEndTime(LocalDateTime.of(reservationDate.plusDays(1),endTime));
    	}

	    return offerReservationRepo.save(reservation);
	}
	
	public List<OfferReservation> getReservationsForServiceByOrganizer(Integer serviceID, String organizerEmail) {
		System.out.println("Getting reservations for serviceID = " + serviceID + ", organizerEmail = " + organizerEmail);

	    return offerReservationRepo.findAllByOffer_IdAndEvent_Organizer_Email(serviceID, organizerEmail);
	}


	private Event getEventByID(Integer id) {
		Optional<Event> optEvent = eventRepo.findById(id);
		if(optEvent.isEmpty()) {
			throw new IllegalArgumentException("No event for that id");
		}
		Event event = optEvent.get();
		return event;
	}
	private Offer getOfferByID(Integer id) {
		Optional<Offer> optOffer=offerRepo.findById(id);
		if(optOffer.isEmpty()) {
			throw new IllegalArgumentException("No offer for that id");
		}
		Offer off = optOffer.get();
		return off;
	}
    
    
}
