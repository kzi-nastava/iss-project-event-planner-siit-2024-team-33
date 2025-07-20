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
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.MessageRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
	@Autowired
	private AuthentifiedUserRepository userRepo;
	
	@Autowired
	private MessageRepository messageRepo;

	@GetMapping("/{email}")
	public List<MessageDTO> getMessagesWithUser(@PathVariable String email, @AuthenticationPrincipal UserPrincipal principal) {
	    AuthentifiedUser currentUser = userRepo.findByEmailOptional(principal.getUsername()).orElseThrow();
	    AuthentifiedUser otherUser = userRepo.findByEmailOptional(email).orElseThrow();

	    List<Message> messages = messageRepo.findConversation(currentUser, otherUser);
	    return messages.stream().map(m -> new MessageDTO(
	        m.Sender.getEmail(),
	        m.Content,
	        m.TimeOfSending
	    )).toList();
	}
	
	@GetMapping("/all")
	public List<ChatContactDTO> getContacts(@AuthenticationPrincipal UserPrincipal principal){
	    AuthentifiedUser currentUser = userRepo.findByEmailOptional(principal.getUsername()).orElseThrow();
	    Set<AuthentifiedUser> contacts = new HashSet<>();
	    contacts.addAll(messageRepo.findRecipients(currentUser));
	    contacts.addAll(messageRepo.findSenders(currentUser));
		return contacts.stream().map( user -> new ChatContactDTO(user.getEmail(), user.getName(), currentUser.getBlockedUsers().contains(user))).toList();
	}
}
