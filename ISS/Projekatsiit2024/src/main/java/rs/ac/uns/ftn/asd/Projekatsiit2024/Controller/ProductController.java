package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.GetProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.productPurchase.PostProductPurchaseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.GetServiceDTO;

@RestController
public class ProductController {
	@GetMapping("/api/details")
	public ResponseEntity<GetProductDTO> GetDetails(@RequestAttribute Integer id) {
		return ResponseEntity.ok(null);
	}
	
	@PostMapping
	public ResponseEntity<GetProductDTO> BuyProduct(@RequestBody PostProductPurchaseDTO data){
		return ResponseEntity.ok(null);
	}
}
