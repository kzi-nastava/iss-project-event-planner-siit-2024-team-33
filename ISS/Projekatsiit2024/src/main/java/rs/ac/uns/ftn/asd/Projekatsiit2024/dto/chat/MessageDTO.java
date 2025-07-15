package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.chat;

import java.util.Date;

public class MessageDTO {
	public String senderEmail;
	public String message;
	public Date sendDate;
	
	public MessageDTO(String sender, String content, Date date) {
		senderEmail = sender;
		message = content;
		sendDate = date;
	}
}
