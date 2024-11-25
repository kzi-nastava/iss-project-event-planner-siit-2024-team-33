package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation.GetServiceReservationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation.CreatedServiceReservationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation.PostServiceReservationDTO;

import java.util.List;

@RestController
@RequestMapping("/api/serviceReservations")
public class ServiceReservationController {

    @PostMapping
    public ResponseEntity<CreatedServiceReservationDTO> createServiceReservation(@RequestBody PostServiceReservationDTO postServiceReservationDTO) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<GetServiceReservationDTO> getServiceReservationById(@RequestAttribute() int reservationId) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<List<GetServiceReservationDTO>> getAllReservationsForEvent(@RequestAttribute() int eventId) {
        return ResponseEntity.ok(null);
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<CreatedServiceReservationDTO> updateServiceReservation(
    		@RequestAttribute() int reservationId, 
            @RequestBody PostServiceReservationDTO postServiceReservationDTO) {
        return ResponseEntity.ok(null);
    }
}
