package rs.ac.uns.ftn.asd.Projekatsiit2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.chat.ChatContactDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.chat.GetChatDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.chat.MessageDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.chat.PostMessageDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Message;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.MessageRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
	@Autowired
	private AuthentifiedUserRepository userRepo;
	
	@Autowired
	private MessageRepository messageRepo;

	@GetMapping("/{id}")
	public List<MessageDTO> getMessagesWithUser(@PathVariable Integer id, @AuthenticationPrincipal Principal principal) {
	    AuthentifiedUser currentUser = userRepo.findByEmailOptional(principal.getName()).orElseThrow();
	    AuthentifiedUser otherUser = userRepo.findById(id).orElseThrow();

	    List<Message> messages = messageRepo.findConversation(currentUser, otherUser);
	    return messages.stream().map(m -> new MessageDTO(
	        m.Content,
	        m.Sender.getEmail(),
	        m.TimeOfSending
	    )).toList();
	}
	
	@GetMapping("/all")
	public List<ChatContactDTO> getContacts(@AuthenticationPrincipal Principal principal){
	    AuthentifiedUser currentUser = userRepo.findByEmailOptional(principal.getName()).orElseThrow();
		List<AuthentifiedUser> contacts = messageRepo.findAllContacts(currentUser);
		return contacts.stream().map( user -> new ChatContactDTO(user.getEmail(), user.getName())).toList();
	}
}
