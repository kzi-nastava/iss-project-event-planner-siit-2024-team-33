package rs.ac.uns.ftn.asd.Projekatsiit2024.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Product;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private EventRepository eventRepo;
	@Autowired
	private offerReservationService reservationService;
	
	@Transactional
	public Product buyProduct(Integer productId, Integer eventId) {
		Optional<Product> product = productRepo.findById(productId);
		if(product.isEmpty())
			throw new EntityNotFoundException("No product with that id exists");
		
		Optional<Event> event = eventRepo.findById(eventId);
		if(product.isEmpty())
			throw new EntityNotFoundException("No event with that id exists");
		
		if(product.get().getAvailability() != Availability.AVAILABLE)
			throw new IllegalArgumentException("Product is currently unavailable");
		
		product.get().setAvailability(Availability.UNAVAILABLE);
		reservationService.createProductReservation(product.get(), event.get());
		
		return product.get();
	}
}
