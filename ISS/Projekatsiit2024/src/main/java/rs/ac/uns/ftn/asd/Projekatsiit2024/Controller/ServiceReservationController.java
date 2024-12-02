package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation.GetServiceReservationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.ServiceService;
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
	private ServiceReservationController SRC;
	
	
	
	@PostMapping
    public ResponseEntity<CreatedServiceReservationDTO> ReserveService(@PathVariable Integer serviceID, @RequestBody PostServiceReservationDTO postServiceReservationDTO) {

        // TODO: Add logic to check if the service exists
        boolean serviceExists = true;
        if (!serviceExists) {
            return ResponseEntity.notFound().build();
        }

        // 409: Conflict if the service is already reserved for the requested date
        boolean alreadyReserved = false;
        if (alreadyReserved) {
            return ResponseEntity.status(409).build();
        }

        OfferReservation reservation = new OfferReservation();
        // TODO: Populate reservation fields and save to database

        CreatedServiceReservationDTO createdReservation = new CreatedServiceReservationDTO(reservation);
        return ResponseEntity.ok(createdReservation);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<GetServiceReservationDTO> getServiceReservationById(@PathVariable Integer serviceID, @PathVariable int reservationId) {
        OfferReservation SR = new OfferReservation();
        if (SR == null)
			return ResponseEntity.notFound().build();
        
    	return ResponseEntity.ok(new GetServiceReservationDTO(SR));
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<CreatedServiceReservationDTO> updateServiceReservation(
    		@PathVariable Integer serviceID,
    		@PathVariable Integer reservationId, 
            @RequestBody PostServiceReservationDTO postServiceReservationDTO) {
        // TODO: Add logic to check if the service and reservation exist
        boolean serviceExists = true;
        boolean reservationExists = true;

        if (!serviceExists || !reservationExists) {
            return ResponseEntity.notFound().build();
        }

        // TODO: Add logic to verify user authorization, if needed
        boolean unauthorized = false;
        if (unauthorized) {
            return ResponseEntity.status(403).build();
        }

        OfferReservation reservation = new OfferReservation();
        // TODO: Update reservation fields and save to database

        CreatedServiceReservationDTO updatedReservation = new CreatedServiceReservationDTO(reservation);
        return ResponseEntity.ok(updatedReservation);
    }
}
