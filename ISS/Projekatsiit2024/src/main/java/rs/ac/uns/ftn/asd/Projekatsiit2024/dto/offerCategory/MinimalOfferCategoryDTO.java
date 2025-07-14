package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offerCategory;

import lombok.Getter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferType;

@Getter
public class MinimalOfferCategoryDTO {
	private Integer id;
	private String name;
	private String description;
	private Boolean isEnabled;
	private OfferType type;
	
	public MinimalOfferCategoryDTO(OfferCategory oc) {
		this.id = oc.getId();
		this.name = oc.getName();
		this.description = oc.getDescription();
		this.isEnabled = oc.getIsEnabled();
		this.type = oc.getOfferType();
	}
}