package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.BudgetItem;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offer.OfferDTO;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class MinimalBudgetItemDTO {
	public Integer offerCategoryID;
	public String offerCategoryName;
	public Double maxBudget;
	
	public MinimalBudgetItemDTO(BudgetItem bi) {
		this.offerCategoryID = bi.getBudgetCategory().getId();
		this.offerCategoryName = bi.getBudgetCategory().getName();
		this.maxBudget = bi.getBudget();
	}
}
