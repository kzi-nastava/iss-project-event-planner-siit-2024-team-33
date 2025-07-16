package rs.ac.uns.ftn.asd.Projekatsiit2024.controller;

import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.chat.MessageDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.chat.PostMessageDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.MessageRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;

@Controller
public class ChatWebSocketController {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private AuthentifiedUserRepository userRepository;
	
	@Autowired
	private MessageRepository messageRepository;
	
	@MessageMapping("/chat.send")
	public void sendMessage(PostMessageDTO message, Principal principal) {
		AuthentifiedUser sender = userRepository.findByEmailOptional(principal.getName()).orElseThrow();
		AuthentifiedUser recipient = userRepository.findByEmailOptional(message.recipientEmail).orElseThrow();
		
		rs.ac.uns.ftn.asd.Projekatsiit2024.model.Message messageModel = new rs.ac.uns.ftn.asd.Projekatsiit2024.model.Message();
		messageModel.Sender = sender;
		messageModel.Recipient = recipient;
		messageModel.Content = message.message;
		messageModel.TimeOfSending = new Date();
		
		messageRepository.save(messageModel);
		
		messagingTemplate.convertAndSendToUser(
				recipient.getEmail(),
				"/queue/messages",
				new MessageDTO(sender.getEmail(), message.message, messageModel.TimeOfSending)
				);
	}
}
