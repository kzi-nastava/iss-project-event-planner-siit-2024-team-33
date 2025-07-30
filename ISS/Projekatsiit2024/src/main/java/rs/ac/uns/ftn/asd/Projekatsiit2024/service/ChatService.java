package rs.ac.uns.ftn.asd.Projekatsiit2024.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.chat.MessageDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.chat.PostMessageDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.MessageRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;

@Service
public class ChatService {
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private AuthentifiedUserRepository userRepository;
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Transactional
	public void handleSendMessage(String senderMail, String recipientMail, PostMessageDTO message) {
		AuthentifiedUser sender = userRepository.findByEmailOptional(senderMail).orElseThrow();
		AuthentifiedUser recipient = userRepository.findByEmailOptional(recipientMail).orElseThrow();
		
		if(recipient.getBlockedUsers().contains(sender) || sender.getBlockedUsers().contains(recipient))
			return;
		
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
