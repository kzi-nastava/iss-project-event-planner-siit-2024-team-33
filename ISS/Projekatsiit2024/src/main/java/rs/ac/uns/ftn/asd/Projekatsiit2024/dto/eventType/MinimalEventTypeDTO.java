package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.EventType;

public class MinimalEventTypeDTO {
	private Integer id;
	private String name;
	private String description;

	public MinimalEventTypeDTO(EventType eventType) {
		this.id = eventType.getId();
		this.name = eventType.getName();
		this.description = eventType.getDescription();
	}
}
