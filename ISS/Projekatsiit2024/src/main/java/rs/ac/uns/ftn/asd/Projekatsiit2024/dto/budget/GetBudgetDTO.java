package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget;

import java.util.List;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offer.OfferDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offerCategory.MinimalOfferCategoryDTO;

public class GetBudgetDTO {
	public Integer eventID;
	public String eventName;
	public List<MinimalOfferCategoryDTO> recommendedOfferTypes;
	public List<BudgetItemDTO> takenItems;
	public List<BudgetOfferDTO> takenOffers;
}
