package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.notification;

import java.sql.Date;

public class GetNotificationDTO {
    private int notificationId;
    private String content;
    private Date dateOfSending;
    private Boolean isRead;

    public GetNotificationDTO() {
        super();
    }
    
}
