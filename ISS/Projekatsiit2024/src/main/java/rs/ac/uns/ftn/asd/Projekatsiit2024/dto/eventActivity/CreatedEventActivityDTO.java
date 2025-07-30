package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventActivity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventActivity;

@Getter
@Setter
public class CreatedEventActivityDTO {
	private Integer id;
	private String name;
	private String description;
	private LocalDateTime startingTime;
	private LocalDateTime endingTime;
	private String location;
	
	public CreatedEventActivityDTO(EventActivity eventActivity) {
		this.setId(eventActivity.getId());
		this.setName(eventActivity.getName());
		this.setDescription(eventActivity.getDescription());
		this.setStartingTime(eventActivity.getStartingTime());
		this.setEndingTime(eventActivity.getEndingTime());
		this.setLocation(eventActivity.getLocation());
	}
}
