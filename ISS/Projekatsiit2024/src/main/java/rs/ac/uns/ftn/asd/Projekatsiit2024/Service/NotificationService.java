package rs.ac.uns.ftn.asd.Projekatsiit2024.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Notification;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.NotificationRepository;


@Service
public class NotificationService {

    @Autowired
    private AuthentifiedUserRepository userRepo;
    
    @Autowired
    private NotificationRepository notifRepo;
    
    
    public void createNotification(Integer receiverId, String content) {
        Optional<AuthentifiedUser> receiverOptional = userRepo.findById(receiverId);

        if (receiverOptional.isEmpty()) {
            throw new IllegalArgumentException("Receiver not found with the provided ID.");
        }

        AuthentifiedUser receiver = receiverOptional.get();

        Notification notification = new Notification();
        notification.setContent(content);
        notification.setTimeOfSending(Date.valueOf(LocalDate.now()));
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
    
    //TODO: Make mute and unmute notifications
}
