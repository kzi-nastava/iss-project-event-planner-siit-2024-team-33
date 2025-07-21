package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.chat;

import java.time.LocalDateTime;

public class ChatContactDTO {
	public String email;
	public String username;
	public Boolean isBlocked;
	
	public ChatContactDTO(String mail, String name, Boolean isBlocked) {
		email = mail;
		username = name;
		this.isBlocked = isBlocked;
	}
}
