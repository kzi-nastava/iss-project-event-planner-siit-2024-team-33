package rs.ac.uns.ftn.asd.Projekatsiit2024.offerReservationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.auth.AuthenticationRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.auth.UserToken;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation.PostServiceReservationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.service.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.offer.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;

import jakarta.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ServiceReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AuthentifiedUserRepository userRepository;

    @Autowired
    private OfferReservationRepository offerReservationRepo;
    
    private String loginAndGetToken(String email, String password) throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(email, password);
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserToken token = objectMapper.readValue(response, UserToken.class);
        return token.getAccessToken();
    }

    @Test
    @Transactional
    void reserveService_unsuccessful() throws Exception {
        String token = loginAndGetToken("organizer2@example.com", "pass1234");

        Organizer organizer = (Organizer) userRepository.findByEmail("organizer2@example.com");
        if (organizer == null) throw new IllegalStateException("Organizer not found in DB!");

        Event event = eventRepository.findById(2)
                .orElseThrow(() -> new IllegalStateException("Event not found"));

        Service service = serviceRepository.findById(12)
                .orElseThrow(() -> new IllegalStateException("Service not found"));

        PostServiceReservationDTO dto = new PostServiceReservationDTO();
        dto.setEventId(event.getId());

        LocalDate eventDate = event.getDateOfEvent().toLocalDate();
        LocalTime eventStartTime = event.getDateOfEvent().toLocalTime();
        LocalTime eventEndTime = event.getEndOfEvent().toLocalTime();

        dto.setReservationDate(eventDate.toString());
        dto.setStartTime(eventStartTime.minusHours(1).toString()); 
        dto.setEndTime(eventEndTime.plusHours(1).toString());
        
        mockMvc.perform(post("/api/services/{serviceID}/reservations", service.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("BAD_TIME_RANGE"));
    }
    
    @Test
    @Transactional
    void reserveService_timeCollisionWithAnotherEvent() throws Exception {
        String token = loginAndGetToken("organizer2@example.com", "pass1234");

        Event event = eventRepository.findById(2)
                .orElseThrow(() -> new IllegalStateException("Event not found"));
        Service service = serviceRepository.findById(12)
                .orElseThrow(() -> new IllegalStateException("Service not found"));

        OfferReservation offerRes = new OfferReservation();
        offerRes.setDateOfReservation(event.getDateOfEvent().toLocalDate());
        offerRes.setStartTime(LocalDateTime.of(event.getDateOfEvent().toLocalDate(), LocalTime.of(12, 0)));
        offerRes.setEndTime(LocalDateTime.of(event.getDateOfEvent().toLocalDate(), LocalTime.of(14, 0)));
        offerRes.setEvent(event);
        offerRes.setOffer(service);
        offerRes.setId(100);
        offerReservationRepo.save(offerRes);
        
        PostServiceReservationDTO dto = new PostServiceReservationDTO();
        dto.setEventId(event.getId());
        dto.setReservationDate(event.getDateOfEvent().toLocalDate().toString());
        dto.setStartTime("12:30");
        dto.setEndTime("13:30");

        mockMvc.perform(post("/api/services/{serviceID}/reservations", service.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("TIME_COLLISION_WITH_ANOTHER_EVENT"));
    }

    @Test
    void reserveService_notLoggedIn_returnsUnauthorized() throws Exception {
        PostServiceReservationDTO dto = new PostServiceReservationDTO();
        dto.setEventId(1);
        dto.setReservationDate("2025-09-09");
        dto.setStartTime("10:00");
        dto.setEndTime("12:00");

        mockMvc.perform(post("/api/services/{serviceID}/reservations", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    void reserveService_nonExistentService_returnsNotFound() throws Exception {
        String token = loginAndGetToken("organizer2@example.com", "pass1234");

        PostServiceReservationDTO dto = new PostServiceReservationDTO();
        dto.setEventId(1);
        dto.setReservationDate("2025-09-09");
        dto.setStartTime("10:00");
        dto.setEndTime("12:00");

        mockMvc.perform(post("/api/services/{serviceID}/reservations", 999)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void getMyReservations_successful() throws Exception {
        String token = loginAndGetToken("organizer2@example.com", "pass1234");

        Service service = serviceRepository.findById(12).orElseThrow();

        mockMvc.perform(get("/api/services/{serviceID}/reservations/my-reservations", service.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Transactional
    void cancelReservation_successful() throws Exception {

        String token = loginAndGetToken("organizer2@example.com", "pass1234");

        Organizer organizer = (Organizer) userRepository.findByEmail("organizer2@example.com");
        if (organizer == null) throw new IllegalStateException("Organizer not found in DB!");

        Event event = eventRepository.findById(2)
                .orElseThrow(() -> new IllegalStateException("Event not found"));

        Service service = serviceRepository.findById(12)
                .orElseThrow(() -> new IllegalStateException("Service not found"));

        PostServiceReservationDTO dto = new PostServiceReservationDTO();
        dto.setEventId(event.getId());

        LocalDate eventDate = event.getDateOfEvent().toLocalDate();
        LocalTime eventStartTime = event.getDateOfEvent().toLocalTime();
        LocalTime eventEndTime = event.getEndOfEvent().toLocalTime();

        dto.setReservationDate(eventDate.toString());
        dto.setStartTime(eventStartTime.plusHours(1).toString()); 
        dto.setEndTime(eventStartTime.plusHours(2).toString());

        String response = mockMvc.perform(post("/api/services/{serviceID}/reservations", service.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println("Response body: " + response);

        int reservationId = objectMapper.readTree(response).get("reservationId").asInt();

        mockMvc.perform(delete("/api/services/{serviceID}/reservations/{reservationId}", service.getId(), reservationId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    void cancelReservation_nonExistent_returnsNotFound() throws Exception {
        String token = loginAndGetToken("organizer2@example.com", "pass1234");

        mockMvc.perform(delete("/api/services/{serviceID}/reservations/{reservationId}", 12, 999)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void cancelReservation_notLoggedIn_returnsUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/services/{serviceID}/reservations/{reservationId}", 12, 1))
                .andExpect(status().isUnauthorized());
    }
}
