package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.chat.GetChatDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.chat.PostMessageDTO;

@RestController
@RequestMapping("/chat")
public class ChatController {
	@GetMapping
	public ResponseEntity<GetChatDTO> GetChatMessages(@RequestAttribute Integer OtherUserID){
		return ResponseEntity.ok(null);
	}
	
	@PostMapping
	public ResponseEntity<GetChatDTO> PostMessage(@RequestBody PostMessageDTO Message){
		return ResponseEntity.ok(null);
	}
}
