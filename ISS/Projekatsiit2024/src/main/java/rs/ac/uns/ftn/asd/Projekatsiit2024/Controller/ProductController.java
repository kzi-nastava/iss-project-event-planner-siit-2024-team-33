package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

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
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.ProductService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.offerReservationService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event.GetEventDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.CreateProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.CreatedProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.GetProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.MinimalProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.UpdateProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.UpdatedProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.productPurchase.PostProductPurchaseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.GetServiceDTO;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping(value = "/top5", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<GetProductDTO>> GetTop5Products() {
	    List<GetProductDTO> products = new ArrayList<>();

	    for (int i = 1; i <= 5; i++) {
	        GetProductDTO product = new GetProductDTO();
	        product.setId(i);
	        product.setName("Top Product " + i);
	        product.setDescription("Description for top product " + i);
	        product.setPrice(100.0 + i);
	        product.setDiscount(10.0 + i);
	        product.setPictures(List.of("image" + i + ".jpg"));
//	        product.Availability = Availability.Available;
	        product.CreationDate = new Date(System.currentTimeMillis());
	        product.IsPending = false;
	        product.IsDeleted = false;
//	        product.Category = new OfferCategory();
	        product.Provider = new Provider();
	        product.ValidEvents = new ArrayList<>();
	        product.OfferReservations = new ArrayList<>();
	        product.Ratings = new ArrayList<>();

	        products.add(product);
	    }

	    return new ResponseEntity<>(products, HttpStatus.OK);
	}

    
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<GetProductDTO>> getProducts(
			@RequestParam(required = false) String name,
            @RequestParam(required = false) OfferCategory offerCategory,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Availability Availability) {
		Collection<GetProductDTO> products = new ArrayList<>() ;

		GetProductDTO product = new GetProductDTO();
		GetProductDTO product2 = new GetProductDTO();

		products.add(product);
		products.add(product2);

		return new ResponseEntity<Collection<GetProductDTO>>(products, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetProductDTO> getProduct(@PathVariable("id") Long id) {
		GetProductDTO product = new GetProductDTO();

		if (product == null) {
			return new ResponseEntity<GetProductDTO>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<GetProductDTO>(product, HttpStatus.OK);
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
			return ResponseEntity.ok(new MinimalProductDTO(productService.buyProduct(id, data.EventID)));
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(404).body(e.toString());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.toString());
		}
	}
}
