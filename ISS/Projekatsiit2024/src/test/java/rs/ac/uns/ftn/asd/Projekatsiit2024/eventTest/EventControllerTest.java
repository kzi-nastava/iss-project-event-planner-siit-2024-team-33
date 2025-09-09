package rs.ac.uns.ftn.asd.Projekatsiit2024.eventTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@Test
	@WithMockUser(username="organizer@test.com", roles= {"ORGANIZER_ROLE"})
	void createEvent_ShouldReturnCreatedEvent() throws Exception {
		
		
	}
	
}
