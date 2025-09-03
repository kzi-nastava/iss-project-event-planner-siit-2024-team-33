package rs.ac.uns.ftn.asd.Projekatsiit2024.controller.offer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget.BudgetItemDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget.GetBudgetDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget.MinimalBudgetItemDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget.PostBudgetItemDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget.PutBudgetItemDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.BudgetItem;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.offer.BudgetService;

@RestController
@RequestMapping("/api/events/{eventID}/budget")
public class BudgetController {
	@Autowired
	private BudgetService budgetService;
	
	@GetMapping
	public ResponseEntity GetBudgetInfo(@PathVariable Integer eventID){
		try {
			return ResponseEntity.ok(budgetService.getBudget(eventID));
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(404).body(e.toString());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.toString());
		} catch (AuthenticationCredentialsNotFoundException e) {
			return ResponseEntity.status(401).body(null);
		} catch (AccessDeniedException e) {
			return ResponseEntity.status(403).body(null);
		}
	}
	
	@PostMapping
	public ResponseEntity AddBudgetItem(@PathVariable Integer eventID, @RequestBody PostBudgetItemDTO PostDTO){
		try {
			BudgetItem bi = budgetService.createItem(eventID, PostDTO.offerCategoryID, PostDTO.maxBudget);
			return ResponseEntity.ok(new MinimalBudgetItemDTO(bi));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.toString());
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(404).body(e.toString());
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(409).body(e.toString());
		} catch (AuthenticationCredentialsNotFoundException e) {
			return ResponseEntity.status(401).body(null);
		} catch (AccessDeniedException e) {
			return ResponseEntity.status(403).body(null);
		}
	}
	
	@PutMapping("/{categoryID}")
	public ResponseEntity EditBudgetItem(@PathVariable Integer eventID, @PathVariable Integer categoryID, @RequestBody PutBudgetItemDTO PutDTO){
		try {
			BudgetItem bi = budgetService.editItem(eventID, categoryID, PutDTO.maxBudget);
			return ResponseEntity.ok(new MinimalBudgetItemDTO(bi));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.toString());
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(404).body(e.toString());
		} catch (AuthenticationCredentialsNotFoundException e) {
			return ResponseEntity.status(401).body(null);
		} catch (AccessDeniedException e) {
			return ResponseEntity.status(403).body(null);
		}
	}
	
	@DeleteMapping("/{categoryID}")
	public ResponseEntity DeleteBudgetItem(@PathVariable Integer eventID, @PathVariable Integer categoryID){
		try {
			budgetService.deleteItem(eventID, categoryID);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.toString());
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(404).body(e.toString());
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(409).body(e.toString());
		} catch (AuthenticationCredentialsNotFoundException e) {
			return ResponseEntity.status(401).body(null);
		} catch (AccessDeniedException e) {
			return ResponseEntity.status(403).body(null);
		}
	}
}
