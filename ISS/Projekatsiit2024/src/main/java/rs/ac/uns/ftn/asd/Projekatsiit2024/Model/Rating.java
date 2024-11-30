package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int ratingValue;
    private String comment; 
    private Boolean accepted;

    @ManyToOne
    private AuthentifiedUser author;
    
    @ManyToOne
    private Offer offer; 
}
