package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.OfferCategoryService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offerCategory.HandleSuggestionDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offerCategory.MinimalOfferCategoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offerCategory.PostOfferCategoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offerCategory.PutOfferCategoryDTO;

@RestController
@RequestMapping("/api/offers/categories")
public class OfferCategoryController {
	@Autowired
	private OfferCategoryService offerCategoryService;
	
	@GetMapping
	public ResponseEntity getCategories(
			@RequestParam(name = "isAccepted", required = false) Boolean isAccepted,
			@RequestParam(name = "isEnabled", required = false) Boolean isEnabled
			) {
		List<OfferCategory> ocs = offerCategoryService.getOffers(isAccepted, isEnabled);
		List<MinimalOfferCategoryDTO> miniOcs = ocs.stream().map(oc -> new MinimalOfferCategoryDTO(oc)).toList();
		return ResponseEntity.ok(miniOcs);
	}
	
	@GetMapping("/pending")
	public ResponseEntity getPendingCategories() {
		List<OfferCategory> ocs = offerCategoryService.getOffers(false, null);
		List<MinimalOfferCategoryDTO> miniOcs = ocs.stream().map(oc -> new MinimalOfferCategoryDTO(oc)).toList();
		return ResponseEntity.ok(miniOcs);
	}
	
	@PostMapping
	public ResponseEntity createNewCategory(@RequestBody PostOfferCategoryDTO data) {
		try {
			OfferCategory oc = offerCategoryService.createAndFlush(data.name, data.description);
			return ResponseEntity.status(201).body(new MinimalOfferCategoryDTO(oc));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.toString());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity editExistingCategory(@PathVariable Integer id, @RequestBody PutOfferCategoryDTO data) {
		try {
			offerCategoryService.editCategory(id, data.name, data.description, data.isEnabled);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.toString());
		}
	}
	
	@PutMapping("/pending/{id}")
	public ResponseEntity handleCategorySuggestion(@PathVariable Integer id, @RequestBody HandleSuggestionDTO data) {
		//TODO: Admin validation
		try {
			OfferCategory oc;
			if(data.isAccepted)
				oc = offerCategoryService.acceptSuggestion(id, data.name, data.description);
			else
				oc = offerCategoryService.rejectSuggestion(id, data.newId);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.toString());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity deleteCategory(@PathVariable Integer id) {
		try {
			offerCategoryService.deleteCategory(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.toString());
		} catch (DataIntegrityViolationException e) {
			//Conflict
			return ResponseEntity.status(409).body(e.toString());
		}
	}
}
