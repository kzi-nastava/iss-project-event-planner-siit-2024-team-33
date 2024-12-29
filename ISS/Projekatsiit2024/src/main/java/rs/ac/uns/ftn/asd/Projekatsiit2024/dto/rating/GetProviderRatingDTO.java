package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.rating;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.ProviderRating;

@Getter
@Setter
public class GetProviderRatingDTO {
	private Integer id;
    private Integer authorId;
    private String authorName;
    private Integer providerId;
    private String providerName;
    private Integer value;
    private String comment;
    private Boolean isAccepted;
    private Boolean isDeleted;
    
    public GetProviderRatingDTO(ProviderRating pr) {
    	setId(pr.getId());
    	setAuthorId(pr.getAuthor().getId());
    	setAuthorName(pr.getAuthor().getName());
    	setProviderId(pr.getProvider().getId());
    	setProviderName(pr.getProvider().getProviderName());
    	setValue(pr.getRatingValue());
    	setComment(pr.getComment());
    	setIsAccepted(pr.getAccepted());
    	setIsDeleted(pr.getIsDeleted());
    }
}
