package rs.ac.uns.ftn.asd.Projekatsiit2024.controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.service.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.FilterEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.MinimalEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offer.MinimalOfferDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offer.OfferFilterDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.GetServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.PostServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.PutServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.ServiceFilterDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.ServiceService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.offerService;


@RestController
@RequestMapping("/api/offers")
public class OfferController {

	
	@Autowired
	private offerService offerService;
	@Autowired
	private AuthentifiedUserRepository userRepo;
	
	@GetMapping(value = "/top5/authentified", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MinimalOfferDTO>> GetTop5Offers() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails principal = (UserDetails) auth.getPrincipal();
		String email = principal.getUsername();
	
		AuthentifiedUser user = userRepo.findByEmail(email);
		
		int id = user.getId();
	    List<MinimalOfferDTO> offers = new ArrayList<>();
	    List<Offer> offerz = offerService.getTop5Offers(id);
	    
	    for(Offer off:offerz) {
	    	MinimalOfferDTO offr = new MinimalOfferDTO(off);
	    	offers.add(offr);
	    }
	    
	    return ResponseEntity.ok(offers);
	}
	
	@GetMapping(value = "/top5/unauthentified", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MinimalOfferDTO>> GetTop5OffersUnauthentified() {
		
	    List<MinimalOfferDTO> offers = new ArrayList<>();
	    List<Offer> offerz = offerService.getTop5OffersUnauthorized();
	    
	    for(Offer off:offerz) {
	    	MinimalOfferDTO offr = new MinimalOfferDTO(off);
	    	offers.add(offr);
	    }
	    
	    return ResponseEntity.ok(offers);
	}
	
    @GetMapping(value = "/rest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MinimalOfferDTO>> GetAllOffers() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails principal = (UserDetails) auth.getPrincipal();
		String email = principal.getUsername();
	
		AuthentifiedUser user = userRepo.findByEmail(email);
		
		int id = user.getId();
        List<Offer> offers = offerService.getRestOffers(id);
        
        List<MinimalOfferDTO> offersDTO = offers.stream()
                .map(MinimalOfferDTO::new)
                .toList();
  
        return new ResponseEntity<>(offersDTO, HttpStatus.OK);
    }
	
	
    @GetMapping(value = "/filter/authentified", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<MinimalOfferDTO>> getOfferList(
            @RequestParam(name = "isProduct", required = false) Boolean isProduct,
            @RequestParam(name = "isService", required = false) Boolean isService,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "category", required = false) String categoryName,
            @RequestParam(name = "lowestPrice", required = false) Integer lowestPrice,
            @RequestParam(name = "isAvailable", required = false) Availability isAvailable,
            @RequestParam(name = "eventTypes", required = false) List<Integer> eventTypes,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "6") int size) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) auth.getPrincipal();
        String email = principal.getUsername();

        AuthentifiedUser user = userRepo.findByEmail(email);
        int userId = user.getId();

        Page<Offer> filteredOffers = offerService.getFilteredOffers(
                isProduct, isService, name, categoryName, lowestPrice, isAvailable, eventTypes, userId, page, size);

        Page<MinimalOfferDTO> offersDto = filteredOffers.map(MinimalOfferDTO::new);

        return new ResponseEntity<>(offersDto, HttpStatus.OK);
    }
    
    @GetMapping(value = "/filter/unauthentified", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<MinimalOfferDTO>> getOfferListUnauthentified(
            @RequestParam(name = "isProduct", required = false) Boolean isProduct,
            @RequestParam(name = "isService", required = false) Boolean isService,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "category", required = false) String categoryName,
            @RequestParam(name = "lowestPrice", required = false) Integer lowestPrice,
            @RequestParam(name = "isAvailable", required = false) Availability isAvailable,
            @RequestParam(name = "eventTypes", required = false) List<Integer> eventTypes,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "6") int size) {

    	Page<Offer> filteredOffers = offerService.getFilteredOffersUnauthorized(
                isProduct, isService, name, categoryName, lowestPrice, isAvailable, eventTypes,page, size);

        Page<MinimalOfferDTO> offersDto = filteredOffers.map(MinimalOfferDTO::new);

        return new ResponseEntity<>(offersDto, HttpStatus.OK);
    }
	
}
