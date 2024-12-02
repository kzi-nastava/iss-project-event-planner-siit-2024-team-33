package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.invitation.GetInvitationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.invitation.PostInvitationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.invitationService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/events/{eventID}/invitations")
public class InvitationController {

	@Autowired
	invitationService invitationService;
	
    @PostMapping
    public ResponseEntity<String> createInvitations(@PathVariable Integer eventID, @RequestBody PostInvitationDTO postInvitationDTO) {
        if (eventID == null || eventID <= 0) {
            return ResponseEntity.status(403).body("Invalid event ID.");
        }

        return ResponseEntity.ok("Aura.");
    }

    @GetMapping
    public ResponseEntity<List<GetInvitationDTO>> getInvitationsForEvent(@PathVariable Integer eventID) {
        if (eventID == null || eventID <= 0) {
            return ResponseEntity.status(403).build();
        }

        List<GetInvitationDTO> invitations = new ArrayList<>();
        invitations.add(new GetInvitationDTO());
        invitations.add(new GetInvitationDTO());

        return ResponseEntity.ok(invitations);
    }

    @GetMapping("/{invitationId}")
    public ResponseEntity<GetInvitationDTO> getInvitationById(@PathVariable Integer invitationId) {

        if (invitationId == null || invitationId <= 0) {
            return ResponseEntity.notFound().build();
        }

        GetInvitationDTO invitation = new GetInvitationDTO();
        return ResponseEntity.ok(invitation);
    }

    @DeleteMapping("/{invitationId}")
    public ResponseEntity<String> deleteInvitation(@PathVariable Integer invitationId) {
        if (invitationId == null || invitationId <= 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Aura.");
    }
}
