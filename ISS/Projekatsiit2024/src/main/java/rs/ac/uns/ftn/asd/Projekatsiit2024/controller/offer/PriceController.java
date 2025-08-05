package rs.ac.uns.ftn.asd.Projekatsiit2024.controller.offer;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.prices.PriceItemDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.prices.PutPriceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.offer.OfferService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.offer.PriceService;

@RestController
@RequestMapping("/api/offers/mine/prices")
public class PriceController {

	@Autowired 
	public OfferRepository offerRepo;
	@Autowired 
	public OfferService offerService;
	@Autowired
	public PriceService priceService;
	
	@GetMapping
	public ResponseEntity<List<PriceItemDTO>> GetMyPrices(@AuthenticationPrincipal UserPrincipal userPrincipal){
		if(userPrincipal == null)
			return ResponseEntity.status(404).body(null);
		
		if(!userPrincipal.getUser().getRole().getName().equals("PROVIDER_ROLE"))
			return ResponseEntity.status(403).body(null);
		
		List<Offer> allOffers = offerRepo.findLatestOffersByOfferID();
		
		//Return a list
		return ResponseEntity.ok(allOffers.stream().filter(o -> o.getProvider().getEmail() == userPrincipal.getUsername()).map(o -> new PriceItemDTO(o)).toList());
	}
	
	@GetMapping("/export")
	public ResponseEntity<byte[]> GetPricesPDF(@AuthenticationPrincipal UserPrincipal userPrincipal){
		try {
			byte[] PDFData = priceService.GeneratePdf();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("file", "pricing.pdf");
			ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(PDFData, headers, HttpStatus.OK);
			return response;
		} catch (AccessDeniedException e) {
			return ResponseEntity.status(403).body(null);
		}
	}
	
	@PutMapping("/{offerID}")
	public ResponseEntity<List<PriceItemDTO>> ChangePrice(
			@AuthenticationPrincipal UserPrincipal userPrincipal,
			@PathVariable Integer offerID,
			@RequestBody PutPriceDTO NewPrice
			){
		try {
			offerService.editOfferPrice(offerID, NewPrice.FullPrice, NewPrice.Discount);
			return ResponseEntity.ok(offerRepo.findLatestOffersByOfferID().stream()
					.filter(o -> o.getProvider().getEmail() == userPrincipal.getUsername())
					.map(o -> new PriceItemDTO(o)).toList());
		} catch (BadRequestException e) {
			return ResponseEntity.status(400).body(null);
		} catch (AccessDeniedException e) {
			return ResponseEntity.status(403).body(null);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(404).body(null);
		}
	}
}
