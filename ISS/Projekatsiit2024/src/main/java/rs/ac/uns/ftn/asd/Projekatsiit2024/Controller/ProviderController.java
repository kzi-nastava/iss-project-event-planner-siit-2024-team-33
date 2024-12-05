package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Exception.UserCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.OrganizerService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.ProviderService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.CreateOrganizerDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.CreateProviderDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.CreatedOrganizerDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.CreatedProviderDTO;

@RestController
@RequestMapping("api/providers")
public class ProviderController {
	
	@Autowired
	ProviderService providerService = new ProviderService();
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreatedProviderDTO> createProvider(@RequestBody CreateProviderDTO providerDTO) throws UserCreationException {
		
		Provider provider = providerService.createProvider(providerDTO);
		CreatedProviderDTO savedProvider = new CreatedProviderDTO(provider);

		return new ResponseEntity<CreatedProviderDTO>(savedProvider, HttpStatus.CREATED);
	}
	
	@ExceptionHandler(UserCreationException.class)
	public ResponseEntity<String> handleProviderCreationException(UserCreationException ex) {
        //bad request
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
}
