package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product;

import java.util.List;

import lombok.Getter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Availability;

@Getter
public class UpdateProductDTO {
	private String name;
	private String description;
	private Double price;
	private Double discount;
	private List<String> pictures;
	private Availability availability;
	private List<Integer> eventTypeIds;
}
