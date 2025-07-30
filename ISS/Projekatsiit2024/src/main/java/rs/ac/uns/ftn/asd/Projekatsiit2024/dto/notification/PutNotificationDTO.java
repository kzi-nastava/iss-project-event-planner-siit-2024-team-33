package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.notification;

import java.sql.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
@Setter
@Getter
public class PutNotificationDTO {
	private Integer notifId;
    private Integer receiverId;
    private String content;
    private String dateOfSending;
    private Boolean isRead;
    private Boolean isSelected;

}
