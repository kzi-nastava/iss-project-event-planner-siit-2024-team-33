package rs.ac.uns.ftn.asd.Projekatsiit2024.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferCategoryRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferRepository;

@Service
public class OfferCategoryService {
	
	@Autowired
	private OfferCategoryRepository offerCategoryRepo;
	@Autowired
	private OfferRepository offerRepo;
	
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
		//TODO: User validation
		validateData(name, description);
		OfferCategory oc = new OfferCategory(name, description, true, true);
		oc = offerCategoryRepo.save(oc);
		return oc;
	}
	
	public OfferCategory createAndFlush(String name, String description) {
		//TODO: User validation
		validateData(name, description);
		OfferCategory oc = create(name, description);
		offerCategoryRepo.flush();
		return oc;
	}
	
	public OfferCategory editCategory(Integer id, String name, String description, Boolean isEnabled) {
		//TODO: Validation
		//TODO: Notify PUP about changes
		//TODO: Admin validation
		validateData(name, description);
		
		Optional<OfferCategory> optionalOc = offerCategoryRepo.findById(id);
		if(optionalOc.isEmpty())
			throw new IllegalArgumentException("Invalid argument, no offer category with ID " + id + " exists.");
		
		OfferCategory oc = optionalOc.get();
		oc.setName(name);
		oc.setDescription(description);
		oc.setIsEnabled(isEnabled);
		
		oc = offerCategoryRepo.saveAndFlush(oc);
		return oc;
	}
	
	public void deleteCategory(Integer id) throws IllegalArgumentException, DataIntegrityViolationException {
		//TODO: Admin validation
		
		Optional<OfferCategory> optionalOc = offerCategoryRepo.findById(id);
		if(optionalOc.isEmpty())
			throw new IllegalArgumentException("Invalid argument, no offer category with ID " + id + " exists.");
		
		//Check for conflict
		if(offerRepo.existsByCategoryID(id))
			throw new DataIntegrityViolationException("Can't delete a category that is in use by existing offers");
		
		offerCategoryRepo.deleteById(id);
		offerCategoryRepo.flush();
	}
	
	//Without flush
	public OfferCategory createSuggestion(String name, String description) {
		//TODO: User validation
		validateData(name, description);
		OfferCategory oc = new OfferCategory(name, description, false, false);
		oc = offerCategoryRepo.save(oc);
		//TODO: Send notification
		return oc;
	}
	
	public List<OfferCategory> getOffers(Boolean isAccepted, Boolean isEnabled){
		return offerCategoryRepo.getCategories(isAccepted, isEnabled);
	}
	
	@Transactional
	public OfferCategory acceptSuggestion(Integer id, String name, String description) {
		//TODO: Admin validation
		//TODO: Notify PUP
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
		
		return oc;
	}
	
	//Reject the suggestion withe the id "id", and set the offer category of pending offers to "newCategoryId"
	@Transactional
	public OfferCategory rejectSuggestion(Integer id, Integer newCategoryId) {
		//TODO: Admin validation
		//TODO: Notify PUP
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
		
		offerCategoryRepo.deleteById(id);
		
		return newOC.get();
	}
}
