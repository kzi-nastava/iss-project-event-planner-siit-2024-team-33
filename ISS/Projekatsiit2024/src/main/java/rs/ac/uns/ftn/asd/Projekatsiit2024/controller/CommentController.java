package rs.ac.uns.ftn.asd.Projekatsiit2024.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/api/offers/{offerID}")
public class CommentController {
	
	@PostMapping("/comments")
	public ResponseEntity<GetCommentDTO> PostComment(@PathVariable Integer offerID, @RequestBody PostCommentDTO data){
		//403: Forbidden if user didn't purchase the offer
		Boolean wrongUser = false;
		if(wrongUser)
			return ResponseEntity.status(403).build();
		
		//404: Offer not found
		Boolean notFound = false;
		if(notFound)
			return ResponseEntity.notFound().build();
		
		//409: Conflict, already commented
		Boolean alreadyCommented = false;
		if(alreadyCommented)
			return ResponseEntity.status(409).build();
		
		return ResponseEntity.ok(new GetCommentDTO());
	}
	
	@PutMapping("/comments/{commentID}")
	public ResponseEntity<GetCommentDTO> EditComment(@PathVariable Integer commentID, @PathVariable Integer offerID, @RequestBody PostCommentDTO data){
		//403: Forbidden if user didn't make the comment being edited
		Boolean wrongUser = false;
		if(wrongUser)
			return ResponseEntity.status(403).build();
		
		//404: Comment not found
		Boolean notFound = false;
		if(notFound)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(new GetCommentDTO());
	}
}
