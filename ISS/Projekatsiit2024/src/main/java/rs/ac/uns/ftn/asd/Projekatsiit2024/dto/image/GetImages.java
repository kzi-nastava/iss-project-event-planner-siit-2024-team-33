package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.image;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetImages {
	private List<String> imagesData;
	
	public GetImages() {
	}
	
	public GetImages(List<String> imagesData) {
		this.imagesData = imagesData;
	}
}
