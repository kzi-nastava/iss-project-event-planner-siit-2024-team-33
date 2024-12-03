package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offer;

import java.sql.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Offer;

@Setter
@Getter
public class GetOfferDTO {
	public String name;
	public Double basePrice;
	public String description;
	public String images;
	
	public GetOfferDTO(Offer o){
		this.setName(o.getName());
		this.setBasePrice(o.getPrice());
		this.setDescription(o.getDescription());
		this.setImages(o.getPictures().get(0));
	}
}
