package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product;

import java.util.List;

import lombok.Getter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offerCategory.PostOfferCategoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Availability;

@Getter
public class CreateProductDTO {
	private String name;
	private String description;
	private Double price;
	private Double discount;
	private List<String> pictures;
	private Availability availability;
	private Boolean isPending;
	private Integer existingCategoryId;
	private PostOfferCategoryDTO newCategory;
	private List<Integer> eventTypeIds;
}
