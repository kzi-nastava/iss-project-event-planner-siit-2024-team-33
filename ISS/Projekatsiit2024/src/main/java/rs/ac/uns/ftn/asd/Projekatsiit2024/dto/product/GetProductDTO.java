package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product;

import java.util.List;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.product.Product;
import rs.ac.uns.ftn.asd.Projekatsiit2024.utils.ImageManager;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.eventType.MinimalEventTypeDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offerCategory.MinimalOfferCategoryDTO;

public class GetProductDTO {
	public Integer versionId;
	public Integer offerId;
	public String name;
	public String description;
	public Double price;
	public Double discount;
	public List<String> picturesDataURI;
	public Availability availability;
    public MinimalOfferCategoryDTO category;
    
    public Integer providerId;
    public String providerEmail;
    public String providerName;
    
    public List<MinimalEventTypeDTO> validEvents;
    
    public GetProductDTO(Product p) {
    	this.versionId = p.getId();
    	this.offerId = p.getOfferID();
    	this.name = p.getName();
    	this.description = p.getDescription();
    	this.price = p.getPrice();
    	this.discount = p.getDiscount();
    	this.picturesDataURI = p.getPictures().stream().map(imgpath -> ImageManager.loadAsDataURI(imgpath)).toList();
    	this.availability = p.getAvailability();
    	this.category = new MinimalOfferCategoryDTO(p.getCategory());
    	this.providerId = p.getProvider().getId();
    	this.providerEmail = p.getProvider().getEmail();
    	this.providerName = p.getProvider().getProviderName();
    	this.validEvents = p.getValidEvents().stream().map(et -> new MinimalEventTypeDTO(et)).toList();
    }
}
