package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventStatistics;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventArrayStats {
	private List<Integer> attendeesStats;
	private List<Integer> ratingsStats;
	
	public EventArrayStats(List<Integer> attendeesStats, List<Integer> ratingsStats) {
		this.attendeesStats = attendeesStats;
		this.ratingsStats = ratingsStats;
	}
}
