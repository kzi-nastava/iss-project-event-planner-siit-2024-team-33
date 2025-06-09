package rs.ac.uns.ftn.asd.Projekatsiit2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.rating.GetRatingDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.rating.PostRatingDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Rating;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.RatingService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.auth.AuthenticationService;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;
    @Autowired
    private AuthentifiedUserRepository userRepo;
    @PostMapping
    public ResponseEntity<GetRatingDTO> submitRating(@RequestBody PostRatingDTO postRatingDTO,
                                                     @RequestParam int offerId)  {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails principal = (UserDetails) auth.getPrincipal();
		String email = principal.getUsername();
	
		AuthentifiedUser user = userRepo.findByEmail(email);
		
		int userId = user.getId();
        Rating rating = ratingService.submitComment(postRatingDTO.getValue(), postRatingDTO.getComment(), userId, offerId);
        return ResponseEntity.ok(new GetRatingDTO(rating));
    }

    @PutMapping("/approve/{commentId}")
    public ResponseEntity<Void> approveRating(@PathVariable int commentId) {
        ratingService.approveComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteRating(@PathVariable int commentId) {
        ratingService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/offer/{offerId}")
    public ResponseEntity<List<GetRatingDTO>> getRatingsByOffer(@PathVariable int offerId) {
        List<Rating> ratings = ratingService.getRatingsByOfferId(offerId);
        List<GetRatingDTO> ratingDTOs = ratings.stream()
                .map(GetRatingDTO::new)
                .toList();
        return ResponseEntity.ok(ratingDTOs);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<GetRatingDTO>> getRatings(){
    	List<Rating> ratings = ratingService.getAllRatings();
        List<GetRatingDTO> ratingDTOs = ratings.stream()
                .map(GetRatingDTO::new)
                .toList();
    	return ResponseEntity.ok(ratingDTOs);
    }

    @GetMapping("/{ratingId}")
    public ResponseEntity<GetRatingDTO> getRatingById(@PathVariable int ratingId) {
        Rating rating = ratingService.getRatingById(ratingId);
        return ResponseEntity.ok(new GetRatingDTO(rating));
    }
}
