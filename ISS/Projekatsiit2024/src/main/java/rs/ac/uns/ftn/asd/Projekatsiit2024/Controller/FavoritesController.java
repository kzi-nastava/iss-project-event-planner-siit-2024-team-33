package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offer.OfferDTO;

@RestController
@RequestMapping("/user/{userID}")
public class FavoritesController {
	@PostMapping("/favorites")
	public ResponseEntity<OfferDTO> AddToFavorites(@PathVariable Integer userID, @RequestAttribute Integer OfferID){
		return ResponseEntity.ok(null);
	}

	@DeleteMapping("/favorites/{offerID}")
	public ResponseEntity<OfferDTO> DeleteFromFavorites(@PathVariable Integer userID, @PathVariable Integer offerID){
		return ResponseEntity.ok(null);
	}
}
