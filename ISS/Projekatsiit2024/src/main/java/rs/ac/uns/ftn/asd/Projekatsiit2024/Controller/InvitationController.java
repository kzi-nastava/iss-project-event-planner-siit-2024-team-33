package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.invitation.GetInvitationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.invitation.PostInvitationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Invitation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.authentifiedUserService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.invitationService;

import java.net.http.HttpRequest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events/{eventID}/invitations")
public class InvitationController {

	@Autowired
	invitationService invitationService;
	@Autowired
	authentifiedUserService AUS;
	@Autowired
	AuthentifiedUserRepository AUR;
	@Autowired
	EventRepository eventRepo;
    
	@PostMapping
    public ResponseEntity<HttpStatus> createInvitations(@PathVariable Integer eventID, @RequestBody PostInvitationDTO postInvitationDTO) {
    	Optional<AuthentifiedUser> user = AUR.findById(postInvitationDTO.getInviterId());
        
    	if(user.isEmpty()) {
    		throw new IllegalArgumentException("");
    	}
    	AuthentifiedUser userReal = user.get();
    	
    	try {
            invitationService.createInvitations(
                    eventID,
                    postInvitationDTO.getEmailAddresses(),
                    postInvitationDTO.getMessage(),
                    new Date(System.currentTimeMillis()),
                    postInvitationDTO.getInviterId(),
                    userReal.getEmail(),
                    userReal.getPassword()
                );
    	}catch(IllegalArgumentException e) {
    		// Myb add sumthn idk
    	}
    	return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<GetInvitationDTO>> getInvitationsForEvent(@PathVariable Integer eventID) {
        if (eventID == null || eventID <= 0) {
            return ResponseEntity.status(403).build();
        }
        
        List<Invitation> invitations = invitationService.getInvitationsByForEvent(eventID);
        List<GetInvitationDTO> invitationDTOs = invitations.stream()
                .map(GetInvitationDTO::new)
                .toList();
        
        return ResponseEntity.ok(invitationDTOs);
    }

    @GetMapping("/{invitationId}")
    public ResponseEntity<GetInvitationDTO> getInvitationById(@PathVariable Integer invitationId) {
    	Invitation invitation = invitationService.getInvitationById(invitationId);
    	
    	GetInvitationDTO dto = new GetInvitationDTO(invitation);
    	
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{invitationId}")
    public ResponseEntity<HttpStatus> deleteInvitation(@PathVariable Integer invitationId) {
    	invitationService.deleteInvitationById(invitationId);
    	return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }
}
