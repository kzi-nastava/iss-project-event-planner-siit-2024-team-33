package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offer;

import java.sql.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
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
	
	public MinimalOfferDTO(Offer o){
		this.setId(o.getId());
		this.setType(o.getType());
		this.setName(o.getName());
		this.setBasePrice(o.getPrice());
		this.setDescription(o.getDescription());
		this.setImages(o.getPictures().get(0));
	}
}
