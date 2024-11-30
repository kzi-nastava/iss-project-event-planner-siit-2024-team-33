package rs.ac.uns.ftn.asd.Projekatsiit2024.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.OfferCategoryRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.OfferRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.ProviderRepository;

import java.util.List;
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
}
