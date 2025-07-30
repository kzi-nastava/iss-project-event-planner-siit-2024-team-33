package rs.ac.uns.ftn.asd.Projekatsiit2024.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2024.controller.NotificationController;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventTypeValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Admin;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferCategoryRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventTypeRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AdminRepository;

@Service
public class OfferCategoryService {
	
	@Autowired
	private OfferCategoryRepository offerCategoryRepo;
	@Autowired
	private OfferRepository offerRepo;
	@Autowired
	private NotificationService notifService;
	@Autowired
	private AdminRepository adminRepo;
	
	private void validateData(String name, String description) {
		if(name == null)
			throw new IllegalArgumentException("Illegal argument, name can't be null");
		if(description == null)
			throw new IllegalArgumentException("Illegal argument, name can't be null");
		if(name == "")
			throw new IllegalArgumentException("Illegal argument, name can't be empty");
		if(description == "")
			throw new IllegalArgumentException("Illegal argument, name can't be empty");
	}
	
	//Without database flush
	public OfferCategory create(String name, String description) {
		validateData(name, description);
		OfferCategory oc = new OfferCategory(name, description, true, true);
		oc = offerCategoryRepo.save(oc);
		return oc;
	}
	
	public OfferCategory createAndFlush(String name, String description) {
		validateData(name, description);
		OfferCategory oc = create(name, description);
		offerCategoryRepo.flush();
		return oc;
	}
	
	public OfferCategory editCategory(Integer id, String name, String description, Boolean isEnabled) {
		validateData(name, description);
		
		Optional<OfferCategory> optionalOc = offerCategoryRepo.findById(id);
		if(optionalOc.isEmpty())
			throw new EntityNotFoundException("Invalid argument, no offer category with ID " + id + " exists.");
		
		List<Provider> providers = offerCategoryRepo.getProvidersWithCategory(id);
		providers.forEach(p -> {
			notifService.createNotification(p.getId(), "Offer category \"" + optionalOc.get().getName() +"\" was recently edited.");
		});
		
		OfferCategory oc = optionalOc.get();
		oc.setName(name);
		oc.setDescription(description);
		oc.setIsEnabled(isEnabled);
		
		oc = offerCategoryRepo.saveAndFlush(oc);
		return oc;
	}
	
	public void deleteCategory(Integer id) throws IllegalArgumentException, DataIntegrityViolationException {
		Optional<OfferCategory> optionalOc = offerCategoryRepo.findById(id);
		if(optionalOc.isEmpty())
			throw new EntityNotFoundException("Invalid argument, no offer category with ID " + id + " exists.");
		
		//Check for conflict
		if(offerRepo.existsByCategoryID(id))
			throw new DataIntegrityViolationException("Can't delete a category that is in use by existing offers");
		
		offerCategoryRepo.deleteById(id);
		offerCategoryRepo.flush();
	}
	
	//Without flush
	public OfferCategory createSuggestion(String name, String description) {
		validateData(name, description);
		OfferCategory oc = new OfferCategory(name, description, false, false);
		oc = offerCategoryRepo.save(oc);
		
		adminRepo.findAll().forEach(a -> {
			notifService.createNotification(a.getId(), "New offer category suggestion: \"" + name + "\"");
		});
		
		return oc;
	}
	
	public List<OfferCategory> getOffers(Boolean isAccepted, Boolean isEnabled){
		return offerCategoryRepo.getCategories(isAccepted, isEnabled);
	}
	
	@Transactional
	public OfferCategory acceptSuggestion(Integer id, String name, String description) {
		validateData(name, description);
		Optional<OfferCategory> optionalOc = offerCategoryRepo.findById(id);
		if(optionalOc.isEmpty())
			throw new IllegalArgumentException("Invalid argument, no offer category with ID " + id + " exists.");
		
		OfferCategory oc = optionalOc.get();
		oc.setName(name);
		oc.setDescription(description);
		oc.setIsEnabled(true);
		oc.setIsAccepted(true);
		offerCategoryRepo.save(oc);
		
		List<Offer> affectedOffers = offerRepo.findOffersByCategoryID(id);
		for(Offer o : affectedOffers){
			o.setIsPending(false);
			offerRepo.save(o);
		}
		
		List<Provider> providers = offerCategoryRepo.getProvidersWithCategory(id);
		providers.forEach(p -> {
			notifService.createNotification(p.getId(), "Offer category \"" + optionalOc.get().getName() +"\" was recently edited.");
		});
		
		return oc;
	}
	
	//Reject the suggestion with the the id "id", and set the offer category of pending offers to "newCategoryId"
	@Transactional
	public OfferCategory rejectSuggestion(Integer id, Integer newCategoryId) {
		Optional<OfferCategory> optionalOc = offerCategoryRepo.findById(id);
		if(optionalOc.isEmpty())
			throw new IllegalArgumentException("Invalid argument, no offer category with ID " + id + " exists.");
		
		Optional<OfferCategory> newOC = offerCategoryRepo.findById(newCategoryId);
		if(newOC.isEmpty())
			throw new IllegalArgumentException("Invalid argument, no offer category with ID " + newCategoryId + " exists.");
		
		List<Offer> affectedOffers = offerRepo.findOffersByCategoryID(id);
		for(Offer o : affectedOffers){
			o.setIsPending(false);
			o.setCategory(newOC.get());
			offerRepo.save(o);
		}

		List<Provider> providers = offerCategoryRepo.getProvidersWithCategory(id);
		providers.forEach(p -> {
			notifService.createNotification(p.getId(), "Offer category \"" + optionalOc.get().getName() +"\" was recently edited.");
		});
		
		offerCategoryRepo.deleteById(id);
		
		return newOC.get();
	}
}