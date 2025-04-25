package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offer.OfferDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.BudgetItem;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Offer;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class BudgetItemDTO {
	public Integer offerCategoryID;
	public String offerCategoryName;
	public Double maxBudget;
	public Double usedBudget;
	
	public BudgetItemDTO(BudgetItem bi, List<Offer> offers) {
		this.offerCategoryID = bi.getBudgetCategory().getId();
		this.offerCategoryName = bi.getBudgetCategory().getName();
		this.maxBudget = bi.getBudget();
		this.usedBudget = offers.stream().mapToDouble(offer -> offer.getPrice()-offer.getDiscount()).sum();
	}
}
