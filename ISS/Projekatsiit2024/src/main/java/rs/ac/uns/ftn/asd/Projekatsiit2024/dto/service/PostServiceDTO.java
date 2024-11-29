package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service;

import java.util.List;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Availability;

public class PostServiceDTO {
	//DTO Will either contain this
	public Integer CategoryID;
	//or these
	public String CategoryName;
	public String CategoryDescription;
	
	public String Name;
	public Double Price;
	public String Description;
	public Double Discount;
	public Integer ReservationInHours;
	public Integer CancellationInHours;
	public Integer MinDurationInMins;
	public Integer MaxDurationInMins;
	public Availability Availability;
	public List<String> Pictures;
	public Boolean isAutomatic;
	
	public Integer ProviderID;
	public List<Integer> ValidEventTypeIDs;
}
