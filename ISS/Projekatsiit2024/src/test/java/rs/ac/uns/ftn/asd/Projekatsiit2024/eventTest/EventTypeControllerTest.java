package rs.ac.uns.ftn.asd.Projekatsiit2024.eventTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventTypeRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EventTypeControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private EventTypeRepository eventTypeRepository;
	
	@BeforeEach
    void setup() {
        eventTypeRepository.deleteAll();

        EventType defaultType = new EventType();
        defaultType.setIsActive(true);
        defaultType.setName("All");
        defaultType.setDescription("A generic type of event.");
        defaultType.setId(1);
        eventTypeRepository.save(defaultType);

        EventType activeType = new EventType();
        activeType.setName("Conference");
        activeType.setDescription("Conference description");
        activeType.setIsActive(true);
        eventTypeRepository.save(activeType);

        EventType inactiveType = new EventType();
        inactiveType.setName("Workshop");
        inactiveType.setDescription("Workshop description");
        inactiveType.setIsActive(false);
        eventTypeRepository.save(inactiveType);
    }
	
	@Test
    void getEventTypes_ShouldAlwaysReturnDefaultTypeAndActiveTypes() throws Exception {
        mockMvc.perform(get("/api/eventTypes/active")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[?(@.name == 'All')]").exists())
            .andExpect(jsonPath("$[?(@.name == 'Conference')]").exists());
    }
	
	@Test
    void getEventTypes_ShouldReturnDefaultTypeEvenIfNoOtherActiveTypesExist() throws Exception {
        eventTypeRepository.deleteAll();
        EventType defaultType = new EventType();
        defaultType.setIsActive(true);
        defaultType.setName("All");
        defaultType.setDescription("A generic type of event.");
        defaultType.setId(1);
        eventTypeRepository.save(defaultType);
        
        EventType inactiveType = new EventType();
        inactiveType.setName("Workshop");
        inactiveType.setDescription("Workshop description");
        inactiveType.setIsActive(false);
        eventTypeRepository.save(inactiveType);

        mockMvc.perform(get("/api/eventTypes/active")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].name").value("All"))
            .andExpect(jsonPath("$[0].description").value("A generic type of event."));
    }
	
	@Test
	void getEventTypes_ShouldBeAccessibleWithoutAuthentication() throws Exception {
	    mockMvc.perform(get("/api/eventTypes/active"))
	        .andExpect(status().isOk());
	}
}
