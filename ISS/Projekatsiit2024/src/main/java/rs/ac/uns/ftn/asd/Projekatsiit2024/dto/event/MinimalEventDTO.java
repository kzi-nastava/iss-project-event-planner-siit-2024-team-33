package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
@Setter
@Getter
public class MinimalEventDTO {
	private Integer id;
    private String name;
    private String description;
    
    public MinimalEventDTO(Event event) {
    	this.setId(event.getId());;
        this.setName(event.getName());
        this.setDescription(event.getDescription());
    }
}
