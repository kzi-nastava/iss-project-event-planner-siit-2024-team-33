package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget;

import java.util.List;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offer.OfferDTO;

public class BudgetItemDTO {
	public Integer EventID;
	public String EventName;
	public Integer OfferCategoryID;
	public String OfferCategoryName;
	public Double MaxBudget;
	public Double UsedBudget;
	public List<OfferDTO> ReservedOffers;
}
