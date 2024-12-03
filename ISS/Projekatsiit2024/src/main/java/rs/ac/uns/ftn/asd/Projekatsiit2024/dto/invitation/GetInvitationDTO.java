package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.invitation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Invitation;

@Setter
@Getter
public class GetInvitationDTO {
    private int invitationId;
    private String text;
    private LocalDate date;
    private String inviterName;
    private String eventName;
    private String recipientEmail;

    public GetInvitationDTO(Invitation invitation) {
        this.invitationId = invitation.getId();
        this.text = invitation.getText();
        this.date = invitation.getDate().toLocalDate();
        this.inviterName = invitation.getInviter().getName();
        this.eventName = invitation.getEvent().getName();
        this.recipientEmail = invitation.getInviter().getEmail();
    }
    
    public GetInvitationDTO() {
        super();
    }
}
