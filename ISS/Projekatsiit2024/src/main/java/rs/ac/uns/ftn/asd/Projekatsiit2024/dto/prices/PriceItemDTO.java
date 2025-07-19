package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.prices;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;

public class PriceItemDTO {
	public Integer offerId;
	public String name;
	public Double fullPrice;
	public Double discount;
	
	public PriceItemDTO(Offer o) {
		this.offerId = o.getOfferID();
		this.name = o.getName();
		this.fullPrice = o.getPrice();
		this.discount = o.getDiscount();
	}
}
