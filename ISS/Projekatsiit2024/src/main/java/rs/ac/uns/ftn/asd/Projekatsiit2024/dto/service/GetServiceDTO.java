package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service;

import java.util.List;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Common.ImageManager;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.MinimalEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offerCategory.MinimalOfferCategoryDTO;

public class GetServiceDTO {
	public Integer id;
	public MinimalOfferCategoryDTO category;
	public String name;
	public String description;
	public Double price;
	public Double discount;
	public List<String> picturesDataURI;
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
		this.id = s.getId();
		this.name = s.getName();
		this.description = s.getDescription();
		this.price = s.getPrice();
		this.discount = s.getDiscount();
		
		this.picturesDataURI = s.getPictures().stream().map(filepath -> ImageManager.loadAsDataURI(filepath)).toList();
		this.validEventCategories = s.getValidEvents().stream().map(eventType -> new MinimalEventTypeDTO(eventType)).toList();
		this.avgRating = null;
		//If it is available, it's visible
		this.isVisible = s.getAvailability() == Availability.AVAILABLE;
		this.isAvailable = s.getAvailability() == Availability.AVAILABLE || s.getAvailability() == Availability.INVISIBLE;
		
		this.category = new MinimalOfferCategoryDTO(s.getCategory());
		this.reservationInHours = s.getReservationInHours();
		this.cancellationInHours = s.getCancellationInHours();
		this.isAutomatic = s.getIsAutomatic();
		this.minLengthInMins = s.getMinLengthInMins();
		this.maxLengthInMins = s.getMaxLengthInMins();
	}
}
