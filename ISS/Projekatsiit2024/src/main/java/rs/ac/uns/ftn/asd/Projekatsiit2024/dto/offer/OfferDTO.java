package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offer;

import com.fasterxml.jackson.annotation.JsonInclude;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Offer;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class OfferDTO {
	public Integer id;
	public String name;
	public Double basePrice;
	public Double discount;
	public Double discountedPrice;

	public OfferDTO(Offer offer) {
		this.id = offer.getId();
		this.name = offer.getName();
		this.basePrice = offer.getPrice();
		this.discount = offer.getDiscount();
		this.discountedPrice = offer.getPrice() - offer.getDiscount();
	}
}
