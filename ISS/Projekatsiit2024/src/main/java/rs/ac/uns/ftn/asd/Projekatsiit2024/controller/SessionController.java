package rs.ac.uns.ftn.asd.Projekatsiit2024.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.session.CreateSessionDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.session.CreatedSessionDTO;

@RestController
@RequestMapping("api/sessions")
public class SessionController {
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreatedSessionDTO> createSession(@RequestBody CreateSessionDTO product) throws Exception {
		CreatedSessionDTO savedSession = new CreatedSessionDTO();

		return new ResponseEntity<CreatedSessionDTO>(savedSession, HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/{token}")
	public ResponseEntity<?> deleteSession(@PathVariable("token") String token) {
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
}
