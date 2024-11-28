package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.chat.GetChatDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.chat.PostMessageDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/chat/{receiverID}")
public class ChatController {

    @GetMapping
    public ResponseEntity<GetChatDTO> GetChatMessages(@PathVariable Integer receiverID) {
        if (receiverID == null || receiverID <= 0) {
            return ResponseEntity.status(403).build();
        }

        // Mock chat data
        List<String> messages = new ArrayList<>();
        messages.add("Hello, how are you?");
        messages.add("I'm good, thank you!");

        GetChatDTO chat = new GetChatDTO();

        return ResponseEntity.ok(chat);
    }

    @PostMapping
    public ResponseEntity<GetChatDTO> PostMessage(@PathVariable Integer receiverID, @RequestBody PostMessageDTO message) {
        if (receiverID == null || receiverID <= 0) {
            return ResponseEntity.status(403).build();
        }

        List<String> updatedMessages = new ArrayList<>();
        updatedMessages.add("Hello, how are you?");
        updatedMessages.add("I'm good, thank you!");
        //Make sure to update this 
        GetChatDTO updatedChat = new GetChatDTO();

        return ResponseEntity.ok(updatedChat);
    }
}
