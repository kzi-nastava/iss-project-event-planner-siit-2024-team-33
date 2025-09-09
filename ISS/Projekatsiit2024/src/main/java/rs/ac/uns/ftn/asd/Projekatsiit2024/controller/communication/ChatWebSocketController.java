package rs.ac.uns.ftn.asd.Projekatsiit2024.controller.communication;

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
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.communication.MessageRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.communication.ChatService;

@Controller
public class ChatWebSocketController {
	@Autowired
	private AuthentifiedUserRepository userRepository;

	@Autowired
	public ChatService chatService;
	
	@MessageMapping("/chat.send")
	public void sendMessage(PostMessageDTO message, Principal principal) {
		chatService.handleSendMessage(principal.getName(), message.recipientEmail, message);
	}
}
