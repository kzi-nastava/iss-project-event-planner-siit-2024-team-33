package rs.ac.uns.ftn.asd.Projekatsiit2024.controller.offer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.offer.OfferService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.offer.ServiceService;


@RestController
@RequestMapping("/api/offers")
public class OfferController {


	@Autowired
	private OfferService offerService;
	@Autowired
	private OfferRepository offerRepo;
	@Autowired
	private AuthentifiedUserRepository userRepo;
	
	@GetMapping(value = "/top5", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MinimalOfferDTO>> getTop5Offers(@AuthenticationPrincipal UserPrincipal userPrincipal) {
	    Integer userId = (userPrincipal != null) ? userPrincipal.getUser().getId() : null;

	    List<Offer> offers = (userId != null) ? offerService.getTop5Offers(userId) : offerService.getTop5OffersUnauthorized();

	    List<MinimalOfferDTO> offerDTOs = offers.stream()
	        .map(MinimalOfferDTO::new)
	        .toList();

	    return ResponseEntity.ok(offerDTOs);
	}
	
    @GetMapping(value = "/rest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MinimalOfferDTO>> GetAllOffers() {
        List<Offer> offers = offerRepo.findLatestOffersByOfferID();
        
        List<MinimalOfferDTO> offersDTO = offers.stream()
                .map(MinimalOfferDTO::new)
                .toList();

        return new ResponseEntity<>(offersDTO, HttpStatus.OK);
    }
	
	
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
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
        Page<Offer> filteredOffers = offerService.getFilteredOffers(
                isProduct, isService, name, categoryName, lowestPrice, isAvailable, eventTypes, page, size);

        Page<MinimalOfferDTO> offersDto = filteredOffers.map(MinimalOfferDTO::new);

        return new ResponseEntity<>(offersDto, HttpStatus.OK);
    }
    
    @GetMapping(value = "/mine")
    public ResponseEntity<List<MinimalOfferDTO>> getLoggedUsersOffers(@AuthenticationPrincipal UserPrincipal userPrincipal){
    	if (userPrincipal == null)
    		return ResponseEntity.status(401).body(null);
    	
    	AuthentifiedUser au = userPrincipal.getUser();
    	
    	if (!userPrincipal.getUser().getRole().getName().equals("PROVIDER_ROLE"))
    		return ResponseEntity.status(403).body(null);
    	
    	List<MinimalOfferDTO> filtered = offerRepo.findLatestOffersByOfferID().stream()
    			.filter(o -> {return o.getProvider().getId() == au.getId();})
    			.map(o -> new MinimalOfferDTO(o)).toList();
    	
    	return new ResponseEntity<List<MinimalOfferDTO>>(filtered, HttpStatus.OK);
    }
}
