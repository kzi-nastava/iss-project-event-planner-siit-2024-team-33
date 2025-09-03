package rs.ac.uns.ftn.asd.Projekatsiit2024.repository.communication;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.communication.Rating;

public interface RatingRepository extends JpaRepository<Rating, Integer>{
    List<Rating> findByOfferId(int offerId);
    List<Rating> findByOfferIdAndAcceptedTrue(int offerId);
    
    @Query("SELECT r FROM Rating r WHERE r.offer.provider.id=:providerId")
    List<Rating> findByProviderId(@Param("providerId") int providerId);
}