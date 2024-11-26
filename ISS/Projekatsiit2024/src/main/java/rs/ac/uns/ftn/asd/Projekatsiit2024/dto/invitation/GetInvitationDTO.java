package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.invitation;

import java.time.LocalDateTime;

public class GetInvitationDTO {
    private int invitationId;
    private String text;
    private LocalDateTime date;
    private String inviterName;
    private String eventName;
    private String recipientEmail;

    public GetInvitationDTO() {
        super();
    }

}

