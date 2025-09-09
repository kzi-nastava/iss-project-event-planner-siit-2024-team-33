package rs.ac.uns.ftn.asd.Projekatsiit2024.eventTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventTypeValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventTypeRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.event.EventTypeService;

@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class EventTypeServiceTest {
	
	@InjectMocks
	private EventTypeService eventTypeService;
	
	@Mock
	private EventTypeRepository eventTypeRepository;
	
	private EventType activeType1;
    private EventType activeType2;
    private EventType activeEventTypeWithCategories;
    private OfferCategory cat1;
    private OfferCategory cat2;
    private OfferCategory cat3;
	
	@BeforeEach
	void setup() {
		activeType1 = new EventType();
        activeType1.setId(1);
        activeType1.setName("Conference");
        activeType1.setIsActive(true);

        activeType2 = new EventType();
        activeType2.setId(2);
        activeType2.setName("Workshop");
        activeType2.setIsActive(true);
        
        activeEventTypeWithCategories = new EventType();
        activeEventTypeWithCategories.setId(10);
        activeEventTypeWithCategories.setName("Conference");
        activeEventTypeWithCategories.setIsActive(true);

        cat1 = new OfferCategory("Cat1", "Desc1", true, true);
        cat1.setId(1);
        cat2 = new OfferCategory("Cat2", "Desc2", true, false);
        cat2.setId(2);
        cat3 = new OfferCategory("Cat3", "Desc3", false, true);
        cat3.setId(3);

        Set<OfferCategory> categories = Set.of(cat1, cat2, cat3);
        activeEventTypeWithCategories.setRecommendedCategories(categories);
	}
	
	// ------------------------ getActiveEventTypes tests ------------------------

    @Test
    @Order(1)
    void getActiveEventTypes_ShouldReturnOnlyActive() {
        when(eventTypeRepository.getActiveEventTypes()).thenReturn(List.of(activeType1, activeType2));

        List<EventType> result = eventTypeService.getActiveEventTypes();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(activeType1));
        assertTrue(result.contains(activeType2));

        verify(eventTypeRepository, times(1)).getActiveEventTypes();
    }

    @Test
    @Order(2)
    void getActiveEventTypes_ShouldReturnEmptyListWhenNoneExist() {
        when(eventTypeRepository.getActiveEventTypes()).thenReturn(List.of());

        List<EventType> result = eventTypeService.getActiveEventTypes();

        assertTrue(result.isEmpty());
        verify(eventTypeRepository, times(1)).getActiveEventTypes();
    }

    // ------------------------ getRecommendedByEventType tests ------------------------

    @Test
    @Order(3)
    void getRecommendedByEventType_ShouldReturnOnlyEnabledAndAccepted() {
        when(eventTypeRepository.findById(10)).thenReturn(Optional.of(activeEventTypeWithCategories));

        Set<OfferCategory> result = eventTypeService.getRecommendedByEventType(10);

        assertEquals(1, result.size());
        assertTrue(result.contains(cat1));
        assertFalse(result.contains(cat2));
        assertFalse(result.contains(cat3));

        verify(eventTypeRepository, times(1)).findById(10);
    }

    @Test
    @Order(4)
    void getRecommendedByEventType_ShouldThrowException_WhenEventTypeNotFound() {
        when(eventTypeRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EventTypeValidationException.class,
                () -> eventTypeService.getRecommendedByEventType(999));

        verify(eventTypeRepository, times(1)).findById(999);
    }

    @Test
    @Order(5)
    void getRecommendedByEventType_ShouldThrowException_WhenEventTypeInactive() {
        activeEventTypeWithCategories.setIsActive(false);
        when(eventTypeRepository.findById(10)).thenReturn(Optional.of(activeEventTypeWithCategories));

        assertThrows(EventTypeValidationException.class,
                () -> eventTypeService.getRecommendedByEventType(10));

        verify(eventTypeRepository, times(1)).findById(10);
    }
    
    @Test
    @Order(6)
    void getRecommendedByEventType_ShouldReturnEmpty_WhenNoCategoryEnabledAndAccepted() {
        OfferCategory c1 = new OfferCategory("C1", "Desc1", false, true); // not accepted
        OfferCategory c2 = new OfferCategory("C2", "Desc2", true, false); // not enabled
        c1.setId(101);
        c2.setId(102);
        
        EventType eventType = new EventType();
        eventType.setId(20);
        eventType.setIsActive(true);
        eventType.setRecommendedCategories(Set.of(c1, c2));

        when(eventTypeRepository.findById(20)).thenReturn(Optional.of(eventType));

        Set<OfferCategory> result = eventTypeService.getRecommendedByEventType(20);

        assertTrue(result.isEmpty());
        verify(eventTypeRepository, times(1)).findById(20);
    }
    
    @Test
    @Order(7)
    void getRecommendedByEventType_ShouldReturnOnlyEnabledAccepted_WhenMixedCategories() {
        OfferCategory c1 = new OfferCategory("C1", "Desc1", true, true);  // valid
        OfferCategory c2 = new OfferCategory("C2", "Desc2", true, false); // invalid
        OfferCategory c3 = new OfferCategory("C3", "Desc3", false, true); // invalid
        OfferCategory c4 = new OfferCategory("C4", "Desc4", true, true);  // valid
        c1.setId(201);
        c2.setId(202);
        c3.setId(203);
        c4.setId(204);
        
        
        EventType eventType = new EventType();
        eventType.setId(30);
        eventType.setIsActive(true);
        eventType.setRecommendedCategories(Set.of(c1, c2, c3, c4));

        when(eventTypeRepository.findById(30)).thenReturn(Optional.of(eventType));

        Set<OfferCategory> result = eventTypeService.getRecommendedByEventType(30);

        assertEquals(2, result.size());
        assertTrue(result.contains(c1));
        assertTrue(result.contains(c4));
        assertFalse(result.contains(c2));
        assertFalse(result.contains(c3));

        verify(eventTypeRepository, times(1)).findById(30);
    }
}
