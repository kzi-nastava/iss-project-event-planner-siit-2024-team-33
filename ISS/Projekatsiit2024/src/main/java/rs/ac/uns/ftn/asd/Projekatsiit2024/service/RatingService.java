package rs.ac.uns.ftn.asd.Projekatsiit2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Rating;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.RatingRepository;

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
}
