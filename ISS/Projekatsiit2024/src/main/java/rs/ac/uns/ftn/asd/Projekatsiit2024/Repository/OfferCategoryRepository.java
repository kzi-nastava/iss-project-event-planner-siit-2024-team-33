package rs.ac.uns.ftn.asd.Projekatsiit2024.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferCategory;

@Repository
public interface OfferCategoryRepository extends JpaRepository<OfferCategory,Integer>{
	@Query("SELECT oc FROM OfferCategory oc WHERE oc.isAccepted=true")
	public List<OfferCategory> getAcceptedCategories();
	
	@Query("SELECT oc FROM OfferCategory oc WHERE oc.isAccepted=false")
	public List<OfferCategory> getPendingCategories();
}
