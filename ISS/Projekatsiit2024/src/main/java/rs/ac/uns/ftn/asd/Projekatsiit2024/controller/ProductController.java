package rs.ac.uns.ftn.asd.Projekatsiit2024.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.product.Product;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.ProductService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.offerReservationService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.CreateProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.CreatedProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.GetProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.MinimalProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.UpdateProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.UpdatedProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.productPurchase.PostProductPurchaseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.GetServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.ProductService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.offerReservationService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.offerService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private offerService offerService;

	
	
	@GetMapping("/{id}")
	public ResponseEntity getProduct(@PathVariable Integer id) {
		try {
			Product p = productService.get(id);
			return ResponseEntity.ok(new GetProductDTO(p));
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreatedProductDTO> createProduct(@RequestBody CreateProductDTO product) throws Exception {
		CreatedProductDTO savedProduct = new CreatedProductDTO();

		return new ResponseEntity<CreatedProductDTO>(savedProduct, HttpStatus.CREATED);
	}
	
	
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UpdatedProductDTO> updateProduct(@RequestBody UpdateProductDTO product, @PathVariable Long id)
			throws Exception {
		UpdatedProductDTO updatedProduct = new UpdatedProductDTO();

		return new ResponseEntity<UpdatedProductDTO>(updatedProduct, HttpStatus.OK);
	}
	
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
		//not physical deletion
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PostMapping("/{id}/reservations")
	public ResponseEntity buyProduct(@PathVariable Integer id, @RequestBody PostProductPurchaseDTO data){
		try {
			return ResponseEntity.ok(new MinimalProductDTO(productService.buyProduct(id, data.eventId)));
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(404).body(e.toString());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.toString());
		}
	}
	
	@DeleteMapping("/{id}/reservations/{eventId}")
	public ResponseEntity cancelReservation(@PathVariable Integer id, @PathVariable Integer eventId) {
		try {
			productService.cancelReservation(id, eventId);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.toString());
		}
	}
}
