package rs.ac.uns.ftn.asd.Projekatsiit2024.Common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class ImageManager {
	static private String IMAGE_DIR = "src/main/resources/images";
	
	static public String loadAsDataURI(String imageName) {
		try {
			File imgFile = new File(IMAGE_DIR + "/" + imageName);
	        byte[] imageBytes = Files.readAllBytes(imgFile.toPath());
	        String base64Image = new String(Base64Coder.encode(imageBytes));
	        String mimeType = Files.probeContentType(Paths.get(IMAGE_DIR + "/" + imageName));
	        String dataUri = "data:" + mimeType + ";base64," + base64Image;
	        return dataUri;
		} catch (Exception e) {
			return null;
		}
	}
	
	static private String getUnusedFilename(String extension) {
		String filename;
		File f;
		do {
			filename = UUID.randomUUID().toString() + "." + extension;
			f = new File(IMAGE_DIR + "/" + filename);
		} while (f.isFile());
		return filename;
	}
	
	//Returns the name of the new file
	static public String saveAsFile(String base64Image) {
		System.out.println("IMAGEEEEEEEE " + base64Image);
		String base64Data = base64Image.split(",")[1];
        byte[] imageBytes = Base64.getDecoder().decode(base64Data);

        // Image extension (png or jpg or whatever)
        String extension = base64Image.split(";")[0].split("/")[1];
        String fileName = getUnusedFilename(extension);

        File f = new File(IMAGE_DIR + "/" + fileName);
        f.getParentFile().mkdirs();
        try {
			f.createNewFile();
	        FileOutputStream fos = new FileOutputStream(f);
	        fos.write(imageBytes);
		} catch (IOException e) {
			return null;
		}
        System.out.println("SUCCESSFUL IMAGE WRITE");

        return fileName;
	}
}
