package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget.DeleteBudgetItemDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget.GetBudgetDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.budget.PostBudgetItemDTO;

@RestController
@RequestMapping("/api/events/{eventID}/budget")
public class BudgetController {
	
	@GetMapping
	public ResponseEntity<GetBudgetDTO> GetBudgetInfo(@PathVariable Integer eventID){
		return ResponseEntity.ok(null);
	}
	
	@PostMapping
	public ResponseEntity<GetBudgetDTO> AddBudgetItem(@PathVariable Integer eventID, @RequestBody PostBudgetItemDTO PostDTO){
		return ResponseEntity.ok(null);
	}
	
	@PutMapping
	public ResponseEntity<GetBudgetDTO> EditBudgetItem(@PathVariable Integer eventID, @RequestBody PostBudgetItemDTO PutDTO){
		return ResponseEntity.ok(null);
	}
	
	@DeleteMapping
	public ResponseEntity<GetBudgetDTO> DeleteBudgetItem(@PathVariable Integer eventID, @RequestBody DeleteBudgetItemDTO DeleteDTO){
		return ResponseEntity.ok(null);
	}
}
