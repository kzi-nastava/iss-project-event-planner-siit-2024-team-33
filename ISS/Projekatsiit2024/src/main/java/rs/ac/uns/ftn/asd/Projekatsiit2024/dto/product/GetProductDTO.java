package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product;

import java.util.List;

public class GetProductDTO {
	private Integer Id;
	private String Name;
	private String Description;
	private Double Price;
	private Double Discount;
	private List<String> Pictures;
	private List<Integer> ValidEventCategories;
	private Double AvgRating;
	private Boolean isVisible;
	private Boolean isAvailable;
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Double getPrice() {
		return Price;
	}
	public void setPrice(Double price) {
		Price = price;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public Double getDiscount() {
		return Discount;
	}
	public void setDiscount(Double discount) {
		Discount = discount;
	}
	public List<String> getPictures() {
		return Pictures;
	}
	public void setPictures(List<String> pictures) {
		Pictures = pictures;
	}
	public List<Integer> getValidEventCategories() {
		return ValidEventCategories;
	}
	public void setValidEventCategories(List<Integer> validEventCategories) {
		ValidEventCategories = validEventCategories;
	}
	public Double getAvgRating() {
		return AvgRating;
	}
	public void setAvgRating(Double avgRating) {
		AvgRating = avgRating;
	}
	public Boolean getIsAvailable() {
		return isAvailable;
	}
	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	public Boolean getIsVisible() {
		return isVisible;
	}
	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
}
