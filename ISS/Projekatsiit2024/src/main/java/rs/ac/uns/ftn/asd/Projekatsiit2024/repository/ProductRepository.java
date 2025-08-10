package rs.ac.uns.ftn.asd.Projekatsiit2024.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.product.Product;

public interface ProductRepository extends JpaRepository<Product,Integer>{
	@Query("SELECT p FROM Product p WHERE p.id=(SELECT max(p2.id) FROM Product p2 WHERE p2.offerID=:offerId)")
	public Product getLatestProductVersion(@Param("offerId") Integer offerId);
	
	@Query("SELECT p FROM Product p WHERE p.provider.id = :providerId")
	Page<Product> findProductsByProvider(@Param("providerId") Integer providerId, Pageable pageable);
	
	@Query("""
		    SELECT p FROM Product p 
		    WHERE p.id IN (
		        SELECT MAX(p2.id) 
		        FROM Product p2 
		        WHERE p2.provider.id = :providerId 
		        GROUP BY p2.offerID
		    )
		""")
	Page<Product> findLatestProductsByProvider(@Param("providerId") Integer providerId, Pageable pageable);
	
	
	@Query("""
			  SELECT DISTINCT p FROM Product p
			  LEFT JOIN p.validEvents e
			  WHERE p.id IN (
			    SELECT MAX(p2.id) FROM Product p2
			    WHERE p2.provider.id = :providerId
			    GROUP BY p2.offerID
			  )
			  AND (:categoryIds IS NULL OR p.category.id IN :categoryIds)
			  AND (:maxPrice IS NULL OR p.price <= :maxPrice)
			  AND (:availabilities IS NULL OR p.availability IN :availabilities)
			  AND (:query IS NULL OR 
			       LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR 
			       LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')))
			  AND (:eventTypeIds IS NULL OR e.id IN :eventTypeIds)
			""")
	Page<Product> findLatestFilteredProducts(
	  @Param("providerId") Integer providerId,
	  @Param("categoryIds") List<Integer> categoryIds,
	  @Param("eventTypeIds") List<Integer> eventTypeIds,
	  @Param("availabilities") List<Availability> availabilities,
	  @Param("maxPrice") Double maxPrice,
	  @Param("query") String query,
	  Pageable pageable
	);
	
	
	
	
	
	@Query("SELECT p FROM Product p WHERE p.offerID=:offerId")
	public List<Product> findProductsByOfferID(@Param("offerId") Integer offerId);
	
	Optional<Product> findById(Integer id);
}
