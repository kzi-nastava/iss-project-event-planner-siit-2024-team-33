package rs.ac.uns.ftn.asd.Projekatsiit2024.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.OfferCategoryRepository;

@Service
public class OfferCategoryService {
	
	@Autowired
	private OfferCategoryRepository OfferCategoryRepo;
	
	//Without database flush
	public OfferCategory create(String name, String description) {
		//TODO: Validation
		OfferCategory oc = new OfferCategory(name, description, true, true);
		OfferCategoryRepo.save(oc);
		return oc;
	}
	
	public OfferCategory createAndFlush(String name, String description) {
		//TODO: Validation
		OfferCategory oc = create(name, description);
		OfferCategoryRepo.flush();
		return oc;
	}
	
	//Without flush
	public OfferCategory createSuggestion(String name, String description) {
		//TODO: Validation
		OfferCategory oc = new OfferCategory(name, description, false, false);
		OfferCategoryRepo.save(oc);
		//TODO: Send notification
		return oc;
	}
}
