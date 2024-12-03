package rs.ac.uns.ftn.asd.Projekatsiit2024.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.EventTypeRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.OfferCategoryRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.ProviderRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.ServiceRepository;

@org.springframework.stereotype.Service
public class ServiceService {
	@Autowired
	private ServiceRepository serviceRepo;
	@Autowired
	private OfferCategoryRepository offerCategoryRepo;
	@Autowired
	private ProviderRepository providerRepo;
	@Autowired
	private EventTypeRepository eventTypeRepo;
	
	@Autowired 
	OfferCategoryService offerCategoryService;
	
	private void validateArguments(
			String name,
			String description,
			Double price,
			Double discount,
			List<String> pictures,
			Integer reservationInHours,
			Integer cancellationInHours,
			Boolean isAutomatic,
			Integer minLengthInMins,
			Integer maxLengthInMins,
			List<Integer> validEventIDs
			) {
		if (name == null)
	        throw new IllegalArgumentException("Invalid argument, name cannot be null");

	    if (description == null)
	        throw new IllegalArgumentException("Invalid argument, description cannot be null");

	    if (price == null) 
	        throw new IllegalArgumentException("Invalid argument, price cannot be null");

	    if (discount == null)
	        throw new IllegalArgumentException("Invalid argument, discount cannot be null");

	    if (pictures == null)
	        throw new IllegalArgumentException("Invalid argument, pictures cannot be null");
	    
	    if (reservationInHours == null)
	    	throw new IllegalArgumentException("Inavlid argument, reservation deadline can't be null");
	    
	    if (cancellationInHours == null)
	    	throw new IllegalArgumentException("Inavlid argument, cancellation deadline can't be null");
	    
	    if (minLengthInMins == null)
	    	throw new IllegalArgumentException("Inavlid argument, minimum duration can't be null");

	    if (maxLengthInMins == null)
	    	throw new IllegalArgumentException("Inavlid argument, maximum duration can't be null");
	    
	    if (validEventIDs == null)
	    	throw new IllegalArgumentException("Inavlid argument, valid event types list can't be null");

		String nameRegex = "^[\\w.']{2,}(\\s[\\w.']{2,})+$";
		if(!name.matches(nameRegex))
			throw new IllegalArgumentException("Invalid name");
		
		if(price < 0.0)
			throw new IllegalArgumentException("Invalid argument, the price can't be negative");
		
		if(discount < 0.0)
			throw new IllegalArgumentException("Invalid argument, the discount can't be negative");
		
		if(discount > price)
			throw new IllegalArgumentException("Invalid argument, the discount can't be higher than the price");
		
		if(pictures.size() == 0)
			throw new IllegalArgumentException("Invalid argument, at least one image must be provided");
	    
	    if (reservationInHours <= 0)
	    	throw new IllegalArgumentException("Inavlid argument, reservation deadline can't be negative");
	    
	    if (cancellationInHours <= 0)
	    	throw new IllegalArgumentException("Inavlid argument, cancellation deadline can't be negative");
	    
	    if (minLengthInMins <= 0)
	    	throw new IllegalArgumentException("Inavlid argument, minimum duration can't be negative");
	    
	    if (maxLengthInMins <= 0)
	    	throw new IllegalArgumentException("Inavlid argument, maximum duration can't be negative");
	    
	    if (minLengthInMins > maxLengthInMins)
	    	throw new IllegalArgumentException("Inavlid argument, minimum duration can't be higher than maximum");
	    
	    if(validEventIDs.size() == 0) {
	    	throw new IllegalArgumentException("Inavlid argument, no valid event types provided");
	    }
	}
	
	public Service get(Integer serviceID) {
		Optional<Service> s = serviceRepo.findById(serviceID);
		if(s.isEmpty())
			throw new EntityNotFoundException("Service with that id doesn't exist");
		
		return s.get();
	}
	
