package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.sql.Date;
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

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer offerID;
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
    private OfferCategory category;
    
    @ManyToOne
    private Provider provider;
    
    @ManyToMany
    private List<EventType> validEvents;
    
    @OneToMany(mappedBy = "offer") 
    private List<OfferReservation> offerReservations;
    
    @OneToMany(mappedBy = "offer") 
    private List<Rating> ratings; 

}
