package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Event;

public class MinimalEventDTO {
    private String image;
    private String name;
    private String description;
    private double cost;

    
    private MinimalEventDTO mapToDto(Event event) {
    	MinimalEventDTO dto = new MinimalEventDTO();
        dto.setImage("");
        dto.setName(event.getName());
        dto.setDescription(event.getDescription());
        dto.setCost(0);
        return dto;
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
