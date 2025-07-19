package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventActivity;

import java.time.format.DateTimeFormatter;

import lombok.Getter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventActivity;

@Getter
public class EventActivityPDF {
	private String timeEventActivity;
	private String nameEventActivity;
	private String descriptionEventActivity;
	private String locationEventActivity;
	
	public EventActivityPDF(EventActivity eventActivity) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a");
		this.timeEventActivity = eventActivity.getStartingTime().format(formatter) + " - " +
                eventActivity.getEndingTime().format(formatter);
		this.nameEventActivity = eventActivity.getName();
		this.descriptionEventActivity = eventActivity.getDescription();
		this.locationEventActivity = eventActivity.getLocation();
	}
}
