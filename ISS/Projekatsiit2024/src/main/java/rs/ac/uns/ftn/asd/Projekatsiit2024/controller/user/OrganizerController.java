package rs.ac.uns.ftn.asd.Projekatsiit2024.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.user.OrganizerService;

@RestController
@RequestMapping("api/organizers")
public class OrganizerController {
	
	@Autowired
	OrganizerService organizerService;
	
	/*@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreatedOrganizerDTO> createOrganizer(@RequestBody CreateOrganizerDTO organizerDTO) throws UserCreationException {
		
		Organizer organizer = organizerService.createOrganizer(organizerDTO);
		CreatedOrganizerDTO savedOrganizer = new CreatedOrganizerDTO(organizer);

		return new ResponseEntity<CreatedOrganizerDTO>(savedOrganizer, HttpStatus.CREATED);
	}*/
	
	/*@ExceptionHandler(UserCreationException.class)
	public ResponseEntity<String> handleOrganizerCreationException(UserCreationException ex) {
        //bad request
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }*/
}
