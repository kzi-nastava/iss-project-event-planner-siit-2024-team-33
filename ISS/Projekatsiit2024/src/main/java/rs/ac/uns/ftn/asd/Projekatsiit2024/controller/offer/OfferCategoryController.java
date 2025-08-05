package rs.ac.uns.ftn.asd.Projekatsiit2024.controller.offer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offerCategory.HandleSuggestionDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offerCategory.MinimalOfferCategoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offerCategory.PostOfferCategoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offerCategory.PutOfferCategoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferType;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.offer.OfferCategoryService;

@RestController
@RequestMapping("/api/offerCategories")
public class OfferCategoryController {
	@Autowired
	private OfferCategoryService offerCategoryService;
	
	@GetMapping
	public ResponseEntity<List<MinimalOfferCategoryDTO>> getCategories(
			@RequestParam(name = "isAccepted", required = false) Boolean isAccepted,
			@RequestParam(name = "isEnabled", required = false) Boolean isEnabled,
			@AuthenticationPrincipal UserPrincipal userPrincipal
			) {
		List<OfferCategory> ocs = offerCategoryService.getOffers(isAccepted, isEnabled);
		List<MinimalOfferCategoryDTO> miniOcs = ocs.stream().map(oc -> new MinimalOfferCategoryDTO(oc)).toList();
		return ResponseEntity.ok(miniOcs);
	}
	
	
	@GetMapping("/available")
	public ResponseEntity<List<MinimalOfferCategoryDTO>> getCategories(
			@RequestParam(name = "type", required = false) OfferType type) {
		List<OfferCategory> ocs = offerCategoryService.getSpecificAvailableOffers(true, true, type);
		List<MinimalOfferCategoryDTO> miniOcs = ocs.stream().map(oc -> new MinimalOfferCategoryDTO(oc)).toList();
		return ResponseEntity.ok(miniOcs);
	}
	
	
	@GetMapping("/exists")
	public ResponseEntity<Boolean> checkIfExists(@RequestParam String name) {
		boolean exists = offerCategoryService.existsByName(name);
		return ResponseEntity.ok(exists);
	}
	
	
	@GetMapping("/pending")
	public ResponseEntity getPendingCategories() {
		List<OfferCategory> ocs = offerCategoryService.getOffers(false, null);
		List<MinimalOfferCategoryDTO> miniOcs = ocs.stream().map(oc -> new MinimalOfferCategoryDTO(oc)).toList();
		return ResponseEntity.ok(miniOcs);
	}
	
	@PostMapping
	public ResponseEntity createNewCategory(@RequestBody PostOfferCategoryDTO data,
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		try {
			OfferCategory oc = offerCategoryService.createAndFlush(data.getName(), data.getDescription());
			return ResponseEntity.status(201).body(new MinimalOfferCategoryDTO(oc));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.toString());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity editExistingCategory(@PathVariable Integer id, @RequestBody PutOfferCategoryDTO data,
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		try {
			offerCategoryService.editCategory(id, data.name, data.description, data.isEnabled);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.toString());
		}
	}
	
	@PutMapping("/pending/{id}")
	public ResponseEntity handleCategorySuggestion(@PathVariable Integer id, @RequestBody HandleSuggestionDTO data,
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		try {
			OfferCategory oc;
			if(data.isAccepted)
				oc = offerCategoryService.acceptSuggestion(id, data.name, data.description);
			else
				oc = offerCategoryService.rejectSuggestion(id, data.newId);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(404).body(e.toString());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity deleteCategory(@PathVariable Integer id,
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
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