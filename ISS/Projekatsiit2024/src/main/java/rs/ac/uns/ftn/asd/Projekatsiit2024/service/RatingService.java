package rs.ac.uns.ftn.asd.Projekatsiit2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.EventRating;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Notification;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Rating;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.EventRatingRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.RatingRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {
	

	
    @Autowired
    private AuthentifiedUserRepository userRepository;
    
    @Autowired
    private OfferRepository offerRepo;
    
    @Autowired
    private RatingRepository ratingRepository;
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private EventRatingRepository eventRatingRepository;
    
    @Autowired
    private NotificationService notificationervice;
    
    public Rating submitComment(int ratingValue, String comment, int userId, int offerId) {
        Rating rating = new Rating();
        rating.setRatingValue(ratingValue);
        rating.setComment(comment);
        rating.setAccepted(false);
        rating.setIsDeleted(false);
        
        AuthentifiedUser author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        Offer offer = offerRepo.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found."));

        rating.setAuthor(author);
        rating.setOffer(offer);

        return ratingRepository.save(rating);
    }
    
    

    public void approveComment(int commentId) {
        Optional<Rating> ratingOpt = ratingRepository.findById(commentId);
        if (ratingOpt.isPresent()) {
            Rating rating = ratingOpt.get();
            rating.setAccepted(true);
            
            Offer offer = rating.getOffer();
            Provider provider = offer.getProvider();
            
            notificationervice.createNotification(provider.getId(), "Your offer  " + offer.getName() + " got rated with a: " +rating.getRatingValue()+ "\n"
        		+ rating.getComment());
            
            ratingRepository.save(rating);
        } else {
            throw new IllegalArgumentException("");
        }
    }
    
    //Made it so that the rating is deleted
    public void deleteComment(int commentId) {
        Optional<Rating> ratingOpt = ratingRepository.findById(commentId);
        if (ratingOpt.isPresent()) {
            Rating rating = ratingOpt.get();
            rating.setIsDeleted(true);
            rating.setAccepted(false);
            ratingRepository.save(rating);
        } else {
            throw new IllegalArgumentException("");
        }
    }
    
    public List<Rating> getRatingsByProvider(int providerId){
    	List<Rating> ratingsForProvider = ratingRepository.findByProviderId(providerId);
    	
    	return ratingsForProvider;
    }
    
    public List<Rating> getRatingsByOfferId(int offerId) {
        return ratingRepository.findByOfferIdAndAcceptedTrue(offerId);
    }
    
    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    public Rating getRatingById(int ratingId) {
        return ratingRepository.findById(ratingId)
                .orElseThrow(() -> new IllegalArgumentException(""));
    }
    public Page<Rating> getAllRatings(Pageable pageable) {
        return ratingRepository.findAll(pageable);
    }
    
    //THIS HERE IS FOR EVENTS. UP THERE IS FOR OFFERS
    public EventRating submitEventRating(int ratingValue, String comment, int userId, int eventId) {
        EventRating eventRating = new EventRating();
        eventRating.setRatingValue(ratingValue);
        eventRating.setComment(comment);
        eventRating.setAccepted(false);
        eventRating.setIsDeleted(false);

        AuthentifiedUser author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found."));

        eventRating.setAuthor(author);
        eventRating.setEvent(event);

        return eventRatingRepository.save(eventRating);
    }

    
    public void approveRating(int ratingId) {
        EventRating rating = eventRatingRepository.findById(ratingId)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found"));
        rating.setAccepted(true);
        
        Event event = rating.getEvent();
        AuthentifiedUser org = event.getOrganizer();
        
        notificationervice.createNotification(org.getId(), "Your event  " + event.getName() + " got rated with a: " +rating.getRatingValue()+ "\n"
        		+ rating.getComment());
        
        eventRatingRepository.save(rating);
    }

    
    public void deleteRating(int ratingId) {
        EventRating rating = eventRatingRepository.findById(ratingId)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found"));
        rating.setIsDeleted(true);
        rating.setAccepted(false);
        eventRatingRepository.save(rating);
    }

    
    public List<EventRating> getRatingsByEventId(int eventId) {
        return eventRatingRepository.findByEventIdAndAcceptedTrue(eventId);
    }

    public Page<EventRating> getAllEventRatings(Pageable pageable) {
        return eventRatingRepository.findAll(pageable); 
    }

    public EventRating getRatingEventById(int ratingId) {
        return eventRatingRepository.findById(ratingId)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found"));
    }

}
