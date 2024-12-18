package rs.ac.uns.ftn.asd.Projekatsiit2024.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.EventType;

public interface EventTypeRepository extends JpaRepository<EventType, Integer>{
	@Query("SELECT et FROM EventType et WHERE et.isActive=true")
	public List<EventType> getActiveEventTypes();
}
