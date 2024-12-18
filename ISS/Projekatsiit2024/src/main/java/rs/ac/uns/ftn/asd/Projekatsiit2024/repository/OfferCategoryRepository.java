package rs.ac.uns.ftn.asd.Projekatsiit2024.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.OfferCategory;

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
}
