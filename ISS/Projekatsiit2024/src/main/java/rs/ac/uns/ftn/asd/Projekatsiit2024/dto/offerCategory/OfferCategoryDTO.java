package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offerCategory;

import lombok.Getter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferType;

@Getter
public class OfferCategoryDTO {
	private String name;
	private String description;
	private Boolean isAccepted;
	private Boolean isEnabled;
	private OfferType offerType;
	
	public OfferCategoryDTO() {
	}
	
	public OfferCategoryDTO(OfferCategory oc) {
		this.name = oc.getName();
		this.description = oc.getDescription();
		this.isAccepted = oc.getIsAccepted();
		this.isEnabled = oc.getIsEnabled();
		this.offerType = oc.getOfferType();
	}
}
