package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import java.security.Provider.Service;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/api/services")
public class ServiceController {

	@GetMapping
	public ResponseEntity<List<GetServiceDTO>> GetServices(@RequestBody ServiceFilterDTO FilterParameters){
		//TODO: Apply filter and return service list
		return ResponseEntity.ok(null);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<GetServiceDTO> GetDetails(@PathVariable Integer id) {
		return ResponseEntity.ok(new GetServiceDTO(id));
	}
	
	@PostMapping
	public ResponseEntity<GetServiceDTO> CreateService(@RequestBody PostServiceDTO data){
		//TODO: Edit Service and return it's details
		return ResponseEntity.ok(null);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<GetServiceDTO> ReplaceService(@PathVariable Integer id, @RequestBody PostServiceDTO data){
		//TODO: Edit Service and return it's details
		return ResponseEntity.ok(null);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<GetServiceDTO> DeleteService(@RequestAttribute Integer id){
		return ResponseEntity.ok(null);
	}
}
