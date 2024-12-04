package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.EventType;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class MinimalEventTypeDTO {
	public Integer id;
	public String name;
	public String description;

	public MinimalEventTypeDTO(EventType eventType) {
		this.id = eventType.getId();
		this.name = eventType.getName();
		this.description = eventType.getDescription();
	}
}
