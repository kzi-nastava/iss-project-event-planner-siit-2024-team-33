package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Product;

public class MinimalProductDTO {
	private Integer id;
	private String name;
	private String description;
	public Availability availability;
	
	public MinimalProductDTO(Product p) {
		this.id = p.getId();
		this.name = p.getName();
		this.description = p.getDescription();
		this.availability = p.getAvailability();
	}
}
