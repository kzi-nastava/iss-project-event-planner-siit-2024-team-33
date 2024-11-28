package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.rating;

import java.sql.Date;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Rating;

public class GetRatingDTO {
    private int ratingId;
    private int offerId;
    private String OfferName;
    private int authorId;
    private String AuthorName;
    private int Value;
    private String Comment;
    private Boolean isAccepted;
    
    public GetRatingDTO(Rating R) {
    	
    }
}
