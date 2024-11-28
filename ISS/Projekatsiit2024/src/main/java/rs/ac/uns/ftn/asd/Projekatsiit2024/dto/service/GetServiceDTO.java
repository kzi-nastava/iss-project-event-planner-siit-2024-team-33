package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service;

import java.util.List;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Service;

public class GetServiceDTO {
	public String Name;
	public String Description;
	public Double Price;
	public Double Discount;
	public List<String> Pictures;
	public List<Integer> ValidEventCategories;
	public Double AvgRating;
	public Boolean isVisible;
	public Boolean isAvailable;
	
	public int ReservationInHours;
	public int CancellationInHours;
	public Boolean isAutomatic;
	public int MinLengthInMins;
	public int MaxLengthInMins;
	
	public GetServiceDTO(Service s) {
		
	}
}
