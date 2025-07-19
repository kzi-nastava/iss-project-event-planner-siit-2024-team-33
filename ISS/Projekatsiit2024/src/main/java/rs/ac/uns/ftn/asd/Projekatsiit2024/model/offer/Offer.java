package rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
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
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Rating;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;

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
    @Enumerated(EnumType.STRING)
	private OfferType type;
	private String name;
	@Column(length = 2048)
	private String description;
	private Double price;
	private Double discount;
	private List<String> pictures;
    @Enumerated(EnumType.STRING)
    private Availability availability;
    private LocalDateTime creationDate;
    private Boolean isPending;
    private Boolean isDeleted;
    private String city;
    
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
			Integer offerID,
			String name,
			String description,
			Double price,
			Double discount,
			List<String> pictures,
			OfferCategory category,
			Provider provider,
			List<EventType> validEvents,
			String city) {
		super();
		this.offerID = offerID;
		this.name = name;
		this.description = description;
		this.price = price;
		this.discount = discount;
		this.pictures = pictures;
		this.category = category;
		this.provider = provider;
		this.creationDate = LocalDateTime.now();
		this.validEvents = validEvents;
		this.availability = Availability.AVAILABLE;
		this.city = city;
		
		this.isPending = false;
		this.isDeleted =false;
	}

	public Integer getCategoryId() {
		return category.getId();
	}
}
