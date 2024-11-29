package rs.ac.uns.ftn.asd.Projekatsiit2024.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.EventType;

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
	
	@Query("SELECT e FROM Event e " +
	        "WHERE (:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
	        "AND (:place IS NULL OR LOWER(e.place) LIKE LOWER(CONCAT('%', :place, '%'))) " +
	        "AND (:numOfAttendees IS NULL OR e.numOfAttendees = :numOfAttendees) " +
	        "AND (:startDate IS NULL OR e.dateOfEvent >= :startDate) " +
	        "AND (:endDate IS NULL OR e.dateOfEvent <= :endDate) " +
	        "AND (:eventTypes IS NULL OR EXISTS (SELECT et FROM e.eventTypes et WHERE et IN :eventTypes))")
	List<Event> findAllByFilters(
	        @Param("name") String name,
	        @Param("place") String place,
	        @Param("numOfAttendees") Integer numOfAttendees,
	        @Param("startDate") Date startDate,
	        @Param("endDate") Date endDate,
	        @Param("eventTypes") List<EventType> eventTypes);



}
