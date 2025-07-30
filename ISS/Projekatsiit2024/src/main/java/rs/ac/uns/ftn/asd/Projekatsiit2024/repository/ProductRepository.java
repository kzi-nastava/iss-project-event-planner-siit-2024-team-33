package rs.ac.uns.ftn.asd.Projekatsiit2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.product.Product;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.service.Service;

public interface ProductRepository extends JpaRepository<Product,Integer>{
	@Query("SELECT p FROM Product p WHERE p.id=(SELECT max(p2.id) FROM Product p2 WHERE p2.offerID=:offerId)")
	public Product getLatestProductVersion(@Param("offerId") Integer offerId);
}
