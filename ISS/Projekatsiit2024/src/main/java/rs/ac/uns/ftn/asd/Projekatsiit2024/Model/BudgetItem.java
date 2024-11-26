package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import jakarta.persistence.ManyToOne;

public class BudgetItem {
	double Budget;
	
	@ManyToOne
	Event Event;
	@ManyToOne
	OfferCategory BudgetCategory;
}
