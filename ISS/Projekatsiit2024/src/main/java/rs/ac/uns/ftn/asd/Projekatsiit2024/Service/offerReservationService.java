package rs.ac.uns.ftn.asd.Projekatsiit2024.Service;

import java.util.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Product;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.OfferRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.OfferReservationRepository;

@Service
public class offerReservationService {

    @Autowired
    private OfferRepository offerRepo;

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private OfferReservationRepository offerReservationRepo;

    private void validateArguments(
            Date dateOfReservation,
            Integer offerId,
            Integer eventId,
            Time startTime,
            Time endTime) {

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

        if (endTime.before(startTime)) 
            throw new IllegalArgumentException("Invalid argument: End time cannot be earlier than start time.");
    }

    public OfferReservation createProductReservation(Product product, Event event) {
    	OfferReservation reservation = new OfferReservation();
    	//TODO
    	reservation.setDateOfReservation(event.getDateOfEvent());
    	reservation.setEvent(event);
    	reservation.setOffer(product);
    	
    	reservation = offerReservationRepo.save(reservation);
    	return reservation;
    }
    
    public OfferReservation createOfferReservation(
            Date dateOfReservation,
            Integer offerId,
            Integer eventId,
            Time startTime,
            Time endTime) {

        validateArguments(dateOfReservation, offerId, eventId, startTime, endTime);

        Offer offer = offerRepo.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid argument: No offer with the given ID exists."));

        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid argument: No event with the given ID exists."));

        OfferReservation offerReservation = new OfferReservation();
        offerReservation.setDateOfReservation(dateOfReservation);
        offerReservation.setOffer(offer);
        offerReservation.setEvent(event);
        offerReservation.setStartTime(startTime);
        offerReservation.setEndTime(endTime);

        offerReservationRepo.save(offerReservation);

        return offerReservation;
    }

    public OfferReservation createAndFlushOfferReservation(
            Date dateOfReservation,
            Integer offerId,
            Integer eventId,
            Time startTime,
            Time endTime) {

        OfferReservation offerReservation = createOfferReservation(dateOfReservation, offerId, eventId, startTime, endTime);
        offerReservationRepo.flush();

        return offerReservation;
    }
    
    
    public OfferReservation bookAService(Integer eventId, Integer offerId, Time offerStartTime, Time offerEndTime) { 
    	Event event = getEventByID(eventId);
    	Offer offer = getOfferByID(offerId);
    	
        List<OfferReservation> reservations = offerReservationRepo.findByOfferId(offer.getId());

        Time eventStartTime = new Time(event.getDateOfEvent().getTime());
        Time eventEndTime = new Time(event.getEndOfEvent().getTime());

      
        if (offerStartTime.before(eventStartTime) || offerEndTime.after(eventEndTime) || offerEndTime.before(offerStartTime)) {
            throw new IllegalArgumentException("This offer's end or starting time exceeds the end or start time of this event.");
        }

      
        for (OfferReservation reserv : reservations) {
            if (reserv.getDateOfReservation().equals(event.getDateOfEvent())) {
                if (isTimeColliding(offerStartTime, offerEndTime, reserv.getStartTime(), reserv.getEndTime())) {
                    throw new IllegalArgumentException("This offer is already booked at that time.");
                }
            }
        }

        
        for (OfferReservation eventReserv : event.getReservations()) {
            if (isTimeColliding(offerStartTime, offerEndTime, eventReserv.getStartTime(), eventReserv.getEndTime())) {
                throw new IllegalArgumentException("This offer's times collide with already existant offers of this event.");
            }
        }

        return createOfferReservation(event.getDateOfEvent(),offer.getOfferID(),event.getId(),offerStartTime,offerEndTime);
    }

    private boolean isTimeColliding(Time start1, Time end1, Time start2, Time end2) {
        return start1.before(end2) && end1.after(start2); 
    }

    public void cancelService(OfferReservation reservation) {
        rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Service service = (rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Service) reservation.getOffer();
        long timeUntilCancellation = reservation.getDateOfReservation().getTime() - System.currentTimeMillis();
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
	        Date reservationDate,
	        Time startTime,
	        Time endTime) {

	    OfferReservation reservation = offerReservationRepo.findById(reservationId)
	            .orElseThrow(() -> new IllegalArgumentException("Reservation not found."));

	    if (!reservation.getOffer().getId().equals(serviceId)) {
	        throw new IllegalArgumentException("Service mismatch.");
	    }

	    validateArguments(reservationDate, serviceId, reservation.getEvent().getId(), startTime, endTime);

	    reservation.setDateOfReservation(reservationDate);
	    reservation.setStartTime(startTime);
	    reservation.setEndTime(endTime);

	    return offerReservationRepo.save(reservation);
	}

	private Event getEventByID(Integer id) {
		Optional<Event> optEvent = eventRepo.findById(id);
		if(optEvent.isEmpty()) {
			throw new IllegalArgumentException("");
		}
		Event event = optEvent.get();
		return event;
	}
	private Offer getOfferByID(Integer id) {
		Optional<Offer> optOffer=offerRepo.findById(id);
		if(optOffer.isEmpty()) {
			throw new IllegalArgumentException("");
		}
		Offer off = optOffer.get();
		return off;
	}
    
    
}
