package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.image;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetImage {
	private String imageData;
	
	public GetImage() {
	}
	
	public GetImage(String imageData) {
		this.imageData = imageData;
	}
}
