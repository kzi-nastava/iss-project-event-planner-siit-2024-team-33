package rs.ac.uns.ftn.asd.Projekatsiit2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.internet.ParseException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation.GetServiceReservationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation.CreatedServiceReservationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.serviceReservation.PostServiceReservationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.OfferReservation;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferReservationRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.ServiceService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.offerReservationService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.offerService;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
	@Autowired
	private EventRepository eR;
	@Autowired
	private OfferRepository oR;
	
	@PostMapping
	public ResponseEntity<CreatedServiceReservationDTO> ReserveService(
	        @PathVariable Integer serviceID,
	        @RequestBody PostServiceReservationDTO postServiceReservationDTO) throws ParseException, java.text.ParseException {

	    boolean serviceExists = serviceService.checkIfServiceExists(serviceID);
	    if (!serviceExists) {
	        return ResponseEntity.notFound().build();
	    }

	    try {

        	LocalDateTime startDateTime = LocalDateTime.parse(postServiceReservationDTO.getReservationDate()+"T"+postServiceReservationDTO.getStartTime());
        	LocalDateTime endDateTime = LocalDateTime.parse(postServiceReservationDTO.getReservationDate()+"T"+postServiceReservationDTO.getEndTime());
	        
	        OfferReservation reservation = oRS.bookAService(
	                postServiceReservationDTO.getEventId(),
	                serviceID,
	                startDateTime,
	                endDateTime
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
            @RequestBody PostServiceReservationDTO postServiceReservationDTO) throws ParseException, java.text.ParseException {

        boolean serviceExists = serviceService.checkIfServiceExists(serviceID);
        if (!serviceExists) {
            return ResponseEntity.notFound().build();
        }

        try {
//        	LocalDateTime startTime = parseTime(postServiceReservationDTO.getStartTime(), postServiceReservationDTO.getReservationDate());
//        	LocalDateTime endTime = parseTime(postServiceReservationDTO.getEndTime(), postServiceReservationDTO.getReservationDate());
//        	LocalDate startDate = LocalDate.parse(postServiceReservationDTO.getReservationDate());
//        	LocalTime startTime = LocalTime.parse(postServiceReservationDTO.getStartTime());
        	LocalDateTime startDateTime = LocalDateTime.parse(postServiceReservationDTO.getReservationDate()+"T"+postServiceReservationDTO.getStartTime());
        	LocalDateTime endDateTime = LocalDateTime.parse(postServiceReservationDTO.getReservationDate()+"T"+postServiceReservationDTO.getEndTime());
	        LocalDate reservationDate = LocalDate.parse(postServiceReservationDTO.getReservationDate());

            OfferReservation updatedReservation = oRS.updateReservation(
                    reservationId,
                    serviceID,
                    reservationDate,
                    startDateTime.toLocalTime(),
                    endDateTime.toLocalTime()
            );

            CreatedServiceReservationDTO updatedDTO = new CreatedServiceReservationDTO(updatedReservation);
            return ResponseEntity.ok(updatedDTO);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(409).body(null);
        }
    }



	private Date parseDate(String dateString) throws ParseException, java.text.ParseException {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    java.util.Date date = sdf.parse(dateString);
	    return new Date(date.getTime());
	}
}
