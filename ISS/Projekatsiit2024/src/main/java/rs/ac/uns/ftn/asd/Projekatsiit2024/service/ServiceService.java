package rs.ac.uns.ftn.asd.Projekatsiit2024.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.service.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferCategoryRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.ServiceRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventTypeRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.ProviderRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.utils.ImageManager;

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

		String nameRegex = "^[a-zA-Z0-9][a-zA-Z0-9 _'\"-]*$";
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
	
	public Service get(Integer offerID) {
		Service s = serviceRepo.getLatestServiceVersion(offerID);
		if(s == null)
			throw new EntityNotFoundException("Service with that id doesn't exist");
		

	    Authentication up = SecurityContextHolder.getContext().getAuthentication();
	    AuthentifiedUser user = null;
	    if(up.getClass() != AnonymousAuthenticationToken.class) {
	    	UserPrincipal pl = (UserPrincipal)up.getPrincipal();
			user = pl.getUser();
	    }
	    
	    if(s.getIsDeleted() || s.getAvailability() == Availability.INVISIBLE || !s.getCategory().getIsAccepted()) {
	    	if(user != null && user.getId() != s.getProvider().getId())
				throw new EntityNotFoundException("Service with that id doesn't exist");
	    }
			
		return s;
	}
	
	public Service get(Integer offerID, Integer versionID) {
		Service s = serviceRepo.findServiceWithVersion(offerID, versionID);
		if(s == null)
			throw new EntityNotFoundException("Service with that id doesn't exist");
		
		return s;
	}
	
	public Service create(
		Integer categoryID,
		String name,
		String description,
		Double price,
		Double discount,
		List<String> picturesDataURI,
		String city,
		Availability availability,
		Integer reservationInHours,
		Integer cancellationInHours,
		Boolean isAutomatic,
		Integer minLengthInMins,
		Integer maxLengthInMins,
		List<Integer> validEventIDs
		) throws AuthenticationCredentialsNotFoundException, AccessDeniedException {
		
		validateArguments(name, description, price, discount, picturesDataURI, reservationInHours, cancellationInHours, isAutomatic, minLengthInMins, maxLengthInMins, validEventIDs);
		
	    Optional<OfferCategory> category = offerCategoryRepo.findById(categoryID);
	    if(category.isEmpty())
	    	throw new IllegalArgumentException("Inavlid argument, no category with that ID exists");
	    if(category.get().getIsEnabled() == false)
	    	throw new IllegalArgumentException("Inavlid argument, no category with that ID exists");
	    
	    UserPrincipal up = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    if(up == null)
	    	throw new AuthenticationCredentialsNotFoundException("");
	    Provider provider = providerRepo.findById(up.getUser().getId()).orElseThrow();
	    
	    List<EventType> validEvents = eventTypeRepo.findAllById(validEventIDs);
	    //In case some of the event types don't exist
	    if(validEvents.size() < validEventIDs.size())
	    	throw new IllegalArgumentException("Inavlid argument, no event type with that ID exists");
	    //In case some of the event types are disabled
	    if(validEvents.stream().anyMatch(eventType -> !eventType.getIsActive()))
	    	throw new IllegalArgumentException("Inavlid argument, no event type with that ID exists");

	    List<String> imagePaths = picturesDataURI.stream().map(imageData -> ImageManager.saveAsFile(imageData)).toList();
	    
	    Service service = new Service(serviceRepo.getMaxOfferID()+1,name, description, price, discount, imagePaths, category.get(), provider, validEvents, city, availability, reservationInHours, cancellationInHours, isAutomatic, minLengthInMins, maxLengthInMins);
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
			List<String> picturesDataURI,
			String city,
			Availability availability,
			Integer reservationInHours,
			Integer cancellationInHours,
			Boolean isAutomatic,
			Integer minLengthInMins,
			Integer maxLengthInMins,
			List<Integer> validEventIDs
			) throws AuthenticationCredentialsNotFoundException, AccessDeniedException {
			
			validateArguments(name, description, price, discount, picturesDataURI, reservationInHours, cancellationInHours, isAutomatic, minLengthInMins, maxLengthInMins, validEventIDs);
		    
			UserPrincipal up = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    if(up == null)
		    	throw new AuthenticationCredentialsNotFoundException("");
		    Provider provider = providerRepo.findById(up.getUser().getId()).orElseThrow();

		    List<EventType> validEvents = eventTypeRepo.findAllById(validEventIDs);
		    //In case some of the event types don't exist
		    if(validEvents.size() < validEventIDs.size())
		    	throw new IllegalArgumentException("Inavlid argument, no event type with that ID exists");
		    //In case some of the event types are disabled
		    if(validEvents.stream().anyMatch(eventType -> !eventType.getIsActive()))
		    	throw new IllegalArgumentException("Inavlid argument, no event type with that ID exists");
		    
		    OfferCategory oc = offerCategoryService.createSuggestion(categoryName, categoryDescription);

		    List<String> imagePaths = picturesDataURI.stream().map(imageData -> ImageManager.saveAsFile(imageData)).toList();
		    
		    Service service = new Service(serviceRepo.getMaxOfferID()+1,name, description, price, discount, imagePaths, oc, provider, validEvents, city, availability, reservationInHours, cancellationInHours, isAutomatic, minLengthInMins, maxLengthInMins);
		    service = serviceRepo.save(service);
		    
			return service;
		}
	
	public Service cloneService(Service s) {
		return new Service(s.getOfferID(), s.getName(), s.getDescription(), s.getPrice(), s.getDiscount(), s.getPictures(), s.getCategory(), s.getProvider(), new ArrayList<>(s.getValidEvents()), s.getCity(), s.getAvailability(), s.getReservationInHours(), s.getCancellationInHours(), s.getIsAutomatic(), s.getMinLengthInMins(), s.getMaxLengthInMins());
	}
	
	public Service editService(
		Integer offerId,
		String name,
		String description,
		Double price,
		Double discount,
		List<String> picturesDataURI,
		Integer reservationInHours,
		Integer cancellationInHours,
		String city,
		Availability availability,
		Boolean isAutomatic,
		Integer minLengthInMins,
		Integer maxLengthInMins,
		List<Integer> validEventIDs
		) throws AuthenticationCredentialsNotFoundException, AccessDeniedException {
		validateArguments(name, description, price, discount, picturesDataURI, reservationInHours, cancellationInHours, isAutomatic, minLengthInMins, maxLengthInMins, validEventIDs);

		List<EventType> validEvents = eventTypeRepo.findAllById(validEventIDs);
	    //In case some of the event types don't exist
	    if(validEvents.size() < validEventIDs.size())
	    	throw new IllegalArgumentException("Inavlid argument, no event type with that ID exists");
	    //In case some of the event types are disabled
	    if(validEvents.stream().anyMatch(eventType -> !eventType.getIsActive()))
	    	throw new IllegalArgumentException("Inavlid argument, no event type with that ID exists");
	    
	    Service s = serviceRepo.getLatestServiceVersion(offerId);
	    
	    UserPrincipal up = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    if(up == null)
	    	throw new AuthenticationCredentialsNotFoundException("");
	    Provider provider = providerRepo.findById(up.getUser().getId()).orElseThrow();
	    if(s.getProvider().getId() != provider.getId())
	    	throw new AccessDeniedException("");
	    	
	    List<String> imagePaths = picturesDataURI.stream().map(imageData -> ImageManager.saveAsFile(imageData)).toList();
	    
	    Service newService = new Service(s.getOfferID(),name, description, price, discount, imagePaths, s.getCategory(), s.getProvider(), validEvents, city, availability, reservationInHours, cancellationInHours, isAutomatic, minLengthInMins, maxLengthInMins);
	    newService = serviceRepo.save(newService);
		
		return newService;
	}
	
	public Boolean checkIfServiceExists(Integer id) {
		return serviceRepo.findById(id).isPresent();
	}
	
	
	
	public void deleteService(Integer offerId) throws SQLIntegrityConstraintViolationException, AuthenticationCredentialsNotFoundException, AccessDeniedException {
		UserPrincipal up = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    if(up == null)
	    	throw new AuthenticationCredentialsNotFoundException("");
	    Provider provider = providerRepo.findById(up.getUser().getId()).orElseThrow();
		
	    List<Service> services = serviceRepo.findServicesByOfferID(offerId);
	    if(services.stream().anyMatch(s -> s.getOfferReservations().size() > 0))
	    	throw new SQLIntegrityConstraintViolationException("Can't delete service when it has reservations");
	    
	    if(services.stream().anyMatch(s -> s.getProvider().getId() != provider.getId()))
	    	throw new AccessDeniedException("");
	    
	    services.forEach(s -> {
	    	s.setIsDeleted(true);
	    });
	    
	    serviceRepo.saveAll(services);
	    serviceRepo.flush();
	}
}
