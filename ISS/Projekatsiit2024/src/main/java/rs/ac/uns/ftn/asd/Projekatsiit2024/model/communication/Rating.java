package rs.ac.uns.ftn.asd.Projekatsiit2024.model.communication;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;

@Entity
@Setter
@Getter
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer ratingValue;
    private String comment; 
    private Boolean accepted;
    private Boolean isDeleted;

    @ManyToOne
    private AuthentifiedUser author;
    
    @ManyToOne
    private Offer offer; 
}
