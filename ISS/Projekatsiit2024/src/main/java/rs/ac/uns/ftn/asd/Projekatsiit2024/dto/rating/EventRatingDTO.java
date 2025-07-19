package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.EventRating;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRatingDTO {
    private Integer id;
    private Integer ratingValue;
    private String comment;
    private Boolean accepted;
    private Boolean isDeleted;

    private Integer authorId;   
    private String authorEmail;  

    private Integer eventId;
    private String eventName;    
    public EventRatingDTO(EventRating eventRating) {
        this.id = eventRating.getId();
        this.ratingValue = eventRating.getRatingValue();
        this.comment = eventRating.getComment();
        this.accepted = eventRating.getAccepted();
        this.isDeleted = eventRating.getIsDeleted();

        if (eventRating.getAuthor() != null) {
            this.authorId = eventRating.getAuthor().getId();
            this.authorEmail = eventRating.getAuthor().getEmail();
        }

        if (eventRating.getEvent() != null) {
            this.eventId = eventRating.getEvent().getId();
            this.eventName = eventRating.getEvent().getName();
        }
    }
}
