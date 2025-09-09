package rs.ac.uns.ftn.asd.Projekatsiit2024.eventTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventTypeRepository;

@DataJpaTest
@ActiveProfiles("test")
public class EventTypeRepositoryTest {

	@Autowired
	private EventTypeRepository eventTypeRepository;
	
	@Test
    void getActiveEventTypes_ShouldReturnOnlyActiveEventTypes() {
        EventType active = new EventType();
        active.setName("Conference");
        active.setDescription("Active EventType");
        active.setIsActive(true);
        eventTypeRepository.save(active);

        EventType inactive = new EventType();
        inactive.setName("Workshop");
        inactive.setDescription("Inactive EventType");
        inactive.setIsActive(false);
        eventTypeRepository.save(inactive);
        eventTypeRepository.flush();

        List<EventType> result = eventTypeRepository.getActiveEventTypes();
        
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Conference");
        assertThat(result.get(0).getIsActive()).isTrue();
    }

    @Test
    void getActiveEventTypes_ShouldReturnEmptyListWhenNoActiveEventTypesExist() {
        EventType inactive1 = new EventType();
        inactive1.setName("Conference");
        inactive1.setIsActive(false);
        eventTypeRepository.save(inactive1);

        EventType inactive2 = new EventType();
        inactive2.setName("Workshop");
        inactive2.setIsActive(false);
        eventTypeRepository.save(inactive2);
        eventTypeRepository.flush();

        List<EventType> result = eventTypeRepository.getActiveEventTypes();
        
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }
}
