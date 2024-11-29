package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import java.util.ArrayList;
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

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.GetServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.PostServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.ServiceFilterDTO;


@RestController
@RequestMapping("/api/services")
public class ServiceController {

	@GetMapping
	public ResponseEntity<List<GetServiceDTO>> getServices(@RequestBody(required = false) ServiceFilterDTO FilterParameters){
		Service s1 = new Service();
		Service s2 = new Service();
		ArrayList<GetServiceDTO> serviceList = new ArrayList<GetServiceDTO>();
		serviceList.add(new GetServiceDTO(s1));
		serviceList.add(new GetServiceDTO(s2));
		return ResponseEntity.ok(serviceList);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<GetServiceDTO> getDetails(@PathVariable Integer id) {
		Service s1 = new Service();
		
		if (s1 == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(new GetServiceDTO(s1));
	}
	
	@PostMapping
	public ResponseEntity<GetServiceDTO> createService(@RequestBody PostServiceDTO data){
		Service s1 = new Service();
		Boolean error = false;
		
		if(error)
			return ResponseEntity.unprocessableEntity().build();
		
		//TODO: Edit Service and return it's details
		return ResponseEntity.ok(new GetServiceDTO(s1));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<GetServiceDTO> editService(@PathVariable Integer id, @RequestBody PostServiceDTO data){
		Service s1 = new Service();
		//TODO: Edit Service and return it's details
		return ResponseEntity.ok(new GetServiceDTO(s1));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<GetServiceDTO> deleteService(@RequestAttribute Integer id){
		return ResponseEntity.ok().build();
	}
}
