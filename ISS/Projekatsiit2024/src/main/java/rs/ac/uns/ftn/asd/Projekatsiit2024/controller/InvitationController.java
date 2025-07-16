package rs.ac.uns.ftn.asd.Projekatsiit2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.invitation.GetInvitationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.invitation.PostInvitationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Invitation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.invitation.SimpleInvitation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.invitationService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.user.AuthentifiedUserService;

import java.net.http.HttpRequest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events/invitations")
public class InvitationController {

	@Autowired
	invitationService invitationService;
	@Autowired
	AuthentifiedUserService AUS;
	@Autowired
	AuthentifiedUserRepository userRepo;
	@Autowired
	EventRepository eventRepo;
    
	@PostMapping()
	public ResponseEntity<HttpStatus> createInvitations( @RequestBody PostInvitationDTO postInvitationDTO) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String email = userDetails.getUsername();
		
		AuthentifiedUser userInviter = userRepo.findByEmail(email);

        try {
            invitationService.createInvitations(
                    postInvitationDTO,
                    userInviter,
                    new Date(System.currentTimeMillis())
            );
        } catch (IllegalArgumentException e) {
            return (ResponseEntity<HttpStatus>) ResponseEntity.badRequest();
        } catch (Exception e) {
            return (ResponseEntity<HttpStatus>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }
	
	@PostMapping("/accept/{id}")
    public ResponseEntity<HttpStatus> acceptInvitation(@PathVariable Integer id) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        AuthentifiedUser user = userRepo.findByEmail(email);

        try {
            invitationService.acceptInvitation(id, user);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/deny/{id}")
    public ResponseEntity<HttpStatus> denyInvitation(@PathVariable Integer id) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        AuthentifiedUser user = userRepo.findByEmail(email);

        try {
            invitationService.denyInvitation(id, user);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

	@GetMapping("/pending")
	public ResponseEntity<List<SimpleInvitation>> getMyPendingInvitations() {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    UserDetails userDetails = (UserDetails) auth.getPrincipal();
	    String email = userDetails.getUsername();

	    AuthentifiedUser user = userRepo.findByEmail(email);

	    List<Invitation> invitations = invitationService.getInvitationForUser(user);
	    List<SimpleInvitation> simpleDTOs = invitations.stream()
	            .map(SimpleInvitation::new)
	            .toList();

	    return ResponseEntity.ok(simpleDTOs);
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
