package rs.ac.uns.ftn.asd.Projekatsiit2024.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

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
	
	
	//Returns the name of the new file
	static public String saveAsFile(String base64Image) {
		if (!isValidImage(base64Image)) return null;

		try {
			String base64Data = base64Image.split(",")[1];
			byte[] imageBytes = Base64.getDecoder().decode(base64Data);

			String extension = base64Image.split(";")[0].split("/")[1];
			String fileName = getUnusedFilename(extension);

			File f = new File(IMAGE_DIR + "/" + fileName);
			f.getParentFile().mkdirs();

			try (FileOutputStream fos = new FileOutputStream(f)) {
				fos.write(imageBytes);
			}

			return fileName;
		} catch (IOException e) {
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
	
	
	public static boolean isValidImage(String base64Image) {
		try {
			if (base64Image == null || !base64Image.contains(",")) return false;

			String[] parts = base64Image.split(",");
			String metadata = parts[0];
			String base64Data = parts[1];

			String mimeType = metadata.split(":")[1].split(";")[0];
			String extension = mimeType.split("/")[1];

			List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/webp", "image/gif");
			List<String> allowedExtensions = List.of("jpeg", "jpg", "png", "webp", "gif");

			if (!allowedTypes.contains(mimeType) || !allowedExtensions.contains(extension)) {
				return false;
			}

			byte[] imageBytes = Base64.getDecoder().decode(base64Data);
			int maxSizeBytes = 2 * 1024 * 1024;
			return imageBytes.length <= maxSizeBytes;

		} catch (Exception e) {
			return false;
		}
	}
	
	
	public static boolean deleteFile(String filename) {
	    if (filename == null || filename.trim().isEmpty()) {
	        return false;
	    }

	    File f = new File(IMAGE_DIR + "/" + filename);

	    if (!f.exists()) {
	        return false;
	    }

	    return f.delete();
	}
}
