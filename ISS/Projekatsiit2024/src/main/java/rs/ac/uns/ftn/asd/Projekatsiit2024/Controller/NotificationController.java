	package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Notification;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.NotificationService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.notification.GetNotificationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.notification.PostNotificationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.notification.PutNotificationDTO;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @PostMapping
    public ResponseEntity<String> sendNotification(@RequestBody PostNotificationDTO postNotificationDTO) {
        try {
            notificationService.createNotification(postNotificationDTO.getReceiverId(), postNotificationDTO.getContent());
            return ResponseEntity.ok("");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("");
        }
    }

    @GetMapping
    public ResponseEntity<List<GetNotificationDTO>> getNotifications(@RequestParam int receiverId) {
        if (receiverId <= 0) {
            return ResponseEntity.status(403).build();
        }

        List<GetNotificationDTO> notificationsDTO = new ArrayList<>();
        List<Notification> notifications = notificationService.getAllNotificationsForUser(receiverId);
        
        for (Notification notification : notifications) {
            GetNotificationDTO dto = new GetNotificationDTO();
            dto.setIndex(notification.getId());
            dto.setContent(notification.getContent());
            dto.setDateOfSending(notification.getTimeOfSending().toString());
            dto.setIsRead(notification.getIsRead());
            dto.setIsSelected(notification.getIsSelected());
            notificationsDTO.add(dto);
        }

        return ResponseEntity.ok(notificationsDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable Integer id) {
        if (id <= 0) {
            return ResponseEntity.notFound().build();
        }

        notificationService.deleteNotification(id);
        return ResponseEntity.ok("Notification deleted successfully.");
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<String> updateNotification(@RequestParam Integer idd, 
    		@RequestParam(value="id", required=false) Integer id,
    		@RequestParam(value="receiverId", required=false) Integer receiverId,
    		@RequestParam(value="content", required=false) String content,
    		@RequestParam(value="dateOfSending", required=false) String dateOfSending,
    		@RequestParam(value="isRead", required=false) Boolean isRead,
    		@RequestParam(value="isSelected", required=false) Boolean isSelected) {
        if (id <= 0) {
            return ResponseEntity.notFound().build();
        }

        try {
            Notification notification = notificationService.updateNotification(id, receiverId, content, dateOfSending, isRead, isSelected);
            if (notification != null) {
                return ResponseEntity.ok("Notification updated successfully.");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid data provided.");
        }
    }
}
