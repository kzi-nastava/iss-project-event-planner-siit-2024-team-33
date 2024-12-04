package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.io.Console;
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

import jakarta.persistence.EntityNotFoundException;
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
	
	@GetMapping(value = "/top5", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<GetServiceDTO>> GetTop5Services() {
	    List<GetServiceDTO> services = new ArrayList<>();

	    for (int i = 1; i <= 5; i++) {
	        Service mockService = new Service();
	        GetServiceDTO service = new GetServiceDTO(mockService);

//	        service.Name = "Top Service " + i;
//	        service.Description = "Description for top service " + i;
//	        service.Price = 100.0 + i;
//	        service.Discount = 10.0 + i;
//	        service.Pictures = List.of("service_image" + i + ".jpg");
//	        service.ValidEventCategories = List.of(i, i + 1, i + 2);
//	        service.AvgRating = 4.5 + (i * 0.1);
//	        service.isVisible = true;
//	        service.isAvailable = true;
//	        service.ReservationInHours = 24 + i;
//	        service.CancellationInHours = 12 + i;
//	        service.isAutomatic = (i % 2 == 0);
//	        service.MinLengthInMins = 30;
//	        service.MaxLengthInMins = 120;

	        services.add(service);
	    }

	    return ResponseEntity.ok(services);
	}

	
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
	public ResponseEntity getDetails(@PathVariable Integer id) {
		try {
			Service s = serviceService.get(id);
			return ResponseEntity.ok(new GetServiceDTO(s));
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(404).body(e.toString());
		}
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
			System.out.println("------------------------------------------------------------SERVICE PUT REQUEST GOTTEN");
			Service s;
			s = serviceService.editService(offerID, data.name, data.description, data.price, data.discount, data.picturesDataURI, data.reservationInHours, data.cancellationInHours, data.isAutomatic, data.minDurationInMins, data.maxDurationInMins, data.validEventTypeIDs);
			System.out.println("SERVICE PUT REQUEST HANDLED");
			return ResponseEntity.ok(new GetServiceDTO(s));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<GetServiceDTO> deleteService(@RequestAttribute Integer id){
		return ResponseEntity.ok().build();
	}
}
