package rs.ac.uns.ftn.asd.Projekatsiit2024.model.event;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;

@Entity
@Setter
@Getter
public class BudgetItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	private double budget;
	
	@ManyToOne
    private Event event;
    @ManyToOne
    private OfferCategory budgetCategory;
}
