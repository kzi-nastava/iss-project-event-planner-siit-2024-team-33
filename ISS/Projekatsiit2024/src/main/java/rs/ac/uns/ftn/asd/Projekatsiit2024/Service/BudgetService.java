package rs.ac.uns.ftn.asd.Projekatsiit2024.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import ch.qos.logback.core.spi.ConfigurationEvent.EventType;
import jakarta.persistence.EntityNotFoundException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.BudgetItem;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.BudgetItemRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.OfferCategoryRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.OfferReservationRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget.BudgetItemDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget.BudgetOfferDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget.GetBudgetDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offerCategory.MinimalOfferCategoryDTO;

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
	
	private Double getUsedBudget(BudgetItem bi) {
		List<Offer> offers = offerReservationRepo.findByBudgetItem(bi);
		return offers.stream().mapToDouble(offer -> offer.getPrice() - offer.getDiscount()).sum();
	}
	
	public BudgetItem createItem(Integer eventId, Integer categoryId, Double maxBudget) {
		//TODO: User validation
		BudgetItem bi = budgetItemRepo.findByEventAndCategory(eventId, categoryId);
		if (bi != null)
			throw new DataIntegrityViolationException("Budget item already exists");
		if(maxBudget < 0)
			throw new IllegalArgumentException("Budget can't be negative");
		//TODO: USER VALIDATION AND MORE
		
		Optional<Event> e = eventRepo.findById(eventId);
		if(e.isEmpty())
			throw new EntityNotFoundException("Nonexistent event");
		
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
		//TODO: User validation
		BudgetItem bi = budgetItemRepo.findByEventAndCategory(eventId, categoryId);
		if (bi == null)
			throw new EntityNotFoundException();
		if(maxBudget < 0)
			throw new IllegalArgumentException("Budget can't be negative");
		if(maxBudget < getUsedBudget(bi))
			throw new IllegalArgumentException("Budget can't be lower than the total spend money");
		
		bi.setBudget(maxBudget);
		bi = budgetItemRepo.saveAndFlush(bi);
		return bi;
	}
	
	public void deleteItem(Integer eventId, Integer categoryId) {
		//TODO: User validation
		
		Optional<Event> e = eventRepo.findById(eventId);
		if(e.isEmpty())
			throw new EntityNotFoundException("Nonexistent event");
		
		BudgetItem bi = budgetItemRepo.findByEventAndCategory(eventId, categoryId);
		if(bi == null)
			throw new EntityNotFoundException("Nonexistent budget item");
		
		//Whether the selected budget item contains any reservations
		Boolean hasReservations = e.get().getReservations().stream()
				.map(reservation -> reservation.getOffer()).anyMatch(offer -> offer.getCategoryId() == categoryId);

		if(hasReservations)
			throw new DataIntegrityViolationException("Can't delete the budget item due to reservations for that item existing");
		
		budgetItemRepo.delete(bi);
		budgetItemRepo.flush();
	}
	
	public GetBudgetDTO getBudget(Integer eventId){
		//TODO: User validation
		GetBudgetDTO budget = new GetBudgetDTO();
		
		Optional<Event> e = eventRepo.findById(eventId);
		if(e.isEmpty())
			throw new EntityNotFoundException("Nonexistent event");
		
		Set<OfferCategory> recommendedCategories = e.get().getEventTypes().stream()
				.flatMap(type -> type.getRecommendedCategories().stream()).collect(Collectors.toSet());
		
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
		
		return budget;
	}
}
