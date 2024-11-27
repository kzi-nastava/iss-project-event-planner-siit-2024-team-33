package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.comment.GetCommentDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.comment.PostCommentDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.offer.OfferDTO;

@RestController
@RequestMapping("/api/offers")
public class OfferController {
	@PostMapping("/comment")
	public ResponseEntity<GetCommentDTO> PostComment(@RequestBody PostCommentDTO data){
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/favorites")
	public ResponseEntity<OfferDTO> AddToFavorites(@RequestAttribute Integer OfferID){
		return ResponseEntity.ok(null);
	}
	
	@PutMapping("/comment")
	public ResponseEntity<GetCommentDTO> EditComment(@RequestBody PostCommentDTO data){
		return ResponseEntity.ok(null);
	}
	
	@DeleteMapping("/favorites")
	public ResponseEntity<OfferDTO> DeleteFromFavorites(@RequestAttribute Integer OfferID){
		return ResponseEntity.ok(null);
	}
}
