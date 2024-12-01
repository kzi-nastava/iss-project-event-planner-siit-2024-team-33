package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget;

import java.util.List;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offer.OfferDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offerCategory.MinimalOfferCategoryDTO;

public class GetBudgetDTO {
	public Integer EventID;
	public String EventName;
	public List<MinimalOfferCategoryDTO> TakenOfferTypes;
	public List<MinimalOfferCategoryDTO> RecommendedOfferTypes;
	public List<BudgetItemDTO> Items;
}
