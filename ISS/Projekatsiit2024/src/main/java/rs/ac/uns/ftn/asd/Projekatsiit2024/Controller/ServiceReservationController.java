package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation.GetServiceReservationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.OfferReservationRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.ServiceService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.offerReservationService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.offerService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation.CreatedServiceReservationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation.PostServiceReservationDTO;

import java.util.List;

@RestController
@RequestMapping("/api/services/{serviceID}/reservations")
public class ServiceReservationController {

	@Autowired
	private offerService offerService;
	@Autowired
	private ServiceService serviceService;
	@Autowired
	private offerReservationService oRS;
	@Autowired
	private OfferReservationRepository oRR;
	
	
	@PostMapping
	public ResponseEntity<CreatedServiceReservationDTO> ReserveService(
	        @PathVariable Integer serviceID,
	        @RequestBody PostServiceReservationDTO postServiceReservationDTO) {

	    boolean serviceExists = serviceService.checkIfServiceExists(serviceID);
	    if (!serviceExists) {
	        return ResponseEntity.notFound().build();
	    }

	    try {
	        OfferReservation reservation = oRS.createOfferReservation(
	                postServiceReservationDTO.getReservationDate(),
	                serviceID,
	                postServiceReservationDTO.getEventId(),
	                postServiceReservationDTO.getStartTime(),
	                postServiceReservationDTO.getEndTime()
	        );

	        CreatedServiceReservationDTO createdReservation = new CreatedServiceReservationDTO(reservation);
	        return ResponseEntity.ok(createdReservation);
	    } catch (IllegalArgumentException ex) {
	        return ResponseEntity.status(409).body(null);
	    }
	}


	@GetMapping("/{reservationId}")
	public ResponseEntity<GetServiceReservationDTO> getServiceReservationById(
	        @PathVariable Integer serviceID, 
	        @PathVariable Integer reservationId) {

	    
	    OfferReservation reservation = oRR.findById(reservationId)
	            .orElse(null);

	    
	    GetServiceReservationDTO reservationDTO = new GetServiceReservationDTO(reservation);
	    return ResponseEntity.ok(reservationDTO);
	}

    @PutMapping("/{reservationId}")
    public ResponseEntity<CreatedServiceReservationDTO> updateServiceReservation(
            @PathVariable Integer serviceID,
            @PathVariable Integer reservationId,
            @RequestBody PostServiceReservationDTO postServiceReservationDTO) {

        boolean serviceExists = serviceService.checkIfServiceExists(serviceID);
        if (!serviceExists) {
            return ResponseEntity.notFound().build();
        }

        try {
            OfferReservation updatedReservation = oRS.updateReservation(
                    reservationId,
                    serviceID,
                    postServiceReservationDTO.getReservationDate(),
                    postServiceReservationDTO.getStartTime(),
                    postServiceReservationDTO.getEndTime()
            );

            CreatedServiceReservationDTO updatedDTO = new CreatedServiceReservationDTO(updatedReservation);
            return ResponseEntity.ok(updatedDTO);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(409).body(null);
        }
    }
}
