package rs.ac.uns.ftn.asd.Projekatsiit2024.eventTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.CreateEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventActivity.CreateEventActivityDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.Role;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.auth.RoleRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventTypeRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test-spring")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EventControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
    private AuthentifiedUserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private EventTypeRepository eventTypeRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;
    private EventType activeEventType;
	
    @BeforeEach
    void setup() throws Exception {
    	Role organizerRole = new Role();
        organizerRole.setName("ORGANIZER_ROLE");
        roleRepository.save(organizerRole);
        Organizer organizer = new Organizer();
        organizer.setEmail("organizer@test.com");
        organizer.setPassword(passwordEncoder.encode("password123"));
        organizer.setIsVerified(true);
        organizer.setRole(organizerRole);
        organizer.setIsDeleted(false);
        userRepository.save(organizer);
        
        activeEventType = new EventType();
        activeEventType.setName("Conference");
        activeEventType.setDescription("Test Event Type");
        activeEventType.setIsActive(true);
        eventTypeRepository.save(activeEventType);
        
        String loginJson = """
                {
                  "email": "organizer@test.com",
                  "password": "password123"
                }
                """;

	    MvcResult result = mockMvc.perform(post("/api/auth/login")
	                    .contentType(MediaType.APPLICATION_JSON)
	                    .content(loginJson))
	            .andExpect(status().isOk())
	            .andReturn();
	
	    String responseBody = result.getResponse().getContentAsString();
	    jwtToken = objectMapper.readTree(responseBody).get("accessToken").asText();
    }

    @Test
    void createEvent_ShouldReturnCreatedEvent() throws Exception {
    	CreateEventActivityDTO activityDTO = new CreateEventActivityDTO();
        activityDTO.setName("Opening Ceremony");
        activityDTO.setDescription("Welcome speech and introduction");
        activityDTO.setStartingTime(LocalDateTime.now().plusDays(1).plusHours(1));
        activityDTO.setEndingTime(LocalDateTime.now().plusDays(1).plusHours(2));
        activityDTO.setLocation("Main Hall");

        CreateEventDTO eventDTO = new CreateEventDTO();
        eventDTO.setName("My Test Event");
        eventDTO.setDescription("Description here");
        eventDTO.setNumOfAttendees(100);
        eventDTO.setIsPrivate(false);
        eventDTO.setPlace("Belgrade, Serbia");
        eventDTO.setDateOfEvent(LocalDateTime.now().plusDays(1));
        eventDTO.setEndOfEvent(LocalDateTime.now().plusDays(1).plusHours(4));
        eventDTO.setEventTypeId(1);
        eventDTO.setLatitude(44.817);
        eventDTO.setLongitude(20.456);
        eventDTO.getEventActivities().add(activityDTO);

        String requestJson = objectMapper.writeValueAsString(eventDTO);

        mockMvc.perform(post("/api/events")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("My Test Event"))
                .andExpect(jsonPath("$.place").value("Belgrade, Serbia"));
    }

    
    @Test
    void createEvent_ShouldReturnUnauthorized_WithoutToken() throws Exception {
        CreateEventDTO eventDTO = buildValidEventDTO();
        String requestJson = objectMapper.writeValueAsString(eventDTO);

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    void createEvent_ShouldReturnBadRequest_InvalidEventType() throws Exception {
        CreateEventDTO eventDTO = buildValidEventDTO();
        eventDTO.setEventTypeId(999);
        String requestJson = objectMapper.writeValueAsString(eventDTO);

        mockMvc.perform(post("/api/events")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No such event type can be used to create an event."));
    }
    
    @Test
    void createEvent_ShouldReturnBadRequest_InvalidDate() throws Exception {
        CreateEventDTO eventDTO = buildValidEventDTO();
        eventDTO.setDateOfEvent(LocalDateTime.now().minusDays(1));
        eventDTO.setEndOfEvent(LocalDateTime.now().minusDays(1).plusHours(2));
        String requestJson = objectMapper.writeValueAsString(eventDTO);

        mockMvc.perform(post("/api/events")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }
    
    
    private CreateEventDTO buildValidEventDTO() {
        CreateEventActivityDTO activityDTO = new CreateEventActivityDTO();
        activityDTO.setName("Opening Ceremony");
        activityDTO.setDescription("Welcome speech and introduction");
        activityDTO.setStartingTime(LocalDateTime.now().plusDays(1));
        activityDTO.setEndingTime(LocalDateTime.now().plusDays(1).plusHours(2));
        activityDTO.setLocation("Main Hall");

        CreateEventDTO eventDTO = new CreateEventDTO();
        eventDTO.setName("My Test Event");
        eventDTO.setDescription("Description here");
        eventDTO.setNumOfAttendees(100);
        eventDTO.setIsPrivate(false);
        eventDTO.setPlace("Belgrade, Serbia");
        eventDTO.setDateOfEvent(LocalDateTime.now().plusDays(1));
        eventDTO.setEndOfEvent(LocalDateTime.now().plusDays(1).plusHours(4));
        eventDTO.setEventTypeId(activeEventType.getId());
        eventDTO.setLatitude(44.817);
        eventDTO.setLongitude(20.456);
        eventDTO.getEventActivities().add(activityDTO);

        return eventDTO;
    }
    
    @Test
    void getEventDetailsPDF_ShouldReturnPDF() throws Exception {
        CreateEventDTO eventDTO = buildValidEventDTO();
        String requestJson = objectMapper.writeValueAsString(eventDTO);

        MvcResult createResult = mockMvc.perform(post("/api/events")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        Integer eventId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("id").asInt();

        mockMvc.perform(get("/api/events/{eventId}/reports/details", eventId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/pdf"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=event-details.pdf"));
    }
    
    @Test
    void getEventDetailsPDF_ShouldContainCorrectEventData() throws Exception {
        // 1. Create an event
        CreateEventDTO eventDTO = buildValidEventDTO();
        String requestJson = objectMapper.writeValueAsString(eventDTO);

        MvcResult createResult = mockMvc.perform(post("/api/events")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        Integer eventId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("id").asInt();

        // 2. Request PDF
        MvcResult pdfResult = mockMvc.perform(get("/api/events/{eventId}/reports/details", eventId)
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/pdf"))
                .andReturn();

        byte[] pdfBytes = pdfResult.getResponse().getContentAsByteArray();

        // 3. Read PDF content
        try (PDDocument document = PDDocument.load(pdfBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String pdfText = stripper.getText(document);

            String normalizedPdfText = pdfText.replaceAll("\\s+", " ").trim();
            assertTrue(normalizedPdfText.contains(eventDTO.getName()), "PDF should contain event name");
            assertTrue(normalizedPdfText.contains(eventDTO.getDescription()), "PDF should contain description");
            assertTrue(normalizedPdfText.contains(eventDTO.getPlace()), "PDF should contain location");
            assertTrue(normalizedPdfText.contains("Conference"), "PDF should contain event type name");
            assertTrue(normalizedPdfText.contains("100"), "PDF should contain number of attendees");
            assertTrue(normalizedPdfText.contains("public"), "PDF should contain privacy type");
            assertTrue(normalizedPdfText.contains("Opening Ceremony"), "PDF should contain activity name");
            assertTrue(normalizedPdfText.contains("Welcome speech and introduction"), "PDF should contain activity description");
        }
    }
}
