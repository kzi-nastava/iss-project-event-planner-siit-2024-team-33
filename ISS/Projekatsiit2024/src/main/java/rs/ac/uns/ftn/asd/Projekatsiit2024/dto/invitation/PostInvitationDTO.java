package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.invitation;

import java.sql.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.EventType;
@Setter
@Getter
public class PostInvitationDTO {
    private List<String> emailAddresses;
    private String message;
    private Integer eventId;
    
}
