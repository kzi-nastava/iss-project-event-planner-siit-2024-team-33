package rs.ac.uns.ftn.asd.Projekatsiit2024.controller;

import java.util.ArrayList;
import java.util.Collection;

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

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.CreateProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.CreatedProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.GetProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.UpdateProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.product.UpdatedProductDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.GetServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.CreateUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.CreatedUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.GetUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.UpdateUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.UpdatedUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Availability;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.OfferCategory;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.ServiceService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.user.AuthentifiedUserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private AuthentifiedUserService userRepo;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<GetUserDTO>> getUsers() {
		Collection<GetUserDTO> users = new ArrayList<>() ;

		GetUserDTO user = new GetUserDTO();
		GetUserDTO user2 = new GetUserDTO();

		users.add(user);
		users.add(user2);

		return new ResponseEntity<Collection<GetUserDTO>>(users, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetUserDTO> getUser(@PathVariable("id") Long id) {
		GetUserDTO user = new GetUserDTO();
		
		if (user == null) {
			return new ResponseEntity<GetUserDTO>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<GetUserDTO>(user, HttpStatus.OK);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreatedUserDTO> createUser(@RequestBody CreateUserDTO user) throws Exception {
		CreatedUserDTO savedUser = new CreatedUserDTO();

		return new ResponseEntity<CreatedUserDTO>(savedUser, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UpdatedUserDTO> updateUser(@RequestBody UpdateUserDTO user, @PathVariable Long id)
			throws Exception {
		UpdatedUserDTO updatedUser = new UpdatedUserDTO();

		return new ResponseEntity<UpdatedUserDTO>(updatedUser, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") Long Id) {
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
    @PostMapping(value = "/{blockerId}/block/{blockedId}")
    public ResponseEntity<String> blockUser(@PathVariable Integer blockerId, @PathVariable Integer blockedId) {
        try {
            userRepo.blockAUser(blockerId, blockedId);
            return ResponseEntity.ok("");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
    }

    @DeleteMapping(value = "/{blockerId}/block/{blockedId}")
    public ResponseEntity<String> unblockUser(@PathVariable Integer blockerId, @PathVariable Integer blockedId) {
        try {
            userRepo.unblockAUser(blockerId, blockedId);
            return ResponseEntity.ok("");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
    }
    
}
