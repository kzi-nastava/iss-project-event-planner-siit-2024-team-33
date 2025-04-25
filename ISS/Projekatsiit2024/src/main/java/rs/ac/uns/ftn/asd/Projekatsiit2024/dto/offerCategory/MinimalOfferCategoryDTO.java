package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offerCategory;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.OfferCategory;

public class MinimalOfferCategoryDTO {
	public Integer id;
	public String name;
	public String description;
	public Boolean isEnabled;
	
	public MinimalOfferCategoryDTO(OfferCategory oc) {
		this.id = oc.getId();
		this.name = oc.getName();
		this.description = oc.getDescription();
		this.isEnabled = oc.getIsEnabled();
	}
}
