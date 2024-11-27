package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.prices.GetPricesDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.prices.PutPriceDTO;

@RestController
@RequestMapping("/api/prices")
public class PriceController {
	
	@GetMapping
	public ResponseEntity<GetPricesDTO> GetMyPrices(){
		return ResponseEntity.ok(null);
	}
	
	@GetMapping("/pdf")
	public ResponseEntity<Byte[]> GetPricesPDF(){
		Byte[] PDFData = {};
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("file", "pricing.pdf");
		ResponseEntity<Byte[]> response = new ResponseEntity<Byte[]>(PDFData, headers, HttpStatus.OK);
		return response;
	}
	
	@PutMapping
	public ResponseEntity<GetPricesDTO> PutPrice(@RequestBody PutPriceDTO NewPrice){
		return ResponseEntity.ok(null);
	}
}
