package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service;

import java.util.List;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Availability;

public class PutServiceDTO {
	public String name;
	public Double price;
	public String description;
	public Double discount;
	public Integer reservationInHours;
	public Integer cancellationInHours;
	public Integer minDurationInMins;
	public Integer maxDurationInMins;
	public Availability availability;
	public List<String> pictures;
	public Boolean isAutomatic;
	
	public List<Integer> validEventTypeIDs;
}
