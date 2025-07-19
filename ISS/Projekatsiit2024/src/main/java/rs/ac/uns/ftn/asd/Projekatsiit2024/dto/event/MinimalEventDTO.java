package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.MinimalEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
@Setter
@Getter
public class MinimalEventDTO {
	private Integer id;
    private String name;
    private String description;
    private MinimalEventTypeDTO validEvent;
    public MinimalEventDTO(Event event) {
        this.setId(event.getId());
        this.setName(event.getName());
        this.setDescription(event.getDescription());
        if (event.getEventType() != null) {
            this.setValidEvent(new MinimalEventTypeDTO(event.getEventType()));
        }
    }
}
