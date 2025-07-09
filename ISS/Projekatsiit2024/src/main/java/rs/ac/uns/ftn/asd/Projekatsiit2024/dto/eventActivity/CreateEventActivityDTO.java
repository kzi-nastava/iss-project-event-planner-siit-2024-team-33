package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventActivity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEventActivityDTO {
	private String name;
	private String description;
	private LocalDateTime startingTime;
	private LocalDateTime endingTime;
	private String location;
}
