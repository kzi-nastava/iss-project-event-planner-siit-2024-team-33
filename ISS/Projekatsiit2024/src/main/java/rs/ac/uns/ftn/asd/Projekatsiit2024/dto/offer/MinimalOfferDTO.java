package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offer;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.MinimalEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferType;

@Setter
@Getter
public class MinimalOfferDTO {
	public Integer id;
	public OfferType type;
	public String name;
	public Double basePrice;
	public String description;
	public String images;
	private List<MinimalEventTypeDTO> validEvents;
	
    public MinimalOfferDTO(Offer offer) {
        this.id = offer.getId();
        this.name = offer.getName();
        this.description = offer.getDescription();
        this.basePrice = offer.getPrice();
        this.type = offer.getType();
        this.validEvents = offer.getValidEvents() != null
            ? offer.getValidEvents().stream()
                .map(MinimalEventTypeDTO::new)
                .toList()
            : Collections.emptyList();
    }
}
