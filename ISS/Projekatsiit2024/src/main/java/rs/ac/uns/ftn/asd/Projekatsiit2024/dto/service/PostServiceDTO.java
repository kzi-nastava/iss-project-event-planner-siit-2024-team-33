package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service;

import java.util.List;

public class PostServiceDTO {
	//DTO Will either contain this
	public Integer CategoryID;
	//or these
	public String CategoryName;
	public String CategoryDescription;
	
	public String Name;
	public String Description;
	public Double Price;
	public Double Discount;
	public List<String> Pictures;
	public Boolean isAutomatic;
	
}
