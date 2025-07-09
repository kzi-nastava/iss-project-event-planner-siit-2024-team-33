package rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;

import java.sql.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>{
	@Query("SELECT e FROM Event e WHERE e.place = :place AND e.isPrivate = false")
    List<Event> findTop5ByPlaceAndIsPrivateFalse(@Param("place") String place);
	
	@Query("SELECT e FROM Event e WHERE e.place = :place AND e.isPrivate = false " +
		       "AND (:searchQuery IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')))")
	List<Event> findAllByPlaceAndIsPrivateFalse(@Param("place") String place,
            @Param("searchQuery") String searchQuery);
	
    @Query("SELECT e FROM Event e WHERE e.organizer.id = :organizerId")
    List<Event> findByOrganizerId(@Param("organizerId") Integer organizerId);
    
}