	public Service create(
		Integer categoryID,
		String name,
		String description,
		Double price,
		Double discount,
		List<String> pictures,
		Integer providerID,
		Integer reservationInHours,
		Integer cancellationInHours,
		Boolean isAutomatic,
		Integer minLengthInMins,
		Integer maxLengthInMins,
		List<Integer> validEventIDs
		) {
		
		validateArguments(name, description, price, discount, pictures, reservationInHours, cancellationInHours, isAutomatic, minLengthInMins, maxLengthInMins, validEventIDs);
		
	    Optional<OfferCategory> category = offerCategoryRepo.findById(categoryID);
	    if(category.isEmpty())
	    	throw new IllegalArgumentException("Inavlid argument, no category with that ID exists");
	    if(category.get().getIsEnabled() == false)
	    	throw new IllegalArgumentException("Inavlid argument, no category with that ID exists");
	    
	    //TODO: GET PROVIDER THROUGH COOKIE OR SOMETHING
	    Optional<Provider> provider = providerRepo.findById(providerID);
	    if(provider.isEmpty())
	    	throw new IllegalArgumentException("Inavlid argument, no provider with that ID exists");

	    
	    List<EventType> validEvents = eventTypeRepo.findAllById(validEventIDs);
	    //In case some of the event types don't exist
	    if(validEvents.size() < validEventIDs.size())
	    	throw new IllegalArgumentException("Inavlid argument, no event type with that ID exists");
	    //In case some of the event types are disabled
	    if(validEvents.stream().anyMatch(eventType -> !eventType.getIsActive()))
	    	throw new IllegalArgumentException("Inavlid argument, no event type with that ID exists");
	    	
	    Service service = new Service(serviceRepo.getMaxOfferID()+1,name, description, price, discount, pictures, category.get(), provider.get(), validEvents, reservationInHours, cancellationInHours, isAutomatic, minLengthInMins, maxLengthInMins);
	    service = serviceRepo.saveAndFlush(service);
	    
		return service;
	}
	
	@Transactional
	public Service createWithCategory(
			String categoryName,
			String categoryDescription,
			String name,
			String description,
			Double price,
			Double discount,
			List<String> pictures,
			Integer providerID,
			Integer reservationInHours,
			Integer cancellationInHours,
			Boolean isAutomatic,
			Integer minLengthInMins,
			Integer maxLengthInMins,
			List<Integer> validEventIDs
			) {
			
			validateArguments(name, description, price, discount, pictures, reservationInHours, cancellationInHours, isAutomatic, minLengthInMins, maxLengthInMins, validEventIDs);
		    
		    //TODO: GET PROVIDER THROUGH COOKIE OR SOMETHING
		    Optional<Provider> provider = providerRepo.findById(providerID);
		    if(provider.isEmpty())
		    	throw new IllegalArgumentException("Inavlid argument, no provider with that ID exists");

		    List<EventType> validEvents = eventTypeRepo.findAllById(validEventIDs);
		    //In case some of the event types don't exist
		    if(validEvents.size() < validEventIDs.size())
		    	throw new IllegalArgumentException("Inavlid argument, no event type with that ID exists");
		    //In case some of the event types are disabled
		    if(validEvents.stream().anyMatch(eventType -> !eventType.getIsActive()))
		    	throw new IllegalArgumentException("Inavlid argument, no event type with that ID exists");
		    
		    OfferCategory oc = offerCategoryService.createSuggestion(categoryName, categoryDescription);
		    
		    Service service = new Service(serviceRepo.getMaxOfferID()+1,name, description, price, discount, pictures, oc, provider.get(), validEvents, reservationInHours, cancellationInHours, isAutomatic, minLengthInMins, maxLengthInMins);
		    service = serviceRepo.save(service);
		    
			return service;
		}
	
	public Service editService(
		Integer offerId,
		String name,
		String description,
		Double price,
		Double discount,
		List<String> pictures,
		Integer reservationInHours,
		Integer cancellationInHours,
		Boolean isAutomatic,
		Integer minLengthInMins,
		Integer maxLengthInMins,
		List<Integer> validEventIDs
		) {
		validateArguments(name, description, price, discount, pictures, reservationInHours, cancellationInHours, isAutomatic, minLengthInMins, maxLengthInMins, validEventIDs);

		List<EventType> validEvents = eventTypeRepo.findAllById(validEventIDs);
	    //In case some of the event types don't exist
	    if(validEvents.size() < validEventIDs.size())
	    	throw new IllegalArgumentException("Inavlid argument, no event type with that ID exists");
	    //In case some of the event types are disabled
	    if(validEvents.stream().anyMatch(eventType -> !eventType.getIsActive()))
	    	throw new IllegalArgumentException("Inavlid argument, no event type with that ID exists");
	    
	    Service s = serviceRepo.getLatestServiceVersion(offerId);
	    
	    Service newService = new Service(s.getOfferID(),name, description, price, discount, pictures, s.getCategory(), s.getProvider(), validEvents, reservationInHours, cancellationInHours, isAutomatic, minLengthInMins, maxLengthInMins);
	    newService = serviceRepo.save(newService);
		
		return newService;
	}
	
	public Boolean checkIfServiceExists(Integer id) {
		return serviceRepo.findById(id).isPresent();
	}
	
	
	
	public void deleteService() {
		//TODO
	}
}
