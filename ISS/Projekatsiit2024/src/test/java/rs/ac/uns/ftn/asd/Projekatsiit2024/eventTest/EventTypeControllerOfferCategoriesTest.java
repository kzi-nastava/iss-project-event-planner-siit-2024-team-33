package rs.ac.uns.ftn.asd.Projekatsiit2024.eventTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventTypeRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.offer.OfferCategoryRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test-spring")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EventTypeControllerOfferCategoriesTest {
	
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private OfferCategoryRepository offerCategoryRepository;

    private EventType activeEventType;
    private OfferCategory enabledAcceptedCategory;
    private OfferCategory disabledCategory;
    private OfferCategory notAcceptedCategory;

    @BeforeEach
    void setup() {
        enabledAcceptedCategory = new OfferCategory();
        enabledAcceptedCategory.setName("Category 1");
        enabledAcceptedCategory.setDescription("Description 1");
        enabledAcceptedCategory.setIsEnabled(true);
        enabledAcceptedCategory.setIsAccepted(true);
        enabledAcceptedCategory.setOfferType(OfferType.SERVICE);
        offerCategoryRepository.save(enabledAcceptedCategory);

        disabledCategory = new OfferCategory();
        disabledCategory.setName("Category 2");
        disabledCategory.setDescription("Description 2");
        disabledCategory.setIsEnabled(false);
        enabledAcceptedCategory.setIsAccepted(true);
        disabledCategory.setOfferType(OfferType.PRODUCT);
        offerCategoryRepository.save(disabledCategory);

        notAcceptedCategory = new OfferCategory();
        notAcceptedCategory.setName("Category 3");
        notAcceptedCategory.setDescription("Description 3");
        notAcceptedCategory.setIsEnabled(true);
        enabledAcceptedCategory.setIsAccepted(false);
        notAcceptedCategory.setOfferType(OfferType.SERVICE);
        offerCategoryRepository.save(notAcceptedCategory);
        
        activeEventType = new EventType();
        activeEventType.setName("Conference");
        activeEventType.setIsActive(true);
        activeEventType.setRecommendedCategories(
                Set.of(enabledAcceptedCategory, disabledCategory, notAcceptedCategory)
        );
        eventTypeRepository.save(activeEventType);
        
        activeEventType = eventTypeRepository.findById(activeEventType.getId()).get();
    }

    @Test
    void getRecommendedCategories_ShouldReturnOnlyEnabledAndAcceptedCategories() throws Exception {
    	mockMvc.perform(get("/api/eventTypes/" + activeEventType.getId() + "/offerCategories")
                .contentType(MediaType.APPLICATION_JSON))
    		.andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].name").value("Category 1"))
            .andExpect(jsonPath("$[0].description").value("Description 1"))
            .andExpect(jsonPath("$[0].isEnabled").value(true))
            .andExpect(jsonPath("$[0].type").value("SERVICE"));
    }

    @Test
    void getRecommendedCategories_ShouldReturn404ForInactiveEventType() throws Exception {
        EventType inactiveEventType = new EventType();
        inactiveEventType.setName("Inactive");
        inactiveEventType.setIsActive(false);
        eventTypeRepository.save(inactiveEventType);

        mockMvc.perform(get("/api/eventTypes/" + inactiveEventType.getId() + "/offerCategories")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    @Test
    void getRecommendedCategories_ShouldReturn404ForNonExistingEventType() throws Exception {
        mockMvc.perform(get("/api/eventTypes/9999/offerCategories")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }
}
