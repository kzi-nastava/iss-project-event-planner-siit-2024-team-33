package rs.ac.uns.ftn.asd.Projekatsiit2024.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>{
//    List<Event> findTop5ByPlaceAndIsPrivateFalse(String city);
//    List<Event> findAllByPlaceAndIsPrivateFalse(String city, String searchQuery, String sortBy);



}
