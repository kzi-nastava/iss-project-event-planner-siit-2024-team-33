package rs.ac.uns.ftn.asd.Projekatsiit2024.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.product.Product;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferReservationRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.ProductRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private EventRepository eventRepo;
	@Autowired
	private OfferReservationRepository reservationRepo;
	@Autowired
	private offerReservationService reservationService;
	
	public Product get(Integer offerId) {
		Product product = productRepo.getLatestProductVersion(offerId);
		if(product == null)
			throw new EntityNotFoundException("No product with that id exists");
		
		return product;
	}
	
	@Transactional
	public Product buyProduct(Integer productId, Integer eventId) throws AccessDeniedException {
		Optional<Product> product = productRepo.findById(productId);
		if(product.isEmpty())
			throw new EntityNotFoundException("No product with that id exists");
		
		Optional<Event> event = eventRepo.findById(eventId);
		if(product.isEmpty())
			throw new EntityNotFoundException("No event with that id exists");
		
		UserPrincipal up = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    if(up == null)
	    	throw new AuthenticationCredentialsNotFoundException("User not logged in");
	    if(event.get().getOrganizer().getId() != up.getUser().getId())
	    	throw new AccessDeniedException("Wrong user");
		
		if(product.get().getAvailability() != Availability.AVAILABLE)
			throw new IllegalArgumentException("Product is currently unavailable");
		
		if(reservationRepo.findByEventAndOffer(productId, eventId) != null)
			throw new IllegalArgumentException("Reservation already exists");
		
//		if(!product.get().getValidEvents().contains(event.get().getEventType()))
//			throw new IllegalArgumentException("Reservation already exists");
		
		product.get().setAvailability(Availability.UNAVAILABLE);
		productRepo.save(product.get());
		reservationService.createProductReservation(product.get(), event.get());
		
		return product.get();
	}
	
	@Transactional
	public Product cancelReservation(Integer productId, Integer eventId) throws AccessDeniedException {
		Optional<Product> product = productRepo.findById(productId);
		if(product.isEmpty())
			throw new EntityNotFoundException("No product with that id exists");
		
		Optional<Event> event = eventRepo.findById(eventId);
		if(product.isEmpty())
			throw new EntityNotFoundException("No event with that id exists");
		
		UserPrincipal up = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    if(event.get().getOrganizer().getId() != up.getUser().getId())
	    	throw new AccessDeniedException("Wrong user");
		
		product.get().setAvailability(Availability.AVAILABLE);
		productRepo.save(product.get());
		reservationService.cancelProductReservation(product.get(), event.get());
		
		return product.get();
	}
	
	public Product cloneProduct(Product p) {
		return new Product(p.getOfferID(), p.getName(), p.getDescription(), p.getPrice(), p.getDiscount(), p.getPictures(), p.getCategory(), p.getProvider(), new ArrayList<>(p.getValidEvents()), p.getCity(), p.getAvailability());
	}
}
