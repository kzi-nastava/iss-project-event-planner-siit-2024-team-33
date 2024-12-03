package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.ServiceService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.offerService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.GetServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.PostServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.PutServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.ServiceFilterDTO;


@RestController
@RequestMapping("/api/services")
public class ServiceController {
	
	@Autowired
	private ServiceService serviceService;
	@Autowired
	private offerService offerService;
	
	
	@GetMapping("/{id}")
	public ResponseEntity<GetServiceDTO> getDetails(@PathVariable Integer id) {
		Service s1 = new Service();
		
		if (s1 == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(new GetServiceDTO(s1));
	}
	
	@PostMapping
	public ResponseEntity createService(@RequestBody PostServiceDTO data){
		try {
			Service s;
			if(data.categoryID != null)
				s = serviceService.create(data.categoryID, data.name, data.description, data.price, data.discount, data.pictures, data.providerID, data.reservationInHours, data.cancellationInHours, data.isAutomatic, data.minDurationInMins, data.maxDurationInMins, data.validEventTypeIDs);
			else
				s = serviceService.createWithCategory(data.categoryName, data.categoryDescription, data.name, data.description, data.price, data.discount, data.pictures, data.providerID, data.reservationInHours, data.cancellationInHours, data.isAutomatic, data.minDurationInMins, data.maxDurationInMins, data.validEventTypeIDs);
			
			return ResponseEntity.ok(new GetServiceDTO(s));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PutMapping("/{offerID}")
	public ResponseEntity editService(@PathVariable Integer offerID, @RequestBody PutServiceDTO data){
		try {
			Service s;
			s = serviceService.editService(offerID, data.name, data.description, data.price, data.discount, data.pictures, data.reservationInHours, data.cancellationInHours, data.isAutomatic, data.minDurationInMins, data.maxDurationInMins, data.validEventTypeIDs);			
			return ResponseEntity.ok(new GetServiceDTO(s));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<GetServiceDTO> deleteService(@RequestAttribute Integer id){
		return ResponseEntity.ok().build();
	}
}
