package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.chat;

import java.time.LocalDateTime;

import rs.ac.uns.ftn.asd.Projekatsiit2024.utils.ImageManager;

public class ChatContactDTO {
	public String email;
	public String username;
	public Boolean isBlocked;
	public String imageURI;
	
	public ChatContactDTO(String mail, String name, Boolean isBlocked) {
		email = mail;
		username = name;
		this.isBlocked = isBlocked;
		this.imageURI = ImageManager.loadAsDataURI("7694193900_3d268cfc80.jpg");
	}
	
	public ChatContactDTO(String mail, String name, Boolean isBlocked, String imageFile) {
		email = mail;
		username = name;
		this.isBlocked = isBlocked;
		if(imageFile != null && !imageFile.isEmpty())
			this.imageURI = ImageManager.loadAsDataURI(imageFile);
		else
			this.imageURI = ImageManager.loadAsDataURI("7694193900_3d268cfc80.jpg");
	}
}
