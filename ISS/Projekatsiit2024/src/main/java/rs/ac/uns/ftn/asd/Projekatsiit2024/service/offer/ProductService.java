package rs.ac.uns.ftn.asd.Projekatsiit2024.service.offer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offerCategory.PostOfferCategoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.CreateProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.ProviderProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.UpdateProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.product.OfferCategoryValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.product.ProductValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.product.Product;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferCategoryRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferReservationRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.ProductRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventTypeRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.ProviderRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.utils.ImageManager;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private OfferRepository offerRepo;
	@Autowired
	private OfferCategoryRepository offerCategoryRepo;
	@Autowired
	private ProviderRepository providerRepo;
	@Autowired
	private EventTypeRepository eventTypeRepo;
	@Autowired
	private EventRepository eventRepo;
	@Autowired
	private OfferReservationRepository reservationRepo;
	@Autowired
	private OfferReservationService reservationService;
	
	
	public Page<ProviderProductDTO> readProviderProducts(UserPrincipal userPrincipal, 
			Pageable pageable, String query, List<Integer> offerCategoryId, List<Integer> eventTypeId, 
			Double maxPrice, List<Availability> availability) {
		return productRepo.findLatestFilteredProducts(userPrincipal.getUser().getId(), 
				offerCategoryId, eventTypeId, availability, maxPrice, query, pageable)
				.map(ProviderProductDTO::new);
	}
	
	@Transactional
	public Product createProduct(UserPrincipal userPrincipal, CreateProductDTO product) {
		//find already existing offerCategory or create a new one
		OfferCategory offerCategory = null;
		if (product.getIsPending().equals(true)) {
			offerCategory = this.createWithCategory(product.getNewCategory());
		}
		else if (product.getIsPending().equals(false)) {
			offerCategory = this.createWithoutCategory(product.getExistingCategoryId());
		}
		else {
			throw new ProductValidationException("Product has to be created with or without"
					+ "offer category.");
		}
		
		//provider which creates the product
	    Provider provider = providerRepo.findById(userPrincipal.getUser().getId()).orElseThrow();
		    
		//find product event types, all of them have to be active and existing
	    List<EventType> productEventTypes = eventTypeRepo.findActiveEventsByIds(product.getEventTypeIds());
	    if(productEventTypes.size() < product.getEventTypeIds().size())
	    	throw new ProductValidationException("Not all of event types you want "
	    			+ "associated with this product exist or are active.");
	    
	    
	    Product createdProduct = new Product();
	    createdProduct.setOfferID(offerRepo.getMaxOfferID() + 1);
	    createdProduct.setType(OfferType.PRODUCT);
	    createdProduct.setName(product.getName());
	    createdProduct.setDescription(product.getDescription());
	    createdProduct.setPrice(product.getPrice());
	    createdProduct.setDiscount(product.getDiscount());
	    createdProduct.setCategory(offerCategory);
	    createdProduct.setProvider(provider);
	    createdProduct.setValidEvents(productEventTypes);
	    createdProduct.setAvailability(product.getAvailability());
	    createdProduct.setCreationDate(LocalDateTime.now());
	    createdProduct.setIsPending(product.getIsPending());
	    createdProduct.setIsDeleted(false);
	    
	    //checking the validity of data
	    this.isProductDataCorrect(createdProduct);
	    
	    //saving product pictures
	    List<String> pictures = new ArrayList<>();
        if (product.getPictures() != null) {
	        for (String base64Img: product.getPictures()) {
	        	String fileName = ImageManager.saveAsFile(base64Img);
	        	if (fileName != null)
	        		pictures.add(fileName);
	        }
        }
        createdProduct.setPictures(pictures);
	    
		return productRepo.save(createdProduct);
	}
	
	
	
	private OfferCategory createWithoutCategory(Integer existingCategoryId) {
		
	    Optional<OfferCategory> category = 
	    		offerCategoryRepo.findValidProductCategory(existingCategoryId, OfferType.PRODUCT);
	    
	    if(category.isEmpty())
	    	throw new OfferCategoryValidationException("No such existing offer category can be used for creation "
	    			+ "of this product.");
		
		return category.get();
	}
	
	
	private OfferCategory createWithCategory(PostOfferCategoryDTO offerCategory) {
		OfferCategory oc = new OfferCategory();
		oc.setName(offerCategory.getName());
		oc.setDescription(offerCategory.getDescription());
		oc.setIsAccepted(false);
		oc.setIsEnabled(true);
		oc.setOfferType(OfferType.PRODUCT);
		
		this.isOfferCategoryDataCorrect(oc);
		
		return offerCategoryRepo.save(oc);
	}
	
	private boolean isOfferCategoryDataCorrect(OfferCategory offerCategory) {
		if (!Pattern.matches("^.{1,50}$", offerCategory.getName()))
		    throw new OfferCategoryValidationException("Name must be between 1 and 50 characters.");
		if (offerCategoryRepo.existsByNameIgnoreCase(offerCategory.getName()))
		    throw new OfferCategoryValidationException("Offer category name already exists.");
		if (!Pattern.matches("^.{1,250}$", offerCategory.getDescription()))
			throw new OfferCategoryValidationException("Description must be between 1 and 250 characters.");
		
		return true;
	}
	
	
	private boolean isProductDataCorrect(Product product) {
		if (!Pattern.matches("^.{1,50}$", product.getName()))
		    throw new ProductValidationException("Name must be between 1 and 50 characters.");
		if (!Pattern.matches("^.{1,250}$", product.getDescription()))
			throw new ProductValidationException("Description must be between 1 and 250 characters.");
		if (product.getPrice() == null && product.getPrice() < 0)
			throw new ProductValidationException("Price must be specified.");
		if (product.getDiscount() == null || product.getDiscount() > product.getPrice())
		    throw new ProductValidationException("Discount must be less than a price.");
		if (product.getAvailability() == null)
		    throw new ProductValidationException("Availability must be specified.");
		
		return true;
	}
	
	
	
	public Product updateProduct(UserPrincipal userPrincipal, UpdateProductDTO product, Integer id) {
		//product we want to update
		Optional<Product> thisProduct = productRepo.findById(id);
		if (thisProduct.isEmpty())
			throw new ProductValidationException("No product with such id exists.");
		Integer offerId = thisProduct.get().getOfferID();
		
		//getting products newest version
		Product p = productRepo.getLatestProductVersion(offerId);
		if(p == null || p.getIsDeleted().equals(true))
	    	throw new ProductValidationException("Cannot update product with such id.");
	    
	    Provider provider = providerRepo.findById(userPrincipal.getUser().getId()).orElseThrow();
	    if(!p.getProvider().getId().equals(provider.getId()))
	    	throw new ProductValidationException("You cannot update product which you didn't create.");

		
	    //find product event types, all of them have to be active and existing
	    List<EventType> productEventTypes = eventTypeRepo.findActiveEventsByIds(product.getEventTypeIds());
	    if(productEventTypes.size() < product.getEventTypeIds().size())
	    	throw new ProductValidationException("Not all of event types you want "
	    			+ "associated with this product exist or are active.");
	    
	    Product createdProduct = new Product();
	    createdProduct.setOfferID(p.getOfferID());
	    createdProduct.setType(OfferType.PRODUCT);
	    createdProduct.setName(product.getName());
	    createdProduct.setDescription(product.getDescription());
	    createdProduct.setPrice(product.getPrice());
	    createdProduct.setDiscount(product.getDiscount());
	    createdProduct.setCategory(p.getCategory());
	    createdProduct.setProvider(provider);
	    createdProduct.setValidEvents(productEventTypes);
	    createdProduct.setAvailability(product.getAvailability());
	    createdProduct.setCreationDate(p.getCreationDate());
	    createdProduct.setIsPending(p.getIsPending());
	    createdProduct.setIsDeleted(false);
		
	    //checking the validity of data
	    this.isProductDataCorrect(createdProduct);
	    
	    //saving product pictures
	    List<String> pictures = new ArrayList<>();
        if (product.getPictures() != null) {
	        for (String base64Img: product.getPictures()) {
	        	String fileName = ImageManager.saveAsFile(base64Img);
	        	if (fileName != null)
	        		pictures.add(fileName);
	        }
        }
        createdProduct.setPictures(pictures);
	    
		return productRepo.save(createdProduct);
	}
	
	
	
	public void deleteProduct(UserPrincipal userPrincipal, Integer id) {
		//product we want to delete
		Optional<Product> thisProduct = productRepo.findById(id);
		if (thisProduct.isEmpty())
			throw new ProductValidationException("No product with such id exists.");
		if (thisProduct.get().getIsDeleted())
			throw new ProductValidationException("The product with this id is already deleted.");
		Integer offerId = thisProduct.get().getOfferID();
		
		Provider provider = providerRepo.findById(userPrincipal.getUser().getId()).orElseThrow();
	    if(!thisProduct.get().getProvider().getId().equals(provider.getId()))
	    	throw new ProductValidationException("You cannot delete product which you didn't create.");
		
	    List<Product> products = productRepo.findProductsByOfferID(offerId);
	    products.forEach(p -> {
	    	p.setIsDeleted(true);
	    });
	    
	    productRepo.saveAll(products);
	}
	
	
	
	
	
	
	
	
	
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
