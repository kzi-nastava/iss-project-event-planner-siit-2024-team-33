package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.GetProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.productPurchase.PostProductPurchaseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.GetServiceDTO;

@RestController("/api/products")
public class ProductController {
	@GetMapping("/{id}")
	public ResponseEntity<GetProductDTO> GetDetails(@PathVariable Integer id) {
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/{id}/buy")
	public ResponseEntity<GetProductDTO> BuyProduct(@PathVariable Integer id, @RequestBody PostProductPurchaseDTO data){
		return ResponseEntity.ok(null);
	}
}
