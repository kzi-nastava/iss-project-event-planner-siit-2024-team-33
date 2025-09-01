package rs.ac.uns.ftn.asd.Projekatsiit2024.budget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.BudgetItem;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.BudgetItemRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferCategoryRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventTypeRepository;

@DataJpaTest
@ActiveProfiles("test")
public class BudgetPlanningRepoTest {
	@Autowired
    private BudgetItemRepository budgetItemRepository;

    @Autowired
    private EventRepository eventRepo;
    
    @Autowired
    private EventTypeRepository eventTypeRepo;

    @Autowired
    private OfferCategoryRepository categoryRepo;
    
    private Event event1;
    private OfferCategory category1;
    private OfferCategory category2;

    @BeforeEach
    void setUp() {
        event1 = new Event();
        event1.setName("Test Event");
        
        EventType et = eventTypeRepo.findById(1).get();
        event1.setEventType(et);
        eventRepo.save(event1);

        category1 = new OfferCategory();
        category1.setName("CateringTest");
        categoryRepo.save(category1);

        category2 = new OfferCategory();
        category2.setName("MusicTest");
        categoryRepo.save(category2);

        BudgetItem item1 = new BudgetItem();
        item1.setBudget(1000.0);
        item1.setEvent(event1);
        item1.setBudgetCategory(category1);
        budgetItemRepository.save(item1);

        BudgetItem item2 = new BudgetItem();
        item2.setBudget(500.0);
        item2.setEvent(event1);
        item2.setBudgetCategory(category2);
        budgetItemRepository.save(item2);

        eventRepo.flush();
        categoryRepo.flush();
        budgetItemRepository.flush();
    }

    @Test
    @DisplayName("Should find BudgetItem")
    void testFindByEventAndCategory() {
        BudgetItem result = budgetItemRepository.findByEventAndCategory(
                event1.getId(),
                category1.getId()
        );

        assertNotNull(result);
        assertThat(result.getBudget()).isEqualTo(1000.0);
        assertThat(result.getBudgetCategory().getName()).isEqualTo("CateringTest");
    }

    @Test
    @DisplayName("Should return all BudgetItems for given eventId")
    void testFindByEventId() {
        Set<BudgetItem> results = budgetItemRepository.findByEventId(event1.getId());

        assertThat(results).hasSize(2);
        
        List<Double> prices = results.stream().map(bi -> bi.getBudget()).toList();
        assertTrue(prices.contains(500.0));
        assertTrue(prices.contains(1000.0));
    }

    @Test
    @DisplayName("Should return null since no budgetItem matches category")
    void testFindByEventAndCategory_NoCategory() {
        BudgetItem result = budgetItemRepository.findByEventAndCategory(
                event1.getId(),
                999 // doesn't exist in database
        );

        assertThat(result).isNull();
    }
    
    @Test
    @DisplayName("Should return null since no budgetItem matches event")
    void testFindByEventAndCategory_NoEvent() {
        BudgetItem result = budgetItemRepository.findByEventAndCategory(
                999,
                category1.getId()
        );

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should find nothing when no BudgetItems exist for event")
    void testFindByEventId_NoResult() {
        Set<BudgetItem> results = budgetItemRepository.findByEventId(999);

        assertThat(results).isEmpty();
    }

}
