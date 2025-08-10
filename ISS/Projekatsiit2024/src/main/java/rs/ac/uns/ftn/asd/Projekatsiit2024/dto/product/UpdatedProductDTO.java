package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.product.Product;
import rs.ac.uns.ftn.asd.Projekatsiit2024.utils.ImageManager;

@Getter
@Setter
public class UpdatedProductDTO {
	private Integer id;
	private String name;
	private String description;
	private Double price;
	private Double discount;
	private List<String> pictures;
	private Availability availability;
	private Boolean isPending;
	private Boolean isDeleted;
	private Integer offerCategoryId;
	private List<Integer> eventTypeIds;
	
	public UpdatedProductDTO() {
	}
	
	public UpdatedProductDTO(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.description = product.getDescription();
		this.price = product.getPrice();
		this.discount = product.getDiscount();
		if (product.getPictures() == null)
			product.setPictures(new ArrayList<>());
		this.setPictures(
				product.getPictures().stream()
			        .map(ImageManager::loadAsDataURI)
			        .filter(img -> img != null)
			        .toList());
		this.availability = product.getAvailability();
		this.isPending = product.getIsPending();
		this.isDeleted = product.getIsDeleted();
		this.offerCategoryId = product.getCategoryId();
		if (product.getValidEvents() == null)
			product.setValidEvents(new ArrayList<>());
		this.eventTypeIds = product.getValidEvents()
				.stream()
			    .map(eventType -> eventType.getId())
			    .toList();
	}
}
