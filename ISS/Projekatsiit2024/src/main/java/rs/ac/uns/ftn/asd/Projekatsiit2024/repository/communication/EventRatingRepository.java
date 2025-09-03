package rs.ac.uns.ftn.asd.Projekatsiit2024.repository.communication;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.communication.EventRating;

public interface EventRatingRepository extends JpaRepository<EventRating, Integer> {
	List<EventRating> findByEventIdAndAcceptedTrue(Integer eventId);
}
