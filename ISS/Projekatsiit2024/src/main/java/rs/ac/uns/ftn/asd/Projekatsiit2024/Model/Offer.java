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
	private Integer ID;
	private Integer OfferID;
	private OfferType type;
	private String Name;
	private String Description;
	private Double Price;
	private Double Discount;
	private List<String> Pictures;
    @Enumerated(EnumType.STRING)
    private Availability Availability;
    private Date CreationDate;
    private Boolean IsPending;
    private Boolean IsDeleted;
    
    @ManyToOne
    private OfferCategory Category = null;
    @ManyToOne
    private Provider Provider = null;
    @ManyToMany
    private List<EventType> ValidEvents = new ArrayList<EventType>();
    @OneToMany(mappedBy = "Offer")
    private List<OfferReservation> OfferReservations;
    @OneToMany(mappedBy = "Offer")
    private List<Rating> Ratings;
    
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
		Name = name;
		Description = description;
		Price = price;
		Discount = discount;
		Pictures = pictures;
		Category = category;
		Provider = provider;
		this.CreationDate = new Date(System.currentTimeMillis());
		this.Availability = Availability.AVAILABLE;
	}
    
    
}
