package rs.ac.uns.ftn.asd.Projekatsiit2024.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer>{
	@Query("SELECT nvl(max(o.offerID),0) from Offer o")
	public Integer getMaxOfferID();
	
	@Query("SELECT s FROM Service s WHERE s.id=(SELECT max(s2.id) FROM Service s2 WHERE s2.offerID=:offerId)")
	public Service getLatestServiceVersion(@Param("offerId") Integer offerId);
	
	@Query("SELECT s FROM Service s WHERE s.id=:versionId AND s.offerID=:offerId")
	public Service findServiceWithVersion(@Param("offerId") Integer offerId, @Param("versionId") Integer versionId);
}
