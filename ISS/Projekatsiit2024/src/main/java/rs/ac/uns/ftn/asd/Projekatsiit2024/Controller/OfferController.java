package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.ServiceService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.offerService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.FilterEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.MinimalEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offer.GetOfferDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offer.OfferFilterDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.GetServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.PostServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.PutServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.ServiceFilterDTO;


@RestController
@RequestMapping("/api/offers")
public class OfferController {

	
	@Autowired
	private offerService offerService;
	
	@GetMapping(value = "/top5", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<GetOfferDTO>> GetTop5Offers(@RequestBody Integer id) {
	    List<GetOfferDTO> offers = new ArrayList<>();
	    List<Offer> offerz = offerService.getTop5Offers(id);
	    
	    for(Offer off:offerz) {
	    	GetOfferDTO offr = new GetOfferDTO(off);
	    	offers.add(offr);
	    }
	    
	    return ResponseEntity.ok(offers);
	}
	
    @GetMapping(value = "/rest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GetOfferDTO>> GetAllOffers(@RequestBody Integer id) {
        List<Offer> offers = offerService.getRestOffers(id);
        
        List<GetOfferDTO> offersDTO = offers.stream()
                .map(GetOfferDTO::new)
                .toList();
  
        return new ResponseEntity<>(offersDTO, HttpStatus.OK);
    }
	
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GetOfferDTO>> GetOfferList(@RequestBody OfferFilterDTO OFDTO, @RequestBody int id) {
        List<Offer> offers = offerService.getFileteredOffers(OFDTO.getIsProduct(),OFDTO.getIsService(),OFDTO.getName(),
        													OFDTO.getCategory(),OFDTO.getLowestPrice(),OFDTO.getIsAvailable(),OFDTO.getEventTypes());

        List<GetOfferDTO> offersDTO = offers.stream()
                .map(GetOfferDTO::new)
                .toList();      
        
        return new ResponseEntity<>(offersDTO, HttpStatus.OK);
    }
	
	
	

	
}
