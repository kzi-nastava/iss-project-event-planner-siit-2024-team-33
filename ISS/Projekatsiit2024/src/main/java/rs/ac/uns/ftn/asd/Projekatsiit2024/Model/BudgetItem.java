package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class BudgetItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer ID;
	public double Budget;
	
	@ManyToOne
	public Event Event;
	@ManyToOne
	public OfferCategory BudgetCategory;
}
