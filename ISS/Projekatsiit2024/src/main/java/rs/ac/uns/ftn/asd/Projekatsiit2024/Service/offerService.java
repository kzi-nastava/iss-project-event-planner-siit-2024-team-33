package rs.ac.uns.ftn.asd.Projekatsiit2024.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.OfferCategoryRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.OfferRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.ProviderRepository;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class offerService {

    @Autowired
    private OfferRepository offerRepo;

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
        
        List<Offer> offers = offerRepo.findCurrentOffers();
        String city = user.getCity();
        List<Offer> filteredEvents = offers.stream()
        		.filter(offer -> city.equalsIgnoreCase(offer.getCity()))
                .filter(offer -> offer.getProvider() == null || !blockedUsers.contains(offer.getProvider()))
                .sorted((o1, o2) -> Double.compare(o1.getDiscount(),o2.getDiscount()))
                .limit(5)
                .toList();
        
    	return filteredEvents;
    }
    
    public List<Offer> getRestOffers(Integer id){
        Optional<AuthentifiedUser> optionalUser = userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("");
        }
        
        AuthentifiedUser user = optionalUser.get();
        List<AuthentifiedUser> blockedUsers = user.getBlockedUsers();
        
        List<Offer> offers = offerRepo.findCurrentOffers();
        String city = user.getCity();

        List<Offer> filteredOffers = offers.stream()
        		.filter(offer -> city.equalsIgnoreCase(offer.getCity()))
                .filter(offer -> offer.getProvider() == null || !blockedUsers.contains(offer.getProvider()))
                .sorted((o1, o2) -> Double.compare(o1.getDiscount(),o2.getDiscount()))
                .limit(5)
                .toList();

        List<Offer> restOffers = offers.stream()
        		//.filter(offer -> city.equalsIgnoreCase(offer.getCity()))
                .filter(offer -> offer.getProvider() == null || !blockedUsers.contains(offer.getProvider()))
                .filter(offer -> !filteredOffers.contains(offer))
                .sorted((o1, o2) -> Double.compare(o1.getDiscount(), o2.getDiscount()))
                .toList();
        
    	return restOffers;
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

    
    public Offer createOffer(
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

       
        Offer offer = new Offer(offerID, name, description, price, discount, pictures, category, provider, validEvents);
        offerRepo.save(offer);
        return offer;
    }

    public Offer createAndFlushOffer(
            Integer offerID,
            String name,
            String description,
            Double price,
            Double discount,
            List<String> pictures,
            Integer categoryId,
            Integer providerId,
            List<EventType> validEvents) {
    	
        Offer offer = createOffer(offerID, name, description, price, discount, pictures, categoryId, providerId, validEvents);
        offerRepo.flush();
        return offer;
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

        Offer offer = new Offer(offerID, name, description, price, discount, pictures, category, provider, validEvents);
        offer.setIsPending(true);
        offerRepo.save(offer);
        // TODO: Add logic to send notifications for suggestions
        return offer;
    }
    
    
    public List<Offer> getFileteredOffers(Boolean isProduct, Boolean isService, String name, String category, int lowestPrice, Availability isAvailable, List<EventType> eventTypes){
    	if(isProduct==false && isService==false) {
    		isProduct=true;
    		isService=true;
    	}
    	List<Offer> offers = offerRepo.findCurrentOffers();
    	
    	if(isProduct && isService) {
    		
    	}else if(isProduct) {
    		offers = offers.stream()
    				.filter(offer -> offer.getType()==OfferType.PRODUCT)
    				.toList();
    	}else {
    		offers = offers.stream()
    				.filter(offer -> offer.getType()==OfferType.SERVICE)
    				.toList();
    	}
    	
    	if(name !="") {
    		offers = offers.stream()
    				.filter(offer -> name!=null && offer.getName().toLowerCase().contains(name.toLowerCase()))
    				.toList();
    	}
    	
    	if(category !="") {
    		offers = offers.stream()
    				.filter(offer -> category!=null && offer.getCategory().getName().toLowerCase().contains(category.toLowerCase()))
    				.toList();
    	}
    	
    	if(isAvailable !=null) {
    		offers = offers.stream()
    				.filter(offer -> offer.getAvailability() == isAvailable)
    				.toList();
    	}
    	
    	if(lowestPrice!=0) {
    		offers = offers.stream()
    				.filter(offer-> offer.getPrice()>lowestPrice)
    				.toList();
    	}
    	
    	if(!eventTypes.isEmpty()) {
    		offers = offers.stream()
                    .filter(offer -> offer.getValidEvents() != null && offer.getValidEvents().stream().anyMatch(eventTypes::contains))
    				.toList();
    	}
    	offers = offers.stream().limit(10).toList();
    	return offers;
    }
}
