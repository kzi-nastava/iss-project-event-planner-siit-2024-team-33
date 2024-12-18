package rs.ac.uns.ftn.asd.Projekatsiit2024.model;

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
