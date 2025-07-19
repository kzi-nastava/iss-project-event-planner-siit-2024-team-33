package rs.ac.uns.ftn.asd.Projekatsiit2024.service;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.persistence.EntityNotFoundException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.product.Product;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.service.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferCategoryRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.ProductRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.ServiceRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.ProviderRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@org.springframework.stereotype.Service
public class OfferService {
	private static final int ALL_EVENT_TYPE_ID = 1;
	
    @Autowired
    private OfferRepository offerRepo;
    
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ServiceRepository serviceRepo;
    @Autowired
    private ServiceService serviceService;
    
    @Autowired
    private OfferCategoryRepository offerCategoryRepo;

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private ProviderRepository providerRepo;
    
    @Autowired
    private AuthentifiedUserRepository userRepo;

    public List<Offer> getTop5Offers(Integer id){
        Optional<AuthentifiedUser> optionalUser = userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("");
        }
        
        AuthentifiedUser user = optionalUser.get();
        List<AuthentifiedUser> blockedUsers = user.getBlockedUsers();
        
        List<Offer> offers = offerRepo.findAll();
        String city = user.getCity();
        List<Offer> filteredOffers = offers.stream()
        		.filter(offer -> offer.getCity() != null && city.equalsIgnoreCase(offer.getCity()))
                .filter(offer -> isOfferVisibleForUser(user, offer))
                .sorted((o1, o2) -> Double.compare(o1.getDiscount(),o2.getDiscount()))
                .limit(5)
                .toList();
        
