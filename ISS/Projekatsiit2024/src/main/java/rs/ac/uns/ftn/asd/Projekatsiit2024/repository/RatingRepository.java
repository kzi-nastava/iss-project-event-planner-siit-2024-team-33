package rs.ac.uns.ftn.asd.Projekatsiit2024.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Rating;

public interface RatingRepository extends JpaRepository<Rating, Integer>{
    List<Rating> findByOfferId(int offerId);
    List<Rating> findByOfferIdAndAcceptedTrue(int offerId);

}