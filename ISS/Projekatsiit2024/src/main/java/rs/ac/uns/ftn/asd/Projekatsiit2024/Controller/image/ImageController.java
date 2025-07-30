package rs.ac.uns.ftn.asd.Projekatsiit2024.controller.image;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.image.GetImage;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.image.GetImages;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.image.PostImage;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.image.PostImages;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.image.PostedImage;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.image.PostedImages;
import rs.ac.uns.ftn.asd.Projekatsiit2024.utils.ErrorMessages;
import rs.ac.uns.ftn.asd.Projekatsiit2024.utils.ImageManager;

@RestController
@RequestMapping(value = "api/images", produces = MediaType.APPLICATION_JSON_VALUE)
public class ImageController {

    @GetMapping("/{filename}")
    public ResponseEntity<?> getImage(@PathVariable String filename) {
    	
        String dataUri = ImageManager.loadAsDataURI(filename);

        if (dataUri == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new GetImage(dataUri));
    }
    
    
    @GetMapping("/batch")
    public ResponseEntity<?> getImages(@RequestParam List<String> filenames) {
        if (filenames.size() > 8) {
            return ResponseEntity.badRequest().body(new ErrorMessages("Maximum 8 images allowed."));
        }

        List<String> dataUris = new ArrayList<>();

        for (String filename : filenames) {
            String trimmed = filename.trim();
            String dataUri = ImageManager.loadAsDataURI(trimmed);

            if (dataUri == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(new ErrorMessages("Failed to load image: " + trimmed));
            }
            dataUris.add(dataUri);
        }

        return ResponseEntity.ok(new GetImages(dataUris));
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
    
    
    @PostMapping("/batch/upload")
    public ResponseEntity<?> uploadImages(@RequestBody PostImages payload) {
        List<String> images = payload.getImagesData();

        if (images == null || images.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorMessages("No images provided."));
        }

        if (images.size() > 8) {
            return ResponseEntity.badRequest().body(new ErrorMessages("Maximum 8 images allowed."));
        }

        //validating all images first
        for (String img : images) {
            String base64 = img;
            if (base64 == null || base64.isEmpty() || !ImageManager.isValidImage(base64)) {
                return ResponseEntity.badRequest().body(new ErrorMessages("Invalid image data detected."));
            }
        }

        List<String> savedFiles = new ArrayList<>();

        //trying to save all images
        for (String img : images) {
            String fileName = ImageManager.saveAsFile(img);

            if (fileName == null) {
                //rollback (deleting) previously saved files
                savedFiles.forEach(f -> ImageManager.deleteFile(f));
                return ResponseEntity.status(500)
                    .body(new ErrorMessages("Failed to save images. No images were uploaded."));
            }
            savedFiles.add(fileName);
        }

        //returning filenames of save images
        return ResponseEntity.ok(new PostedImages(savedFiles));
    }
}
