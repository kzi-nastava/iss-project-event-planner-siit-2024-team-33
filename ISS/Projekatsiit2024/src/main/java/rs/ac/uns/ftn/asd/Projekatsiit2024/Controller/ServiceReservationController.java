package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation.GetServiceReservationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation.CreatedServiceReservationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation.PostServiceReservationDTO;

import java.util.List;

@RestController
@RequestMapping("/api/services/{serviceID}/reservations")
public class ServiceReservationController {

    @PostMapping
    public ResponseEntity<CreatedServiceReservationDTO> ReserveService(@PathVariable Integer serviceID, @RequestBody PostServiceReservationDTO postServiceReservationDTO) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<GetServiceReservationDTO> getServiceReservationById(@PathVariable Integer serviceID, @PathVariable int reservationId) {
        return ResponseEntity.ok(null);
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<CreatedServiceReservationDTO> updateServiceReservation(
    		@PathVariable Integer serviceID,
    		@PathVariable Integer reservationId, 
            @RequestBody PostServiceReservationDTO postServiceReservationDTO) {
        return ResponseEntity.ok(null);
    }
}
