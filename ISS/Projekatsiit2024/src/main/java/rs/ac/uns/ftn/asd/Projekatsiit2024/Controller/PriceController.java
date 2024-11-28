package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.prices.GetPricesDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.prices.PutPriceDTO;

@RestController
@RequestMapping("/api/user/{providerID}/offer-prices")
public class PriceController {
	
	@GetMapping
	public ResponseEntity<GetPricesDTO> GetMyPrices(@PathVariable Integer providerID){
		//403: Forbidden if user isn't a provider
		Boolean wrongUser = false;
		if(wrongUser)
			return ResponseEntity.status(403).build();
		
		//Return a list
		return ResponseEntity.ok(null);
	}
	
	@GetMapping("/pdf")
	public ResponseEntity<Byte[]> GetPricesPDF(@PathVariable Integer providerID){
		//403: Forbidden if user isn't a provider
		Boolean wrongUser = false;
		if(wrongUser)
			return ResponseEntity.status(403).build();
		
		Byte[] PDFData = {};
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("file", "pricing.pdf");
		ResponseEntity<Byte[]> response = new ResponseEntity<Byte[]>(PDFData, headers, HttpStatus.OK);
		return response;
	}
	
	@PutMapping("/{offerID}")
	public ResponseEntity<GetPricesDTO> ChangePrice(@PathVariable Integer providerID, @PathVariable Integer offerID, @RequestBody PutPriceDTO NewPrice){
		//403: Forbidden if user isn't the offer provider
		Boolean wrongUser = false;
		if(wrongUser)
			return ResponseEntity.status(403).build();
		
		//404: Offer not found
		Boolean notFound = false;
		if(notFound)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.noContent().build();
	}
}
