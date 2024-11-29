package rs.ac.uns.ftn.asd.Projekatsiit2024.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Service;
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
	
	private void validateArguments(
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
			Integer maxLengthInMins
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
		
		validateArguments(categoryID, name, description, price, discount, pictures, providerID, reservationInHours, cancellationInHours, isAutomatic, minLengthInMins, maxLengthInMins);
		
	    Optional<OfferCategory> category = offerCategoryRepo.findById(categoryID);
	    if(category.isEmpty())
	    	throw new IllegalArgumentException("Inavlid argument, no category with that ID exists");
	    
	    //TODO: GET PROVIDER THROUGH COOKIE OR SOMETHING
	    Optional<Provider> provider = providerRepo.findById(providerID);
	    if(provider.isEmpty())
	    	throw new IllegalArgumentException("Inavlid argument, no provider with that ID exists");

	    
	    Service service = new Service(name, description, price, discount, pictures, category.get(), provider.get(), reservationInHours, cancellationInHours, isAutomatic, minLengthInMins, maxLengthInMins);
	    serviceRepo.saveAndFlush(service);
	    
		return service;
	}
}
