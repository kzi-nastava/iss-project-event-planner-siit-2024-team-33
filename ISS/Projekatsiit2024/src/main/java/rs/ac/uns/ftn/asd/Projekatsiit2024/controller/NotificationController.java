	package rs.ac.uns.ftn.asd.Projekatsiit2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.notification.GetNotificationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.notification.PostNotificationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.notification.PutNotificationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Notification;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.NotificationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Date;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    NotificationService notificationService;
    @Autowired
    AuthentifiedUserRepository userRepo;
    
    @PostMapping
    public ResponseEntity<PostNotificationDTO> sendNotification(@RequestBody PostNotificationDTO postNotificationDTO) {
        try {
            PostNotificationDTO createdNotification = notificationService.createNotification(
                postNotificationDTO.getReceiverId(), 
                postNotificationDTO.getContent()
            );
            return ResponseEntity.ok(createdNotification);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<GetNotificationDTO>> getNotifications() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails principal = (UserDetails) auth.getPrincipal();
		String email = principal.getUsername();
	
		AuthentifiedUser user = userRepo.findByEmail(email);
		
		int receiverId = user.getId();
        

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
    
    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateNotification(
            @PathVariable Integer id,
            @RequestBody PutNotificationDTO putNotificationDTO) {
        if (id <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            Notification notification = notificationService.updateNotification(
                    id,
                    putNotificationDTO.getReceiverId(),
                    putNotificationDTO.getContent(),
                    putNotificationDTO.getDateOfSending(),
                    putNotificationDTO.getIsRead(),
                    putNotificationDTO.getIsSelected());
            
            if (notification != null) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Notification updated successfully.");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid data provided."));
        }
    }



}
