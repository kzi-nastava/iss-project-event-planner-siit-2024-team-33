package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Event;
@Setter
@Getter
public class MinimalEventDTO {
	private Integer id;
    private String image;
    private String name;
    private String description;
    private double cost;

    
    public MinimalEventDTO(Event event) {
    	this.setId(event.getId());;
        this.setImage(event.getPicture());
        this.setName(event.getName());
        this.setDescription(event.getDescription());
        this.setCost(event.getPrice());
    }
    
    

    
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
