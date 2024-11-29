package rs.ac.uns.ftn.asd.Projekatsiit2024.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Offer;

public interface OfferRepository extends JpaRepository<Offer,Integer>{
	List<Offer> findTop5ByOrderByDiscount();
	//TODO: Fix Offers up a bit, and then add a few other methods such as top5 and stuff like that
    @Query("SELECT o FROM Offer o WHERE (:searchQuery IS NULL OR LOWER(o.name) LIKE LOWER(CONCAT('%', :searchQuery, '%'))) " +
           "ORDER BY CASE WHEN :sortBy = 'price' THEN o.price END ASC")
    List<Offer> findAllWithFilters(@Param("searchQuery") String searchQuery, @Param("sortBy") String sortBy);
}
