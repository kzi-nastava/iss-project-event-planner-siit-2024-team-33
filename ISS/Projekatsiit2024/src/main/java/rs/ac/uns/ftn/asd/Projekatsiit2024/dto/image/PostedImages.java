package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.image;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostedImages {
	private List<String> fileNames;
	
	public PostedImages() {
	}
	
	public PostedImages(List<String> fileNames) {
		this.fileNames = fileNames;
	}
}
