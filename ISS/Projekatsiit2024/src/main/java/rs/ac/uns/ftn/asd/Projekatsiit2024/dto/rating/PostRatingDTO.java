package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.rating;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Offer;

@Setter
@Getter
public class PostRatingDTO {
    private int value;
    private String comment;

    public PostRatingDTO() {
    }

    public PostRatingDTO(int value, String comment) {
        this.value = value;
        this.comment = comment;
    }

}
