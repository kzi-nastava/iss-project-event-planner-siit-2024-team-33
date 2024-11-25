package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget;

import java.util.List;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offer.MinimalOfferCategoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offer.OfferDTO;

public class GetBudgetDTO {
	public Integer EventID;
	public String EventName;
	public List<MinimalOfferCategoryDTO> RecommendedOfferTypes;
	
	public List<BudgetItemDTO> Items;
	public List<OfferDTO> RecommendedOffers;
}
