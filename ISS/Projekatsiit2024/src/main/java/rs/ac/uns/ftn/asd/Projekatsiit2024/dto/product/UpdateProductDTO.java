package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product;

import java.util.List;

public class UpdateProductDTO {
	private String Name;
	private String Description;
	private Double Price;
	private Double Discount;
	private List<String> Pictures;
	private List<Integer> ValidEventCategories;
	private Double AvgRating;
	private Boolean isVisible;
	private Boolean isAvailable;
}
