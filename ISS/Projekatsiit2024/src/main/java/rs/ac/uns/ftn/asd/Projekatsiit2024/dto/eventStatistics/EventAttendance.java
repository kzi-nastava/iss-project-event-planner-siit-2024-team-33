package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventStatistics;

import lombok.Getter;

@Getter
public class EventAttendance {
	private String attendanceState;
	private Integer numOfAttendees;
	
	public EventAttendance(String attendanceState, Integer numOfAttendees) {
		this.attendanceState = attendanceState;
		this.numOfAttendees = numOfAttendees;
	}
}
