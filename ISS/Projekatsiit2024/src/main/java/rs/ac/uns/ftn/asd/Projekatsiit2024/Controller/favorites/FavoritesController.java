package rs.ac.uns.ftn.asd.Projekatsiit2024.controller.favorites;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.MinimalEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offer.MinimalOfferDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.favorites.FavoritesService;

@RestController
@RequestMapping("/api/favorites")
public class FavoritesController {
	
	@Autowired
	FavoritesService favoritesService;
	
	
	@GetMapping(value = "/events", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<MinimalEventDTO>> getFavoriteEvents(
			@AuthenticationPrincipal UserPrincipal userPrincipal,
			@PageableDefault(size = 10) Pageable pageable) {
		Page<MinimalEventDTO> events = favoritesService.getFavoriteEvents(userPrincipal, pageable);

		return new ResponseEntity<Page<MinimalEventDTO>>(events, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/events/{eventId}/exists", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> isFavoriteEvent(
	        @AuthenticationPrincipal UserPrincipal userPrincipal,
	        @PathVariable Integer eventId) {

	    boolean isFavorite = favoritesService.isEventFavorited(userPrincipal, eventId);
	    return ResponseEntity.ok(isFavorite);
	}
	
	
	@PostMapping(value = "/events/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MinimalEventDTO> addFavoriteEvent(@AuthenticationPrincipal UserPrincipal userPrincipal, 
    		@PathVariable Integer eventId) throws EventValidationException {
        Event event = favoritesService.addFavoriteEvent(eventId, userPrincipal);
        return ResponseEntity.ok(new MinimalEventDTO(event));
    }

	
    @DeleteMapping(value = "/events/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MinimalEventDTO> removeFavoriteEvent(@AuthenticationPrincipal UserPrincipal userPrincipal, 
    		@PathVariable Integer eventId) throws EventValidationException {
        Event event = favoritesService.removeFavoriteEvent(eventId, userPrincipal);
        return ResponseEntity.ok(new MinimalEventDTO(event));
    }
	
    @GetMapping(value = "/offers/{offerId}")
	public ResponseEntity<Boolean> isFavoriteOffer(
	        @AuthenticationPrincipal UserPrincipal userPrincipal,
	        @PathVariable Integer offerId){
		return new ResponseEntity<Boolean>(favoritesService.isOfferFavorited(userPrincipal, offerId), HttpStatus.OK);
	}
    
    @PostMapping(value = "/offers/{offerId}")
	public ResponseEntity<MinimalOfferDTO> addFavoriteOffer(
	        @AuthenticationPrincipal UserPrincipal userPrincipal,
	        @PathVariable Integer offerId){
		return new ResponseEntity<MinimalOfferDTO>(new MinimalOfferDTO(favoritesService.addFavoriteOffer(userPrincipal, offerId)), HttpStatus.OK);
	}
    
    @DeleteMapping(value = "/offers/{offerId}")
	public ResponseEntity<MinimalOfferDTO> removeFavoriteOffer(
	        @AuthenticationPrincipal UserPrincipal userPrincipal,
	        @PathVariable Integer offerId){
		return new ResponseEntity<MinimalOfferDTO>(new MinimalOfferDTO(favoritesService.removeFavoriteOffer(userPrincipal, offerId)), HttpStatus.OK);
	}
	
	
	
	
	/*@PostMapping("/favorites")
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
	}*/
}
