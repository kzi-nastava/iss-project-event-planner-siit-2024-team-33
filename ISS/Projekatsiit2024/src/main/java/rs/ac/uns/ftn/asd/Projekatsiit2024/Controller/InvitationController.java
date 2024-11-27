package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.invitation.GetInvitationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.invitation.PostInvitationDTO;

import java.util.List;

@RestController
@RequestMapping("/api/events/{eventID}/invitations")
public class InvitationController {

    @PostMapping
    public ResponseEntity<String> createInvitations(@PathVariable Integer eventID, @RequestBody PostInvitationDTO postInvitationDTO) {
        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<List<GetInvitationDTO>> getInvitationsForEvent(@PathVariable Integer eventID) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{invitationId}")
    public ResponseEntity<GetInvitationDTO> getInvitationById(@PathVariable Integer invitationId) {
        return ResponseEntity.ok(null);
    }
    
    @DeleteMapping("/{invitationId}")
    public ResponseEntity<String> deleteInvitation(@PathVariable Integer invitationId) {
        return ResponseEntity.ok(null);
    }
}
