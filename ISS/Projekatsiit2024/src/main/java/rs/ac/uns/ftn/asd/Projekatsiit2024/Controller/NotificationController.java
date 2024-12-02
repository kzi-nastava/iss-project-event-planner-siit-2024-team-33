package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.NotificationService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.notification.GetNotificationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.notification.PostNotificationDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

	@Autowired
	NotificationService notificationService;
	
    @PostMapping
    public ResponseEntity<String> sendNotification(@RequestBody PostNotificationDTO postNotificationDTO) {
        return ResponseEntity.ok("Aura.");
    }

    @GetMapping
    public ResponseEntity<List<GetNotificationDTO>> getNotifications(@RequestAttribute int receiverId) {
        if (receiverId <= 0) {
            return ResponseEntity.status(403).build();
        }

        List<GetNotificationDTO> notifications = new ArrayList<>();
        notifications.add(new GetNotificationDTO());
        notifications.add(new GetNotificationDTO());

        return ResponseEntity.ok(notifications);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable Integer id) {
        if (id <= 0) {
            return ResponseEntity.notFound().build(); 
        }

        return ResponseEntity.ok("Aura.");
    }
}
