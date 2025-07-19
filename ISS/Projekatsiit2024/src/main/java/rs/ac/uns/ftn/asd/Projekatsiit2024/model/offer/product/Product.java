package rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.product;

import java.util.List;

import jakarta.persistence.Entity;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;

@Entity
public class Product extends Offer
{
	public Product() {
		super();
		setType(OfferType.PRODUCT);
	}
	
	public Product(
			Integer offerID,
			String name,
			String description,
			Double price,
			Double discount,
			List<String> pictures,
			OfferCategory category,
			Provider provider,
			List<EventType> validEvents,
			String city) {
		super(offerID, name, description, price, discount, pictures, category, provider, validEvents, city);
		setType(OfferType.PRODUCT);
	}
}
