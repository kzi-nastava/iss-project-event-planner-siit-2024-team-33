package rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;

public interface EventTypeRepository extends JpaRepository<EventType, Integer>{
	@Query("SELECT et FROM EventType et WHERE et.isActive=true")
	public List<EventType> getActiveEventTypes();
	
	@Query("SELECT et FROM EventType et WHERE et.name = :name")
    Optional<EventType> findByName(@Param("name") String name);
	
	@Query("SELECT COUNT(et) > 0 FROM EventType et WHERE LOWER(et.name) = LOWER(:name)")
	boolean existsByNameIgnoreCase(@Param("name") String name);
	
	@Query("SELECT e FROM EventType e WHERE e.id IN :ids AND e.isActive = true")
	List<EventType> findActiveEventsByIds(@Param("ids") List<Integer> ids);
	
	@Query("SELECT DISTINCT et FROM Offer o JOIN o.validEvents et WHERE o.provider.id = :providerId")
	Page<EventType> findDistinctByProviderId(@Param("providerId") Integer providerId, Pageable pageable);
}
