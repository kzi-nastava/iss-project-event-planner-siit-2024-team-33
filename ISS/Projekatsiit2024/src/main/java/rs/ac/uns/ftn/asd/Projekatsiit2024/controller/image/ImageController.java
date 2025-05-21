package rs.ac.uns.ftn.asd.Projekatsiit2024.controller.image;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.image.GetImage;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.image.PostImage;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.image.PostedImage;
import rs.ac.uns.ftn.asd.Projekatsiit2024.utils.ErrorMessages;
import rs.ac.uns.ftn.asd.Projekatsiit2024.utils.ImageManager;

@RestController
@RequestMapping(value = "api/images", produces = MediaType.APPLICATION_JSON_VALUE)
public class ImageController {

    @GetMapping("/{filename}")
    public ResponseEntity<GetImage> getImage(@PathVariable String filename) {
        String dataUri = ImageManager.loadAsDataURI(filename);

        if (dataUri == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new GetImage(dataUri));
    }
	
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestBody PostImage payload) {
    	 String base64Image = payload.getImageData();

         if (base64Image == null || base64Image.isEmpty()) {
             return ResponseEntity.badRequest().body(new ErrorMessages("Image data is missing."));
         }

         if (!ImageManager.isValidImage(base64Image)) {
             return ResponseEntity.badRequest().body(new ErrorMessages("Invalid image type, extension or size."));
         }

         String fileName = ImageManager.saveAsFile(base64Image);

         if (fileName == null) {
             return ResponseEntity.status(500).body(new ErrorMessages("Failed to save image."));
         }

         return ResponseEntity.ok(new PostedImage(fileName));
    }
	
}
