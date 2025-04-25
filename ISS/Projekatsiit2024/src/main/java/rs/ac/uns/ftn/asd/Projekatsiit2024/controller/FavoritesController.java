package rs.ac.uns.ftn.asd.Projekatsiit2024.controller;

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
	public ResponseEntity<?> AddToFavorites(@PathVariable Integer userID, @RequestAttribute Integer OfferID){
		//403: Forbidden if user sent wrong userID
		Boolean wrongUser = false;
		if(wrongUser)
			return ResponseEntity.status(403).build();
		
		//404: Offer not found
		Boolean notFound = false;
		if(notFound)
			return ResponseEntity.notFound().build();
		
		//409: Conflict
		Boolean alreadyFavorite = false;
		if(alreadyFavorite)
			return ResponseEntity.status(409).build();
		
		return ResponseEntity.ok(null);
	}

	@DeleteMapping("/favorites/{offerID}")
	public ResponseEntity<?> DeleteFromFavorites(@PathVariable Integer userID, @PathVariable Integer offerID){
		//403: Forbidden if user sent wrong userID
		Boolean wrongUser = false;
		if(wrongUser)
			return ResponseEntity.status(403).build();
		
		//404: Offer not found
		Boolean notFound = false;
		if(notFound)
			return ResponseEntity.notFound().build();
		
		//409: Conflict
		Boolean alreadyNotFavorite = false;
		if(alreadyNotFavorite)
			return ResponseEntity.status(409).build();
		
		return ResponseEntity.ok(null);
	}
}
