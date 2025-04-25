package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Product;

public class MinimalProductDTO {
	public Integer id;
	public String name;
	public String description;
	public Availability availability;
	
	public MinimalProductDTO(Product p) {
		this.id = p.getId();
		this.name = p.getName();
		this.description = p.getDescription();
		this.availability = p.getAvailability();
	}
}
