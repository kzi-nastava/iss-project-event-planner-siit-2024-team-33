package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

public class Service {
	private String Name;
	private String Description;
	private int Price;
	private int Discount;
	private String[] Pictures;
	private Availability Availability;
	public Service(String name, String description, int price, int discount, String[] pictures,
			rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Availability availability) {
		super();
		Name = name;
		Description = description;
		Price = price;
		Discount = discount;
		Pictures = pictures;
		Availability = availability;
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
	public int getPrice() {
		return Price;
	}
	public void setPrice(int price) {
		Price = price;
	}
	public int getDiscount() {
		return Discount;
	}
	public void setDiscount(int discount) {
		Discount = discount;
	}
	public String[] getPictures() {
		return Pictures;
	}
	public void setPictures(String[] pictures) {
		Pictures = pictures;
	}
	public Availability getAvailability() {
		return Availability;
	}
	public void setAvailability(Availability availability) {
		Availability = availability;
	}
	
	
}
