package rs.ac.uns.ftn.asd.Projekatsiit2024.model.event;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EventActivity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String description;
	private LocalDateTime startingTime;
	private LocalDateTime endingTime;
	private String location;
	
	@ManyToOne
	private Event event;
}