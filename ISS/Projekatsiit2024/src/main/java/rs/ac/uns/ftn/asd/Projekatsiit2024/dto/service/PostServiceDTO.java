package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service;

import java.util.List;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Availability;

public class PostServiceDTO {
	//DTO Will either contain this
	public Integer categoryID;
	//or these
	public String categoryName;
	public String categoryDescription;
	
	public String name;
	public Double price;
	public String description;
	public Double discount;
	public Integer reservationInHours;
	public Integer cancellationInHours;
	public Integer minDurationInMins;
	public Integer maxDurationInMins;
	public Availability availability;
	public List<String> picturesDataURI;
	public Boolean isAutomatic;
	
	public List<Integer> validEventTypeIDs;
}
