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
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Invitation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.invitationService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.user.AuthentifiedUserService;

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
	AuthentifiedUserService AUS;
	@Autowired
	AuthentifiedUserRepository userRepo;
	@Autowired
	EventRepository eventRepo;
    
	@PostMapping("/{inviterId}")
	public ResponseEntity<HttpStatus> createInvitations( @RequestBody PostInvitationDTO postInvitationDTO) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String email = userDetails.getUsername();
		
		AuthentifiedUser userInviter = userRepo.findByEmail(email);
		int id = userInviter.getId();
		
        Optional<AuthentifiedUser> userInvited = userRepo.findById(id);

        if (userInvited .isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        AuthentifiedUser userReal = userInvited.get();

        try {
            invitationService.createInvitations(
                    postInvitationDTO.getEventId(),
                    postInvitationDTO.getEmailAddresses(),
                    postInvitationDTO.getMessage(),
                    new Date(System.currentTimeMillis()),
                    id,
                    userReal.getEmail(),
                    userReal.getPassword()
            );
        } catch (IllegalArgumentException e) {
            return (ResponseEntity<HttpStatus>) ResponseEntity.badRequest();
        } catch (Exception e) {
            return (ResponseEntity<HttpStatus>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
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
