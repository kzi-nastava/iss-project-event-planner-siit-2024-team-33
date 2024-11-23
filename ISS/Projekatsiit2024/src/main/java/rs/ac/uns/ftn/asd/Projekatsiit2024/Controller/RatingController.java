package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.rating.GetRatingDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.rating.PostRatingDTO;

@RestController
@RequestMapping("/ratings")
public class RatingController {
	
	@PostMapping
    public ResponseEntity<String> createRating(@RequestBody PostRatingDTO postRatingDTO) {
        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<List<GetRatingDTO>> getRatingsByOffer(@RequestParam int offerId) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{ratingId}")
    public ResponseEntity<GetRatingDTO> getRatingById(@RequestAttribute int ratingId) {
        return ResponseEntity.ok(null);
    }


}
