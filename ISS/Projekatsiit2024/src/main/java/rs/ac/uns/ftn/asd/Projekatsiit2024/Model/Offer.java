package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Setter
@Getter
public class Offer
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer offerID;
	private OfferType type;
	private String name;
	private String description;
	private Double price;
	private Double discount;
	private List<String> pictures;
    @Enumerated(EnumType.STRING)
    private Availability availability;
    private Date creationDate;
    private Boolean isPending;
    private Boolean isDeleted;
    
    @ManyToOne
    private OfferCategory category = null;
    @ManyToOne
    private Provider provider = null;
    @ManyToMany
    private List<EventType> validEvents = new ArrayList<EventType>();
    @OneToMany(mappedBy = "offer")
    private List<OfferReservation> offerReservations;
    @OneToMany(mappedBy = "offer")
    private List<Rating> ratings;
    
    public Offer() {
    	
    }
    
	public Offer(
			String name,
			String description,
			Double price,
			Double discount,
			List<String> pictures,
			OfferCategory category,
			Provider provider) {
		super();
		this.name = name;
		this.description = description;
		this.price = price;
		this.discount = discount;
		this.pictures = pictures;
		this.category = category;
		this.provider = provider;
		this.creationDate = new Date(System.currentTimeMillis());
		this.availability = availability.AVAILABLE;
	}
}
