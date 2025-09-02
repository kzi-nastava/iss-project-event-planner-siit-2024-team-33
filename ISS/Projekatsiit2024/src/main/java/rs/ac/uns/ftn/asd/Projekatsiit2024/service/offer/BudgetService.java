package rs.ac.uns.ftn.asd.Projekatsiit2024.service.offer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ch.qos.logback.core.spi.ConfigurationEvent.EventType;
import jakarta.persistence.EntityNotFoundException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget.BudgetItemDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget.BudgetOfferDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget.GetBudgetDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offerCategory.MinimalOfferCategoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.BudgetItem;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.BudgetItemRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferCategoryRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferReservationRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;

@Service
public class BudgetService {
	@Autowired
	private BudgetItemRepository budgetItemRepo;
	@Autowired
	private OfferReservationRepository offerReservationRepo;
	@Autowired
	private EventRepository eventRepo;
	@Autowired
	private OfferCategoryRepository offerCategoryRepo;
	
	private BudgetItem getOrCreateItem(Integer eventId, Integer categoryId) {
		BudgetItem bi = budgetItemRepo.findByEventAndCategory(eventId, categoryId);
		if(bi != null)
			return bi;
		
		return createItem(eventId, categoryId, 0.0);
	}
	
	public Double getUsedBudget(BudgetItem bi) {
		List<Offer> offers = offerReservationRepo.findByBudgetItem(bi);
		return offers.stream().mapToDouble(offer -> offer.getPrice() - offer.getDiscount()).sum();
	}
	
	public BudgetItem createItem(Integer eventId, Integer categoryId, Double maxBudget) {
		BudgetItem bi = budgetItemRepo.findByEventAndCategory(eventId, categoryId);
		if (bi != null)
			throw new DataIntegrityViolationException("Budget item already exists");
		if(maxBudget < 0)
			throw new IllegalArgumentException("Budget can't be negative");
		
		Optional<Event> e = eventRepo.findById(eventId);
		if(e.isEmpty())
			throw new EntityNotFoundException("Nonexistent event");
		
		UserPrincipal up = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    if(up == null)
	    	throw new AuthenticationCredentialsNotFoundException("User not logged in");
	    if(e.get().getOrganizer().getId() != up.getUser().getId())
	    	throw new AccessDeniedException("Wrong user");
		
		Optional<OfferCategory> oc = offerCategoryRepo.findById(categoryId);
		if(oc.isEmpty())
			throw new EntityNotFoundException("Nonexistent offer category");
		
		bi = new BudgetItem();
		bi.setBudget(maxBudget);
		bi.setEvent(e.get());
		bi.setBudgetCategory(oc.get());
		
		bi = budgetItemRepo.saveAndFlush(bi);
		return bi;
	}
	
	public BudgetItem editItem(Integer eventId, Integer categoryId, Double maxBudget) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if(auth == null)
	    	throw new AuthenticationCredentialsNotFoundException("User not logged in");
		
		UserPrincipal up = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    if(up == null)
	    	throw new AuthenticationCredentialsNotFoundException("User not logged in");
		
		BudgetItem bi = budgetItemRepo.findByEventAndCategory(eventId, categoryId);
		if (bi == null)
			throw new EntityNotFoundException();
	    if(bi.getEvent().getOrganizer().getId() != up.getUser().getId())
	    	throw new AccessDeniedException("Wrong user");
		if(maxBudget < 0)
			throw new IllegalArgumentException("Budget can't be negative");
		if(maxBudget < getUsedBudget(bi))
			throw new IllegalArgumentException("Budget can't be lower than the total spend money");
		
		bi.setBudget(maxBudget);
		bi = budgetItemRepo.saveAndFlush(bi);
		return bi;
	}
	
	public void deleteItem(Integer eventId, Integer categoryId) {
		Optional<Event> e = eventRepo.findById(eventId);
		if(e.isEmpty())
			throw new EntityNotFoundException("Nonexistent event");
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if(auth == null)
	    	throw new AuthenticationCredentialsNotFoundException("User not logged in");
		
		UserPrincipal up = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    if(up == null)
	    	throw new AuthenticationCredentialsNotFoundException("User not logged in");
	    if(e.get().getOrganizer().getId() != up.getUser().getId())
	    	throw new AccessDeniedException("Wrong user");
		
		BudgetItem bi = budgetItemRepo.findByEventAndCategory(eventId, categoryId);
		if(bi == null)
			throw new EntityNotFoundException("Nonexistent budget item");
		
		//Whether the selected budget item contains any reservations
		Boolean hasReservations = false;
		if(e.get().getReservations() != null)
			hasReservations = e.get().getReservations().stream()
				.map(reservation -> reservation.getOffer()).anyMatch(offer -> offer.getCategoryId() == categoryId);

		if(hasReservations)
			throw new DataIntegrityViolationException("Can't delete the budget item due to reservations for that item existing");
		
		budgetItemRepo.delete(bi);
		budgetItemRepo.flush();
	}
	
	public GetBudgetDTO getBudget(Integer eventId){
		GetBudgetDTO budget = new GetBudgetDTO();
		
		Optional<Event> e = eventRepo.findById(eventId);
		if(e.isEmpty())
			throw new EntityNotFoundException("Nonexistent event");
		
		UserPrincipal up = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    if(up == null)
	    	throw new AuthenticationCredentialsNotFoundException("User not logged in");
	    if(e.get().getOrganizer().getId() != up.getUser().getId())
	    	throw new AccessDeniedException("Wrong user");
		
		Set<OfferCategory> recommendedCategories = 
			    new HashSet<>(e.get().getEventType().getRecommendedCategories());
		
		List<OfferReservation> offerReservations = offerReservationRepo.findByEvent(e.get());
		
		List<Offer> reservedOffers = offerReservations.stream().map(reservation -> reservation.getOffer()).toList();

		Map<Integer, List<Offer>> offersByCategory = reservedOffers.stream()
				.collect(Collectors.groupingBy(Offer::getCategoryId, Collectors.toList()));
		
		Set<BudgetItem> budgetItems = budgetItemRepo.findByEventId(eventId);
		budgetItems.addAll(offersByCategory.keySet().stream().map(offerId -> getOrCreateItem(eventId, offerId)).toList());
		
		Set<OfferCategory> takenCategories = budgetItems.stream().map(item -> item.getBudgetCategory()).collect(Collectors.toSet());
		
		//Don't recommend already taken categories
		recommendedCategories.removeAll(takenCategories);
		
		budget.eventID = eventId;
		budget.eventName = e.get().getName();
		
		budget.recommendedOfferTypes = recommendedCategories.stream().map(cat -> new MinimalOfferCategoryDTO(cat)).toList();
		budget.takenItems = budgetItems.stream().map(item -> new BudgetItemDTO(item, offersByCategory.getOrDefault(item.getBudgetCategory().getId(), new ArrayList<Offer>()))).toList();
		
		budget.takenOffers = reservedOffers.stream().map(off -> new BudgetOfferDTO(off)).toList();
		
		budget.maxBudget = budgetItems.stream().mapToDouble(bi -> bi.getBudget()).sum();
		budget.usedBudget = budget.takenOffers.stream().mapToDouble(o -> o.cost).sum();
		
		return budget;
	}
}
