package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.rating;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Rating;
@Setter
@Getter
public class GetRatingDTO {
    private String offerName;
    private int authorId;
    private String authorName;
    private int value;
    private String comment;
    
    public GetRatingDTO(Rating rating) {
        this.offerName = rating.getOffer().getName();
        this.authorId = rating.getAuthor().getId();
        this.authorName = rating.getAuthor().getName();
        this.value = rating.getRatingValue();
        this.comment = rating.getComment();
    }
}
