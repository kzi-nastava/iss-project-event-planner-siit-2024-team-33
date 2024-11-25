package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.comment.GetCommentDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.comment.PostCommentDTO;

@RestController
@RequestMapping("/offers")
public class OfferController {
	@PostMapping("/comment")
	public ResponseEntity<GetCommentDTO> PostComment(@RequestBody PostCommentDTO data){
		return ResponseEntity.ok(null);
	}
	
	@PutMapping("/comment")
	public ResponseEntity<GetCommentDTO> EditComment(@RequestBody PostCommentDTO data){
		return ResponseEntity.ok(null);
	}
}