    	return filteredOffers;
    }
    public List<Offer> getTop5OffersUnauthorized() {
        
        List<Offer> offers = offerRepo.findAll();

        List<Offer> filteredOffers = offers.stream()
                .filter(offer -> 
                    !Boolean.TRUE.equals(offer.getIsDeleted()) &&
                    !Boolean.TRUE.equals(offer.getIsPending()) &&
                    offer.getAvailability() == Availability.AVAILABLE &&
                    offer.getPrice() != null && offer.getPrice() <= 200
                )
                .sorted((o1, o2) -> Double.compare(o2.getDiscount(), o1.getDiscount()))
                .limit(5)
                .toList();

        return filteredOffers;
    }
    
    
    
    private void validateArguments(
            Integer offerID,
            String name,
            String description,
            Double price,
            Double discount,
            List<String> pictures,
            Integer categoryId,
            Integer providerId,
            List<EventType> validEvents) {
        
        if (offerID == null)
            throw new IllegalArgumentException("Invalid argument: Offer ID cannot be null");
        
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Invalid argument: Name cannot be null or empty");

        if (description == null || description.isBlank())
            throw new IllegalArgumentException("Invalid argument: Description cannot be null or empty");

        if (price == null || price < 0.0)
            throw new IllegalArgumentException("Invalid argument: Price must be non-negative");

        if (discount == null || discount < 0.0)
            throw new IllegalArgumentException("Invalid argument: Discount must be non-negative");

        if (discount > price)
            throw new IllegalArgumentException("Invalid argument: Discount cannot be greater than price");

        if (pictures == null || pictures.isEmpty())
            throw new IllegalArgumentException("Invalid argument: At least one picture must be provided");

        if (categoryId == null)
            throw new IllegalArgumentException("Invalid argument: Category ID cannot be null");

        if (providerId == null)
            throw new IllegalArgumentException("Invalid argument: Provider ID cannot be null");

        if (validEvents == null || validEvents.isEmpty())
            throw new IllegalArgumentException("Invalid argument: At least one valid event must be provided");

        if (validEvents.stream().anyMatch(event -> !event.getIsActive()))
            throw new IllegalArgumentException("Invalid argument: All valid events must be active");

    }

    public Offer createSuggestion(
            Integer offerID,
            String name,
            String description,
            Double price,
            Double discount,
            List<String> pictures,
            Integer categoryId,
            Integer providerId,
            List<EventType> validEvents) {
        validateArguments(offerID, name, description, price, discount, pictures, categoryId, providerId, validEvents);

    	OfferCategory category = offerCategoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("OfferCategory not found"));
        Provider provider = providerRepo.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        Offer offer = new Offer(offerID, name, description, price, discount, pictures, category, provider, validEvents, "");
        offer.setIsPending(true);
        offerRepo.save(offer);
        // TODO: Add logic to send notifications for suggestions
        return offer;
    }
    
    

    
    public Page<Offer> getFilteredOffers(Boolean isProduct, Boolean isService, String name, String category,
            Integer lowestPrice, Availability isAvailable, List<Integer> eventTypes,
            Integer userId, int page, int size) {
			if (Boolean.FALSE.equals(isProduct) && Boolean.FALSE.equals(isService)) {
				isProduct = true;
				isService = true;
			}
			
			List<Offer> offers = getRestOffers(userId);
			
			if (isProduct && !isService) {
				offers = offers.stream()
							.filter(offer -> offer.getType() == OfferType.PRODUCT)
							.toList();
			} else if (!isProduct && isService) {
					offers = offers.stream()
					.filter(offer -> offer.getType() == OfferType.SERVICE)
					.toList();
			}
			
			if (name != null && !name.isEmpty()) {
					offers = offers.stream()
					.filter(offer -> offer.getName() != null && offer.getName().toLowerCase().contains(name.toLowerCase()))
					.toList();
			}
			
			if (category != null && !category.isEmpty()) {
					offers = offers.stream()
					.filter(offer -> offer.getCategory() != null &&
					offer.getCategory().getName().toLowerCase().contains(category.toLowerCase()))
					.toList();
			}
			
			if (isAvailable != null) {
					offers = offers.stream()
					.filter(offer -> offer.getAvailability() == isAvailable)
					.toList();
			}
			
			if (lowestPrice != null && lowestPrice > 0) {
					offers = offers.stream()
					.filter(offer -> offer.getPrice() != null && offer.getPrice() > lowestPrice)
					.toList();
			}
			if (eventTypes != null && !eventTypes.isEmpty()) {
		        if (eventTypes.contains(ALL_EVENT_TYPE_ID)) {
		            offers = offers.stream()
		                    .filter(offer -> offer.getValidEvents() != null && !offer.getValidEvents().isEmpty())
		                    .toList();
		        } else {
		            offers = offers.stream()
		                    .filter(offer -> offer.getValidEvents() != null &&
		                            offer.getValidEvents().stream()
		                                    .map(EventType::getId)
		                                    .anyMatch(eventTypes::contains))
		                    .toList();
		        }
		    }
			
			
			int start = page * size;
			int end = Math.min(start + size, offers.size());
			List<Offer> pageContent = (start >= offers.size()) ? Collections.emptyList() : offers.subList(start, end);

			return new PageImpl<>(pageContent, PageRequest.of(page, size), offers.size());
    }
    
    
    	
    public Page<Offer> getFilteredOffersUnauthorized(Boolean isProduct, Boolean isService, String name, String category,
            Integer lowestPrice, Availability isAvailable, List<Integer> eventTypes, int page, int size) {
			if (Boolean.FALSE.equals(isProduct) && Boolean.FALSE.equals(isService)) {
				isProduct = true;
				isService = true;
			}
			
			List<Offer> offers = getRestOffersUnauthorized();
			
			if (isProduct && !isService) {
				offers = offers.stream()
							.filter(offer -> offer.getType() == OfferType.PRODUCT)
							.toList();
			} else if (!isProduct && isService) {
					offers = offers.stream()
					.filter(offer -> offer.getType() == OfferType.SERVICE)
					.toList();
			}
			
			if (name != null && !name.isEmpty()) {
					offers = offers.stream()
					.filter(offer -> offer.getName() != null && offer.getName().toLowerCase().contains(name.toLowerCase()))
					.toList();
			}
			
			if (category != null && !category.isEmpty()) {
					offers = offers.stream()
					.filter(offer -> offer.getCategory() != null &&
					offer.getCategory().getName().toLowerCase().contains(category.toLowerCase()))
					.toList();
			}
			
			if (isAvailable != null) {
					offers = offers.stream()
					.filter(offer -> offer.getAvailability() == isAvailable)
					.toList();
			}
			
			if (lowestPrice != null && lowestPrice > 0) {
					offers = offers.stream()
					.filter(offer -> offer.getPrice() != null && offer.getPrice() > lowestPrice)
					.toList();
			}
			
			if (eventTypes != null && !eventTypes.isEmpty()) {
		        if (eventTypes.contains(ALL_EVENT_TYPE_ID)) {
		            offers = offers.stream()
		                    .filter(offer -> offer.getValidEvents() != null && !offer.getValidEvents().isEmpty())
		                    .toList();
		        } else {
		            offers = offers.stream()
		                    .filter(offer -> offer.getValidEvents() != null &&
		                            offer.getValidEvents().stream()
		                                    .map(EventType::getId)
		                                    .anyMatch(eventTypes::contains))
		                    .toList();
		        }
		    }
			
			
			int start = page * size;
			int end = Math.min(start + size, offers.size());
			List<Offer> pageContent = (start >= offers.size()) ? Collections.emptyList() : offers.subList(start, end);

			return new PageImpl<>(pageContent, PageRequest.of(page, size), offers.size());
    }
    
    public List<Offer> getRestOffers(Integer id) {
    	List<Offer> allOffers = offerRepo.findCurrentOffers();
    	
        Optional<AuthentifiedUser> optionalUser = userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException(""); 
        }
        
        AuthentifiedUser user = optionalUser.get();
        List<AuthentifiedUser> blockedUsers = user.getBlockedUsers();

        String city = user.getCity();
        
    	
    	List<Offer> top5Offers = getTop5Offers(id);
        
        List<Offer> offers = allOffers.stream()
        		//.filter(offer -> city.equalsIgnoreCase(offer.getCity()))
                .filter(offer -> isOfferVisibleForUser(user, offer))
                .filter(offer -> !top5Offers.contains(offer))
                .sorted((o1, o2) -> Double.compare(o1.getDiscount(), o2.getDiscount()))
                .toList();
    	
    	
    	return offers;
    }
    
    public List<Offer> getRestOffersUnauthorized() {
    	List<Offer> allOffers = offerRepo.findCurrentOffers();
    	
    	List<Offer> top5Offers = getTop5OffersUnauthorized();
        
        List<Offer> offers = allOffers.stream()
                .filter(offer -> !top5Offers.contains(offer))
                .sorted((o1, o2) -> Double.compare(o1.getDiscount(), o2.getDiscount()))
                .toList();
    	
    	
    	return offers;
    }
    
    private boolean isOfferVisibleForUser(AuthentifiedUser user, Offer offer) {
        AuthentifiedUser provider = offer.getProvider();
        if (provider == null) {
            return true; // Safety measures.
        }

        //if logged in user is blocked, can't see.
        return !user.getBlockedUsers().contains(provider);
    }
    
    public void editOfferPrice(Integer OfferId, double newPrice, double newDiscount) throws BadRequestException {
    	Offer o = offerRepo.getLatestOfferVersion(OfferId);
    	if (o == null)
    		throw new EntityNotFoundException();
    	
    	UserPrincipal up = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (up.getUser().getEmail() != o.getProvider().getEmail())
    		throw new BadRequestException();
    	
    	if (newPrice == o.getPrice() && newDiscount == o.getDiscount())
    		return;
    	
    	if (newPrice < newDiscount)
    		newDiscount = 0.0;
    	
    	if (newPrice < 0.0)
    		throw new BadRequestException();
    	
    	if (newDiscount < 0.0)
    		throw new BadRequestException();
    	
    	if(o.getType() == OfferType.PRODUCT) {
    		Product p = productRepo.getLatestProductVersion(OfferId);
    		Product p2 = productService.cloneProduct(p);
    		p2.setDiscount(newDiscount);
    		p2.setPrice(newPrice);
    		productRepo.save(p2);
    	}
    	else {
    		Service s = serviceRepo.getLatestServiceVersion(OfferId);
    		Service s2 = serviceService.cloneService(s);
    		s2.setDiscount(newDiscount);
    		s2.setPrice(newPrice);
    		serviceRepo.save(s2);
    	}
    }
}