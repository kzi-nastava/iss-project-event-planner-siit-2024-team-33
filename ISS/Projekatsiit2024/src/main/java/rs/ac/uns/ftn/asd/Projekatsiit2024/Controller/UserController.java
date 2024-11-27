package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.service.GetServiceDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.GetUserDTO;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetUserDTO> getBook(@PathVariable("id") Long id) {
		GetUserDTO user = new GetUserDTO();

		if (user == null) {
			return new ResponseEntity<GetUserDTO>(HttpStatus.NOT_FOUND);
		}

		/*book.setId(1L);
		book.setName("Na Drini ćuprija");
		book.setIsbn("0-306-40615-2");
		book.setDescription("Predobra knjiga");*/
		
		return new ResponseEntity<GetUserDTO>(user, HttpStatus.OK);
	}
}
