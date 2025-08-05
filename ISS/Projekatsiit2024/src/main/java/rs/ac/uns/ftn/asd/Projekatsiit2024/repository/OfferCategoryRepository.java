package rs.ac.uns.ftn.asd.Projekatsiit2024.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;

@Repository
public interface OfferCategoryRepository extends JpaRepository<OfferCategory,Integer>{
	@Query("SELECT oc FROM OfferCategory oc WHERE oc.isAccepted=true")
	public List<OfferCategory> getAcceptedCategories();
	
	@Query("SELECT oc FROM OfferCategory oc WHERE oc.isAccepted=false")
	public List<OfferCategory> getPendingCategories();
	
	@Query("SELECT o FROM OfferCategory o " +
	           "WHERE (:isAccepted IS NULL OR o.isAccepted = :isAccepted) " +
	           "AND (:isEnabled IS NULL OR o.isEnabled = :isEnabled)")
	public List<OfferCategory> getCategories(@Param("isAccepted") Boolean isAccepted, @Param("isEnabled") Boolean isEnabled);
	
	@Query("SELECT oc FROM OfferCategory oc WHERE oc.id = :id AND oc.isAccepted = true AND oc.isEnabled = true")
	public Optional<OfferCategory> getAvailableOfferCategory(@Param("id") Integer id);
	
	@Query("SELECT DISTINCT o.provider FROM Offer o WHERE o.category.id = :id")
	public List<Provider> getProvidersWithCategory(@Param("id") Integer id);
	
	
	@Query("SELECT c FROM OfferCategory c WHERE c.id = :id AND c.isAccepted = true AND c.isEnabled = true AND c.offerType = :offerType")
	Optional<OfferCategory> findValidProductCategory(@Param("id") Integer id, @Param("offerType") OfferType offerType);
	
	@Query("SELECT c FROM OfferCategory c WHERE c.isAccepted = true AND c.isEnabled = true AND c.offerType = :offerType")
	List<OfferCategory> findAvailableProductCategories(@Param("offerType") OfferType offerType);
	
	@Query("SELECT COUNT(oc) > 0 FROM OfferCategory oc WHERE LOWER(oc.name) = LOWER(:name)")
	boolean existsByNameIgnoreCase(@Param("name") String name);
}
