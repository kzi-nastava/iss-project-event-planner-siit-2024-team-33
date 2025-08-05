package rs.ac.uns.ftn.asd.Projekatsiit2024.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;

public interface OfferRepository extends JpaRepository<Offer,Integer>{
	List<Offer> findTop5ByOrderByDiscount();
	
	@Query("SELECT nvl(max(o.offerID),0) from Offer o")
	public Integer getMaxOfferID();
	
	
	//TODO: Fix Offers up a bit, and then add a few other methods such as top5 and stuff like that
	@Query("SELECT o FROM Offer o WHERE (:searchQuery IS NULL OR LOWER(o.name) LIKE LOWER(CONCAT('%', :searchQuery, '%'))) " +
           "ORDER BY CASE WHEN :sortBy = 'price' THEN o.price END ASC")
    List<Offer> findAllWithFilters(@Param("searchQuery") String searchQuery, @Param("sortBy") String sortBy);
    
    @Query("SELECT o FROM Offer o WHERE o.category.id=:categoryId")
    List<Offer> findOffersByCategoryID(@Param("categoryId") Integer categoryId);

    @Query("SELECT CASE WHEN count(o)> 0 THEN true ELSE false END FROM Offer o WHERE o.category.id=:categoryId")
    Boolean existsByCategoryID(@Param("categoryId") Integer categoryId);
    
    List<Offer> findByName(String name);
    
    @Query("SELECT o FROM Offer o WHERE o.id IN (SELECT MAX(o2.id) FROM Offer o2 WHERE o2.offerID IS NOT NULL GROUP BY o2.offerID)")
    List<Offer> findCurrentOffers();
    
    @Query("""
    	    SELECT o FROM Offer o
    	    WHERE o.creationDate = (
    	        SELECT MAX(o2.creationDate)
    	        FROM Offer o2
    	        WHERE o2.offerID = o.offerID
    	    )
    	    ORDER BY o.offerID ASC
    	""")
    	List<Offer> findLatestOffersByOfferID();
    
	@Query("SELECT o FROM Offer o WHERE o.id=(SELECT max(o2.id) FROM Offer o2 WHERE o2.offerID=:offerId)")
	public Offer getLatestOfferVersion(@Param("offerId") Integer offerId);
	
}
