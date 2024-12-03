package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.notification;

import java.sql.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.EventType;
@Setter
@Getter
public class GetNotificationDTO {
    private String content;
    private Date dateOfSending;
    private Boolean isRead;

    public GetNotificationDTO() {
        super();
    }
    
}
