package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Rating;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.rating.GetRatingDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.rating.PostRatingDTO;

@RestController
@RequestMapping("/api/offers/{offerID}/ratings")
public class RatingController {

    @PostMapping
    public ResponseEntity<String> RateOffer(@PathVariable Integer offerID, @RequestBody PostRatingDTO postRatingDTO) {
        Rating rating = new Rating();


        return ResponseEntity.ok("");
    }

    @GetMapping
    public ResponseEntity<List<GetRatingDTO>> GetRatings(@PathVariable Integer offerID) {
        List<Rating> ratings = new ArrayList<>();
        ratings.add(new Rating());
        ratings.add(new Rating());

        List<GetRatingDTO> ratingDTOs = new ArrayList<>();
        for (Rating rating : ratings) {
            ratingDTOs.add(new GetRatingDTO(rating));
        }

        return ResponseEntity.ok(ratingDTOs);
    }

    @GetMapping("/{ratingId}")
    public ResponseEntity<GetRatingDTO> GetRating(@PathVariable int ratingId) {
        Rating rating = null;
        rating.ID = 1;
        if (ratingId == 1) {
            rating = new Rating();
        }

        if (rating == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new GetRatingDTO(rating));
    }
}
