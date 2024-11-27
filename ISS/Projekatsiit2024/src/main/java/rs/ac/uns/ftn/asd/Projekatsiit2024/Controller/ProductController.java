package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import java.util.ArrayList;
import java.util.Collection;

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
import org.springframework.web.bind.annotation.RestController;


import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.CreateProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.CreatedProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.GetProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.UpdateProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.UpdatedProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.productPurchase.PostProductPurchaseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.GetServiceDTO;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<GetProductDTO>> getProducts() {
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
	
	
	@PostMapping
	public ResponseEntity<GetProductDTO> BuyProduct(@RequestBody PostProductPurchaseDTO data){
		return ResponseEntity.ok(null);
	}
}
