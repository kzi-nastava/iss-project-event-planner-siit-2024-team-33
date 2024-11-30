package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service;

import java.util.List;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.MinimalEventTypeDTO;

public class GetServiceDTO {
	public String name;
	public String description;
	public Double price;
	public Double discount;
	public List<String> pictures;
	public List<MinimalEventTypeDTO> validEventCategories;
	public Double avgRating;
	public Boolean isVisible;
	public Boolean isAvailable;
	
	public int reservationInHours;
	public int cancellationInHours;
	public Boolean isAutomatic;
	public int minLengthInMins;
	public int maxLengthInMins;
	
	public GetServiceDTO(Service s) {
		this.name = s.getName();
		this.description = s.getDescription();
		this.price = s.getPrice();
		this.discount = s.getDiscount();
		this.pictures = s.getPictures();
//		this.validEventCategories = s.getValidEvents().stream().map(eventType -> new MinimalEventTypeDTO(eventType)).toList();
		this.avgRating = null;
		//If it is available, it's visible
		this.isVisible = s.getAvailability() == Availability.AVAILABLE;
		this.isAvailable = s.getAvailability() == Availability.AVAILABLE || s.getAvailability() == Availability.INVISIBLE;
		
		this.reservationInHours = s.getReservationInHours();
		this.cancellationInHours = s.getCancellationInHours();
		this.isAutomatic = s.getIsAutomatic();
		this.minLengthInMins = s.getMinLengthInMins();
		this.maxLengthInMins = s.getMaxLengthInMins();
	}
}
