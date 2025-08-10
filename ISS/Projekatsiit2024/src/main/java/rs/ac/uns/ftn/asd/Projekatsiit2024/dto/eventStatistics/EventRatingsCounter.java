package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventStatistics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventRatingsCounter {
	private String grade;
	private Integer numOfAuthors;
	
	public EventRatingsCounter(String grade, Integer numOfAuthors) {
		this.grade = grade;
		this.numOfAuthors = numOfAuthors;
	}
}
