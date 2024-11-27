package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.notification.GetNotificationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.notification.PostNotificationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.notification.InvitationNotificationDTO;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {


    @PostMapping
    public ResponseEntity<String> sendNotification(@RequestBody PostNotificationDTO postNotificationDTO) {
        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<List<GetNotificationDTO>> getNotifications(@RequestAttribute int receiverId) {
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable Integer id) {
        return ResponseEntity.ok(null);
    }
    
}
