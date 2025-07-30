package rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;

@Entity
@Setter
@Getter
public class Service extends Offer
{
	private int ReservationInHours;
    private int CancellationInHours;
    private Boolean IsAutomatic;
    private int MinLengthInMins;
    private int MaxLengthInMins;
    
    public Service() {
    	super();
    	setType(OfferType.SERVICE);
    }
    
    public Service(
    		Integer offerID,
    		String name,
    		String description,
    		Double price,
    		Double discount,
			List<String> pictures,
			OfferCategory category,
			Provider provider,
			List<EventType> validEvents,
			String city,
			Availability availability,
			Integer reservationInHours,
			Integer cancellationInHours,
			Boolean isAutomatic,
			Integer minLengthInMins,
			Integer maxLengthInMins) {
		super(offerID, name, description, price, discount, pictures, category, provider, validEvents, city, availability);
		this.ReservationInHours = reservationInHours;
		this.CancellationInHours = cancellationInHours;
		this.IsAutomatic = isAutomatic;
		this.MinLengthInMins = minLengthInMins;
		this.MaxLengthInMins = maxLengthInMins;
    	setType(OfferType.SERVICE);
	}
}