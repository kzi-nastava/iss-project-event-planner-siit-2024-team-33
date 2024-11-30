package rs.ac.uns.ftn.asd.Projekatsiit2024.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer>{
	@Query("SELECT nvl(max(o.offerID),0) from Offer o")
	public Integer getMaxOfferID();
}
