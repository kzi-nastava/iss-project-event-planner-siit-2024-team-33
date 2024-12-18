package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget;

import com.fasterxml.jackson.annotation.JsonInclude;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Offer;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class BudgetOfferDTO {
	public Integer versionId;
	public Integer categoryId;
	public Double cost;
	public String name;
	public String description;
	
	public BudgetOfferDTO(Offer o) {
		this.versionId = o.getId();
		this.categoryId = o.getCategoryId();
		this.cost = o.getPrice() - o.getDiscount();
		this.name = o.getName();
		this.description = o.getDescription();
	}
}
