package rs.ac.uns.ftn.asd.Projekatsiit2024.offerReservationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation.PostServiceReservationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.offer.OfferRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.offer.OfferReservationRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.offer.ServiceRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OfferReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ServiceRepository serviceRepository;
    
    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OfferReservationRepository oRR;
    
    @Test
    public void reserveServiceTest() throws Exception {
    	 
    }
    
    @Test
    public void exceptionReserveServiceTest() throws Exception {
    	 
    }
    
    @Test
    public void getServiceReservationByIdTest() throws Exception {

    }

    
    @Test
    public void updateServiceReservationTest() throws Exception{
    	
    }
    
    @Test
    public void exceptionServiceReservationTest() throws Exception{
    	
    }
    
    
}

