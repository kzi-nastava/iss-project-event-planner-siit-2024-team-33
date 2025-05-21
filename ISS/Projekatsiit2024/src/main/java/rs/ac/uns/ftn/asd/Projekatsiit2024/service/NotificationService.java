package rs.ac.uns.ftn.asd.Projekatsiit2024.service;

import java.util.Date;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.notification.PutNotificationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Notification;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.NotificationRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;


@Service
public class NotificationService {

    @Autowired
    private AuthentifiedUserRepository userRepo;
    
    @Autowired
    private NotificationRepository notifRepo;
    
    
    public void createNotification(Integer receiverId, String content) {
        Optional<AuthentifiedUser> receiverOptional = userRepo.findById(receiverId);

        if (receiverOptional.isEmpty()) {
            throw new IllegalArgumentException("");
        }

        AuthentifiedUser receiver = receiverOptional.get();

        Notification notification = new Notification();
        notification.setContent(content);
        notification.setTimeOfSending(new Date());
        notification.setReceiver(receiver);
        notification.setIsRead(false);

        notifRepo.save(notification);
    }
    
    public List<Notification> getAllNotificationsForUser(Integer userId) {
        return notifRepo.findAllByReceiverId(userId);
    }
    
    public void readANotification(Integer userId, Integer notifId) {
    	Optional<AuthentifiedUser> optionalUser = userRepo.findById(userId);
    	Optional<Notification> notificationOptional = notifRepo.findById(notifId);
    	if(optionalUser.isEmpty()) {
    		throw new IllegalArgumentException("");
    	}
    	if(notificationOptional.isEmpty()) {
    		throw new IllegalArgumentException("");
    	}
    	
    	AuthentifiedUser user = optionalUser.get();
    	Notification notif = notificationOptional.get();
    	
    	int idx = user.getNotifications().indexOf(notif); 
    	
    	user.getNotifications().get(idx).setIsRead(true);
    	
    	userRepo.save(user);
    }
    
    public void unreadANotification(Integer userId, Integer notifId) {
    	Optional<AuthentifiedUser> optionalUser = userRepo.findById(userId);
    	Optional<Notification> notificationOptional = notifRepo.findById(notifId);
    	if(optionalUser.isEmpty()) {
    		throw new IllegalArgumentException("");
    	}
    	if(notificationOptional.isEmpty()) {
    		throw new IllegalArgumentException("");
    	}
    	
    	AuthentifiedUser user = optionalUser.get();
    	Notification notif = notificationOptional.get();
    	
    	int idx = user.getNotifications().indexOf(notif); 
    	
    	user.getNotifications().get(idx).setIsRead(false);
    	
    	userRepo.save(user);
    	
    }
    
    public void deleteNotification(Integer notifId) {
        Optional<Notification> notificationOptional = notifRepo.findById(notifId);
        if (notificationOptional.isEmpty()) {
            throw new IllegalArgumentException("");
        }
        notifRepo.delete(notificationOptional.get());
    }

    
    public void deleteSelectedNotifications(Integer userID) {
    	Optional<AuthentifiedUser> optionalUser= userRepo.findById(userID);
    	if(optionalUser.isEmpty()) {
    		throw new IllegalArgumentException("");
    	}
    	
    	AuthentifiedUser user = optionalUser.get();
    	List<Notification> notifications = user.getNotifications();
    	//Iterator cuzz can't modify a list while iterating over it, ig?
        Iterator<Notification> iterator = notifications.iterator();
        while (iterator.hasNext()) {
            Notification notification = iterator.next();
            if (notification.isRead) {
                iterator.remove();
            }
        }
    	user.setNotifications(notifications);
    	
    	userRepo.save(user);
    }
    
    public Notification updateNotification(Integer id, Integer receiverId, String content, String dateofSending, Boolean isRead, Boolean isSelected ) {
        Optional<Notification> optionalNotification = notifRepo.findById(id);
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            if (content != null) {
                notification.setContent(content);
            }
            if (isRead != null) {
                notification.setIsRead(isRead);
            }
            if (isSelected!=null) {
            	notification.setIsSelected(isSelected);
            }

            notification.setTimeOfSending(new Date());
            return notifRepo.save(notification);
        }
        return null;
    }
    
    
    //TODO: Make mute and unmute notifications
}
