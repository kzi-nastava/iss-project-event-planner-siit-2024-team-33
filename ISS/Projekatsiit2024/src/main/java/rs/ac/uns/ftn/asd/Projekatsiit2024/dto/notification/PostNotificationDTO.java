package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.notification;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
@Setter
@Getter
public class PostNotificationDTO {
    private int receiverId;
    private String content;
    private Date dateOfSending;

}
