package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product;

import java.util.List;

public class GetProductDTO {
	public String Name;
	public String Description;
	public Double Price;
	public Double Discount;
	public List<String> Pictures;
	public List<Integer> ValidEventCategories;
	public Double AvgRating;
	public Boolean isVisible;
	public Boolean isAvailable;
}
