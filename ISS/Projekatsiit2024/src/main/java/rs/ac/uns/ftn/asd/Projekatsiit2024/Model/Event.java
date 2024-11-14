package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.sql.Date;

public class Event {
	private String Name;
	private String Description;
	private int NumberOfAtendees;
	private boolean Private;
	private String Place;
	private Date DateOfEvent;
	
	
	
	public Event(String name, String description, int numberOfAtendees, boolean private1, String place,
			Date dateOfEvent) {
		super();
		Name = name;
		Description = description;
		NumberOfAtendees = numberOfAtendees;
		Private = private1;
		Place = place;
		DateOfEvent = dateOfEvent;
	}
	
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public int getNumberOfAtendees() {
		return NumberOfAtendees;
	}
	public void setNumberOfAtendees(int numberOfAtendees) {
		NumberOfAtendees = numberOfAtendees;
	}
	public boolean isPrivate() {
		return Private;
	}
	public void setPrivate(boolean private1) {
		Private = private1;
	}
	public String getPlace() {
		return Place;
	}
	public void setPlace(String place) {
		Place = place;
	}
	public Date getDateOfEvent() {
		return DateOfEvent;
	}
	public void setDateOfEvent(Date dateOfEvent) {
		DateOfEvent = dateOfEvent;
	}
	
}
