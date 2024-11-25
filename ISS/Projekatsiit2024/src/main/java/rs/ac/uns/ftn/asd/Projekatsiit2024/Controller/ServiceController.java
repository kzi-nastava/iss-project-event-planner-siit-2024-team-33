package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import java.security.Provider.Service;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.GetServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.PostServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.ServiceFilterDTO;


@RestController
@RequestMapping("/services")
public class ServiceController {
	
	
	@GetMapping("/details")
	public ResponseEntity<GetServiceDTO> GetDetails(@RequestAttribute Integer id) {
		return ResponseEntity.ok(new GetServiceDTO(id));
	}
	
	@GetMapping("/list")
	public ResponseEntity<List<GetServiceDTO>> GetServiceList(@RequestBody ServiceFilterDTO FilterParameters){
		//TODO: Apply filter and return service list
		return ResponseEntity.ok(null);
	}
	
	@PostMapping
	public ResponseEntity<GetServiceDTO> PostService(@RequestBody PostServiceDTO data){
		//TODO: Edit Service and return it's details
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/favorites")
	public ResponseEntity<GetServiceDTO> AddToFavorites(@RequestAttribute Integer OfferID){
		return ResponseEntity.ok(null);
	}
	
	@PutMapping
	public ResponseEntity<GetServiceDTO> PutService(@RequestBody PostServiceDTO data){
		//TODO: Edit Service and return it's details
		return ResponseEntity.ok(null);
	}
	
	@DeleteMapping
	public ResponseEntity<GetServiceDTO> DeleteService(@RequestAttribute Integer id){
		return ResponseEntity.ok(null);
	}
	
	@DeleteMapping("/favorites")
	public ResponseEntity<GetServiceDTO> DeleteFromFavorites(@RequestAttribute Integer OfferID){
		return ResponseEntity.ok(null);
		}
}
