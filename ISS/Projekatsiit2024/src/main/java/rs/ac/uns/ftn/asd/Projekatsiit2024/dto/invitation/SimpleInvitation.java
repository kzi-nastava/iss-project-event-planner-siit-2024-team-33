package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.invitation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.MinimalEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Invitation;

@Getter
@Setter
@NoArgsConstructor
public class SimpleInvitation {

    private int id;
    private MinimalEventDTO event;
    private String status;

    public SimpleInvitation(Invitation invitation) {
        this.id = invitation.getId();
        this.event = new MinimalEventDTO(invitation.getEvent());
        this.status = invitation.getStatus().name();
    }
}
