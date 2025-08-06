package rs.ac.uns.ftn.asd.Projekatsiit2024.controller.offer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.product.Product;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.offer.ProductService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.CreateProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.CreatedProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.GetProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.MinimalProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.ProviderProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.UpdateProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.UpdatedProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.productPurchase.PostProductPurchaseDTO;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;

	
	
	@GetMapping("/{id}")
	public ResponseEntity<GetProductDTO> getProductDetails(@PathVariable Integer id) {
		try {
			Product p = productService.get(id);
			return ResponseEntity.ok(new GetProductDTO(p));
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	
	@GetMapping("/me")
	public ResponseEntity<Page<ProviderProductDTO>> getProvidersProducts(
			@AuthenticationPrincipal UserPrincipal userPrincipal, 
			@PageableDefault(size = 10, sort = "offerID") Pageable pageable,
			@RequestParam(value = "query", required = false) String query,
		    @RequestParam(value = "offerCategoryId", required = false) List<Integer> offerCategoryId,
		    @RequestParam(value = "eventTypeId", required = false) List<Integer> eventTypeId,
		    @RequestParam(value = "maxPrice", required = false) Double maxPrice,
		    @RequestParam(value = "availability", required = false) List<Availability> availability) {

		Page<ProviderProductDTO> products = 
				productService.readProviderProducts(userPrincipal, pageable, 
						query, offerCategoryId, eventTypeId, maxPrice, availability);
		
		return new ResponseEntity<Page<ProviderProductDTO>>(products, HttpStatus.OK);
	}
	
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreatedProductDTO> createProduct(@AuthenticationPrincipal UserPrincipal userPrincipal, 
			@RequestBody CreateProductDTO product) {
		Product createdProduct = productService.createProduct(userPrincipal, product);

		return new ResponseEntity<CreatedProductDTO>(new CreatedProductDTO(createdProduct), HttpStatus.CREATED);
	}
	
	
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UpdatedProductDTO> updateProduct(@AuthenticationPrincipal UserPrincipal userPrincipal, 
			@RequestBody UpdateProductDTO product, @PathVariable Integer id) {
		Product updatedProduct = productService.updateProduct(userPrincipal, product, id);

		return new ResponseEntity<UpdatedProductDTO>(new UpdatedProductDTO(updatedProduct), HttpStatus.OK);
	}
	
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteProduct(@AuthenticationPrincipal UserPrincipal userPrincipal, 
			@PathVariable("id") Integer id) {
		productService.deleteProduct(userPrincipal, id);
		
		return ResponseEntity.noContent().build();
	}
	
	
	
	
	
	
	
	
	@PostMapping("/{id}/reservations")
	public ResponseEntity<MinimalProductDTO> buyProduct(@PathVariable Integer id, @RequestBody PostProductPurchaseDTO data){
		try {
			return ResponseEntity.ok(new MinimalProductDTO(productService.buyProduct(id, data.eventId)));
		} catch (AccessDeniedException e) {
			return ResponseEntity.status(403).body(null);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(404).body(null);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(409).body(null);
		}
	}
	
	@DeleteMapping("/{id}/reservations/{eventId}")
	public ResponseEntity<?> cancelReservation(@PathVariable Integer id, @PathVariable Integer eventId) {
		try {
			productService.cancelReservation(id, eventId);
			return ResponseEntity.noContent().build();
		} catch (AccessDeniedException e) {
			return ResponseEntity.status(403).body(e.toString());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.toString());
		}
	}
}
