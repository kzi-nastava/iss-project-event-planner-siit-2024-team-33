package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.rating;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Rating;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
@Setter
@Getter
public class GetRatingDTO {
	private Integer id;
    private String offerName;
    private Integer authorId;
    private String authorName;
    private Integer value;
    private String comment;
    private Boolean isAccepted;
    private Boolean isDeleted;
    
    public GetRatingDTO(Rating rating) {
    	this.id = rating.getId();
        this.offerName = rating.getOffer().getName();
        this.authorId = rating.getAuthor().getId();
        this.authorName = rating.getAuthor().getName();
        this.value = rating.getRatingValue();
        this.comment = rating.getComment();
        this.isAccepted = rating.getAccepted();
        this.isDeleted = rating.getIsDeleted();
     }
}
