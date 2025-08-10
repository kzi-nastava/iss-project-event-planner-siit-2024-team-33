package rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;

import java.time.LocalDateTime;
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
    
    Page<Event> findByOrganizer(Organizer organizer, Pageable pageable);
    
    @Query("SELECT e FROM Event e WHERE e.organizer.id = :id AND e.dateOfEvent <= :endOfDay AND e.endOfEvent >= :startOfDay")
    List<Event> findByCreatorAndDate(@Param("id") Integer id,
                                     @Param("startOfDay") LocalDateTime startOfDay,
                                     @Param("endOfDay") LocalDateTime endOfDay);
    
    @Query("SELECT e FROM Event e JOIN e.listOfAttendees p " +
    	       "WHERE p.id = :userId AND e.dateOfEvent <= :endOfDay AND e.endOfEvent >= :startOfDay")
    	List<Event> findByParticipantAndDate(@Param("userId") Integer userId,
    	                                     @Param("startOfDay") LocalDateTime startOfDay,
    	                                     @Param("endOfDay") LocalDateTime endOfDay);
    
    
    @Query("""
            SELECT COUNT(e) > 0
            FROM Event e
            WHERE e.organizer.id = :organizerId
            AND e.endOfEvent > CURRENT_TIMESTAMP
        """)
    boolean existsFutureOrOngoingEventsByOrganizerId(@Param("organizerId") Integer organizerId);
    
    
    List<Event> findAllByListOfAttendeesContaining(AuthentifiedUser user);

}
