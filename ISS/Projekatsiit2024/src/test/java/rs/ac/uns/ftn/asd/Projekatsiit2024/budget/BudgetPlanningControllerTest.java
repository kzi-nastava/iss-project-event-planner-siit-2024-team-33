package rs.ac.uns.ftn.asd.Projekatsiit2024.budget;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.auth.AuthenticationRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.auth.UserToken;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget.PostBudgetItemDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget.PutBudgetItemDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.productPurchase.PostProductPurchaseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferCategoryRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;


@SpringBootTest // Boots up the full context
@AutoConfigureMockMvc
@ActiveProfiles("test") // Use test DB (test_data.sql preloaded)
public class BudgetPlanningControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private OfferCategoryRepository categoryRepo;

    @Autowired
    private ObjectMapper objectMapper;
    
    private String loginAndGetToken(String email, String password) throws Exception {
        String loginJson = objectMapper.writeValueAsString(
                new AuthenticationRequest(email, password)
        );

        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserToken token = objectMapper.readValue(response, UserToken.class);
        return token.getAccessToken();
    }

    @Test
    @Transactional
    void getBudgetInfo_successful() throws Exception {
        String token = loginAndGetToken("organizer2@example.com", "pass1234");

        // Optional: create a budget item first
        PostBudgetItemDTO dto = new PostBudgetItemDTO();
        dto.offerCategoryID = 1;
        dto.maxBudget = 500.0;

        mockMvc.perform(post("/api/events/{eventID}/budget", 1)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // Now retrieve the budget info
        mockMvc.perform(get("/api/events/{eventID}/budget", 1)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventID").value(1))
                .andExpect(jsonPath("$.eventName").exists())
                .andExpect(jsonPath("$.maxBudget").value(500.0))
                .andExpect(jsonPath("$.usedBudget").value(0.0))
                .andExpect(jsonPath("$.takenItems").isArray())
                .andExpect(jsonPath("$.recommendedOfferTypes").isArray())
                .andExpect(jsonPath("$.takenOffers").isArray());
    }

    @Test
    void getBudgetInfo_notLoggedIn_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/events/{eventID}/budget", 1))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    void getBudgetInfo_nonExistentEvent_returnsNotFound() throws Exception {
        String token = loginAndGetToken("organizer2@example.com", "pass1234");

        mockMvc.perform(get("/api/events/{eventID}/budget", 999)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Nonexistent event")));
    }

    @Test
    @Transactional
    void getBudgetInfo_wrongOrganizer_returnsForbidden() throws Exception {
        String token = loginAndGetToken("organizer@example.com", "pass1234");

        mockMvc.perform(get("/api/events/{eventID}/budget", 1)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void getBudgetInfo_withReservedOffers_correctBudgetCalculation() throws Exception {
        String token = loginAndGetToken("organizer2@example.com", "pass1234");

        // Step 1: Create a budget item
        PostBudgetItemDTO dto = new PostBudgetItemDTO();
        dto.offerCategoryID = 1;
        dto.maxBudget = 500.0;

        mockMvc.perform(post("/api/events/{eventID}/budget", 1)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        //buy product
        PostProductPurchaseDTO buyDTO = new PostProductPurchaseDTO();
        buyDTO.eventId = 1;
        mockMvc.perform(post("/api/products/{id}/reservations", 1)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buyDTO)))
        		.andExpect(status().isOk());

        
        mockMvc.perform(get("/api/events/{eventID}/budget", 1)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.takenOffers").isArray())
                .andExpect(jsonPath("$.takenItems[?(@.offerCategoryID == 1)]").exists())
                .andExpect(jsonPath("$.maxBudget").value(500.0))
                .andExpect(jsonPath("$.usedBudget").value(59.99 - 5.0));
    }

    @Test
    @Transactional
    void addBudgetItem_asOrganizer_createsSuccessfully() throws Exception {
        String token = loginAndGetToken("organizer2@example.com", "pass1234");

        PostBudgetItemDTO dto = new PostBudgetItemDTO();
        dto.offerCategoryID = 1;
        dto.maxBudget = 3000.0;

        mockMvc.perform(post("/api/events/{eventID}/budget", 1)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.offerCategoryID").value(1))
                .andExpect(jsonPath("$.maxBudget").value(3000.0));
    }

    @Test
    @Transactional
    void addBudgetItem_existingItem_returnsConflict() throws Exception {
        String token = loginAndGetToken("organizer2@example.com", "pass1234");

        PostBudgetItemDTO dto = new PostBudgetItemDTO();
        dto.offerCategoryID = 1;
        dto.maxBudget = 3000.0;

        // First creation
        mockMvc.perform(post("/api/events/{eventID}/budget", 1)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // Second creation should conflict
        mockMvc.perform(post("/api/events/{eventID}/budget", 1)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    void addBudgetItem_notLoggedIn_returnsUnauthorized() throws Exception {
        PostBudgetItemDTO dto = new PostBudgetItemDTO();
        dto.offerCategoryID = 1;
        dto.maxBudget = 3000.0;

        mockMvc.perform(post("/api/events/{eventID}/budget", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void addBudgetItem_wrongRole_returnsForbidden() throws Exception {
        // Providers cannot add budget items
        String token = loginAndGetToken("provider@example.com", "pass1234");

        PostBudgetItemDTO dto = new PostBudgetItemDTO();
        dto.offerCategoryID = 1;
        dto.maxBudget = 3000.0;

        mockMvc.perform(post("/api/events/{eventID}/budget", 1)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    void addBudgetItem_invalidEvent_returnsNotFound() throws Exception {
        String token = loginAndGetToken("organizer2@example.com", "pass1234");

        PostBudgetItemDTO dto = new PostBudgetItemDTO();
        dto.offerCategoryID = 1;
        dto.maxBudget = 3000.0;

        mockMvc.perform(post("/api/events/{eventID}/budget", 999) // non-existent event
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void editBudgetItem_asOrganizer_successful() throws Exception {
        String token = loginAndGetToken("organizer2@example.com", "pass1234");

        PostBudgetItemDTO postDTO = new PostBudgetItemDTO();
        postDTO.offerCategoryID = 1; // Music 
        postDTO.maxBudget = 3000.0;
        
        mockMvc.perform(post("/api/events/{eventID}/budget", 1)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDTO)))
        		.andExpect(status().isOk());
        
        PutBudgetItemDTO dto = new PutBudgetItemDTO();
        dto.maxBudget = 5000.0;

        mockMvc.perform(put("/api/events/{eventID}/budget/{categoryID}", 1, 1)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maxBudget").value(5000.0));
    }

    @Test
    @Transactional
    void editBudgetItem_negativeBudget_returnsBadRequest() throws Exception {
        String token = loginAndGetToken("organizer2@example.com", "pass1234");

        PostBudgetItemDTO postDTO = new PostBudgetItemDTO();
        postDTO.offerCategoryID = 1; // Music 
        postDTO.maxBudget = 3000.0;
        
        mockMvc.perform(post("/api/events/{eventID}/budget", 1)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDTO)))
        		.andExpect(status().isOk());
        
        PutBudgetItemDTO dto = new PutBudgetItemDTO();
        dto.maxBudget = -100.0;

        mockMvc.perform(put("/api/events/{eventID}/budget/{categoryID}", 1, 1)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void editBudgetItem_lowerThanUsedBudget_returnsBadRequest() throws Exception {
        String token = loginAndGetToken("organizer2@example.com", "pass1234");

        
        //create item
        PostBudgetItemDTO postDTO = new PostBudgetItemDTO();
        postDTO.offerCategoryID = 1; // Music 
        postDTO.maxBudget = 3000.0;
        
        mockMvc.perform(post("/api/events/{eventID}/budget", 1)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDTO)))
        		.andExpect(status().isOk());
        
        //buy product
        PostProductPurchaseDTO buyDTO = new PostProductPurchaseDTO();
        buyDTO.eventId = 1;
        mockMvc.perform(post("/api/products/{id}/reservations", 1)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buyDTO)))
        		.andExpect(status().isOk());
        
        //edit item
        PutBudgetItemDTO dto = new PutBudgetItemDTO();
        dto.maxBudget = 0.0;

        mockMvc.perform(put("/api/events/{eventID}/budget/{categoryID}", 1, 1)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Budget can't be lower than the total spend money")));
    }

    @Test
    void editBudgetItem_nonExistentItem_returnsNotFound() throws Exception {
        String token = loginAndGetToken("organizer2@example.com", "pass1234");

        PutBudgetItemDTO dto = new PutBudgetItemDTO();
        dto.maxBudget = 500.0;

        mockMvc.perform(put("/api/events/{eventID}/budget/{categoryID}", 999, 999)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void editBudgetItem_notLoggedIn_returnsUnauthorized() throws Exception {
        PutBudgetItemDTO dto = new PutBudgetItemDTO();
        dto.maxBudget = 500.0;

        mockMvc.perform(put("/api/events/{eventID}/budget/{categoryID}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    void editBudgetItem_wrongUser_returnsForbidden() throws Exception {
    	String token = loginAndGetToken("organizer2@example.com", "pass1234");

        
        //create item
        PostBudgetItemDTO postDTO = new PostBudgetItemDTO();
        postDTO.offerCategoryID = 1; // Music 
        postDTO.maxBudget = 3000.0;
        
        mockMvc.perform(post("/api/events/{eventID}/budget", 1)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDTO)))
        		.andExpect(status().isOk());
    	
        token = loginAndGetToken("organizer@example.com", "pass1234");

        PutBudgetItemDTO dto = new PutBudgetItemDTO();
        dto.maxBudget = 500.0;

        mockMvc.perform(put("/api/events/{eventID}/budget/{categoryID}", 1, 1)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void deleteBudgetItem_successful() throws Exception {
    	String token = loginAndGetToken("organizer2@example.com", "pass1234");

        // First, create a budget item to delete
        PostBudgetItemDTO dto = new PostBudgetItemDTO();
        dto.offerCategoryID = 1;
        dto.maxBudget = 1000.0;

        mockMvc.perform(post("/api/events/{eventID}/budget", 1)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // Now delete it
        mockMvc.perform(delete("/api/events/{eventID}/budget/{categoryID}", 1, 1)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBudgetItem_notLoggedIn_returnsUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/events/{eventID}/budget/{categoryID}", 1, 1))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    void deleteBudgetItem_nonExistent_returnsNotFound() throws Exception {
    	String token = loginAndGetToken("organizer2@example.com", "pass1234");

        mockMvc.perform(delete("/api/events/{eventID}/budget/{categoryID}", 999, 999)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Nonexistent event")));
    }

    @Test
    @Transactional
    void deleteBudgetItem_wrongOrganizer_returnsForbidden() throws Exception {
    	String token = loginAndGetToken("organizer@example.com", "pass1234");

        // Assuming event ID 1 is owned by a different organizer
        mockMvc.perform(delete("/api/events/{eventID}/budget/{categoryID}", 1, 1)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void deleteBudgetItem_hasReservations_returnsConflict() throws Exception {
    	String token = loginAndGetToken("organizer2@example.com", "pass1234");

        // Setup a budget item with a reservation
        PostBudgetItemDTO dto = new PostBudgetItemDTO();
        dto.offerCategoryID = 1;
        dto.maxBudget = 5000.0;

        mockMvc.perform(post("/api/events/{eventID}/budget", 1)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        //buy product
        PostProductPurchaseDTO buyDTO = new PostProductPurchaseDTO();
        buyDTO.eventId = 1;
        mockMvc.perform(post("/api/products/{id}/reservations", 1)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buyDTO)))
        		.andExpect(status().isOk());
        
        mockMvc.perform(delete("/api/events/{eventID}/budget/{categoryID}", 1, 1)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isConflict())
                .andExpect(content().string(Matchers.containsString("Can't delete the budget item due to reservations")));
    }

    @Test
    @Transactional
    void getBudget_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/api/events/{eventID}/budget", 1))
                .andExpect(status().isUnauthorized());
    }
}
