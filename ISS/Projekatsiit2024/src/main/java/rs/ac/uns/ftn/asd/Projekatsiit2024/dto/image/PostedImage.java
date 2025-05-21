package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.image;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostedImage {
	private String fileName;
	
	public PostedImage() {
	}
	
	public PostedImage(String fileName) {
		this.fileName = fileName;
	}
}
