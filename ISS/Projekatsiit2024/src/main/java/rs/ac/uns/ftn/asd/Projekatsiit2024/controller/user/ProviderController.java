package rs.ac.uns.ftn.asd.Projekatsiit2024.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.provider.ProviderDetailsDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.rating.GetProviderRatingDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.CreateProviderDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.CreatedProviderDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.UserCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.user.ProviderService;


@RestController
@RequestMapping("api/providers")
public class ProviderController {
	
	@Autowired
	ProviderService providerService = new ProviderService();
	
	@GetMapping("/{id}")
	public ResponseEntity<ProviderDetailsDTO> getProvider(@PathVariable Integer id) {
		return ResponseEntity.ok(new ProviderDetailsDTO(providerService.get(id)));
	}
	
	@GetMapping("/{id}/ratings")
	public ResponseEntity<List<GetProviderRatingDTO>> getRatings(@PathVariable Integer id){
		Provider p = providerService.get(id);
		return ResponseEntity.ok(p.getRatings().stream().map(pr -> new GetProviderRatingDTO(pr)).toList());
	}
	
	/*@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreatedProviderDTO> createProvider(@RequestBody CreateProviderDTO providerDTO) throws UserCreationException {
		
		Provider provider = providerService.createProvider(providerDTO);
		CreatedProviderDTO savedProvider = new CreatedProviderDTO(provider);

		return new ResponseEntity<CreatedProviderDTO>(savedProvider, HttpStatus.CREATED);
	}*/
	
	@ExceptionHandler(UserCreationException.class)
	public ResponseEntity<String> handleProviderCreationException(UserCreationException ex) {
        //bad request
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
	
	@ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data not found");
    }
}
