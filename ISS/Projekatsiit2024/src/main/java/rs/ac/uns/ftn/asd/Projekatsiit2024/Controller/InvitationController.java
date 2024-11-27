package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.invitation.GetInvitationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.invitation.PostInvitationDTO;

import java.util.List;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    @PostMapping
    public ResponseEntity<String> createInvitations(@RequestBody PostInvitationDTO postInvitationDTO) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<GetInvitationDTO>> getInvitationsForEvent(@RequestAttribute int eventId) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{invitationId}")
    public ResponseEntity<GetInvitationDTO> getInvitationById(@RequestAttribute int invitationId) {
        return ResponseEntity.ok(null);
    }
    
    @DeleteMapping
    public ResponseEntity<String> deleteInvitation(@RequestParam int invitationId) {
        return ResponseEntity.ok(null);
    }
}
