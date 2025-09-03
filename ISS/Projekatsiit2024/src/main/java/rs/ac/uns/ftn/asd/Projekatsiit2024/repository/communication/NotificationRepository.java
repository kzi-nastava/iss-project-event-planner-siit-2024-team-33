package rs.ac.uns.ftn.asd.Projekatsiit2024.repository.communication;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.communication.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer>{
    List<Notification> findAllByReceiverId(Integer receiverId);

}
