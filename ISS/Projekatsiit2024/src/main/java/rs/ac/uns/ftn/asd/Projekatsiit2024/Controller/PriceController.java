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
		return ResponseEntity.ok(null);
	}
	
	@GetMapping("/pdf")
	public ResponseEntity<Byte[]> GetPricesPDF(@PathVariable Integer providerID){
		Byte[] PDFData = {};
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("file", "pricing.pdf");
		ResponseEntity<Byte[]> response = new ResponseEntity<Byte[]>(PDFData, headers, HttpStatus.OK);
		return response;
	}
	
	@PutMapping("/{offerID}")
	public ResponseEntity<GetPricesDTO> PutPrice(@PathVariable Integer providerID, @PathVariable Integer offerID, @RequestBody PutPriceDTO NewPrice){
		return ResponseEntity.ok(null);
	}
}
