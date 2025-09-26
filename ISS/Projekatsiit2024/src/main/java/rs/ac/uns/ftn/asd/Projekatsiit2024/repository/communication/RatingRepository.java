package rs.ac.uns.ftn.asd.Projekatsiit2024.repository.communication;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.communication.Rating;

public interface RatingRepository extends JpaRepository<Rating, Integer>{
    List<Rating> findByOfferId(int offerId);
    List<Rating> findByOfferIdAndAcceptedTrue(int offerId);
    List<Rating> findByIsDeletedFalseAndAcceptedTrue();
	List<Rating> findByOfferIdAndIsDeletedFalse(int offerId);
	List<Rating> findByAcceptedFalseAndIsDeletedFalse();
	Page<Rating> findByAcceptedFalseAndIsDeletedFalse(Pageable pageable);
	Page<Rating> findByIsDeletedFalseAndAcceptedTrue(Pageable pageable);
	Page<Rating> findByIsDeletedFalse(Pageable pageable);
	Page<Rating> findByIsDeletedFalseAndAcceptedFalse(Pageable pageable);
}