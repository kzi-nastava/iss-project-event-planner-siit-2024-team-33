package rs.ac.uns.ftn.asd.Projekatsiit2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.io.Console;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
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
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.GetServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.PostServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.PutServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.ServiceFilterDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.service.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.ServiceService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.OfferService;


@RestController
@RequestMapping("/api/services")
public class ServiceController {
	
	@Autowired
	private ServiceService serviceService;
	@Autowired
	private OfferService offerService;
	
	
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
				s = serviceService.create(data.categoryID, data.name, data.description, data.price, data.discount, data.picturesDataURI, "", data.availability, data.reservationInHours, data.cancellationInHours, data.isAutomatic, data.minDurationInMins, data.maxDurationInMins, data.validEventTypeIDs);
			else
				s = serviceService.createWithCategory(data.categoryName, data.categoryDescription, data.name, data.description, data.price, data.discount, data.picturesDataURI, "", data.availability, data.reservationInHours, data.cancellationInHours, data.isAutomatic, data.minDurationInMins, data.maxDurationInMins, data.validEventTypeIDs);
			
			return ResponseEntity.ok(new GetServiceDTO(s));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (AuthenticationCredentialsNotFoundException e) {
			return ResponseEntity.status(401).body(null);
		} catch (AccessDeniedException e) {
			return ResponseEntity.status(403).body(null);
		}
		
	}
	
	@PutMapping("/{offerID}")
	public ResponseEntity editService(@PathVariable Integer offerID, @RequestBody PutServiceDTO data){
		try {
			Service s;
			s = serviceService.editService(offerID, data.name, data.description, data.price, data.discount, data.picturesDataURI, data.reservationInHours, data.cancellationInHours, "", data.availability, data.isAutomatic, data.minDurationInMins, data.maxDurationInMins, data.validEventTypeIDs);
			return ResponseEntity.ok(new GetServiceDTO(s));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (AuthenticationCredentialsNotFoundException e) {
			return ResponseEntity.status(401).body(null);
		} catch (AccessDeniedException e) {
			return ResponseEntity.status(403).body(null);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(404).body(null);
		}
	}
	
	@DeleteMapping("/{offerId}")
	public ResponseEntity deleteService(@PathVariable Integer offerId){
		try {
			serviceService.deleteService(offerId);
			return ResponseEntity.noContent().build();
		} catch (SQLIntegrityConstraintViolationException e) {
			return ResponseEntity.status(409).build();
		} catch (AuthenticationCredentialsNotFoundException e) {
			return ResponseEntity.status(401).body(null);
		} catch (AccessDeniedException e) {
			return ResponseEntity.status(403).body(null);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(404).body(null);
		}
	}
}
