package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Common.ImageManager;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.AuthentifiedUser;

@Getter
@Setter
public class GetUserDTO {
	private Integer Id;
	private String Email;
	private String Password;
	private String Name;
	private String Surname;
	private List<String> Pictures;
	
	public GetUserDTO() {
		
	}
	
	public GetUserDTO(AuthentifiedUser au) {
		this.setId(au.getId());
		this.setEmail(au.getEmail());
		this.setPassword(au.getPassword());
		this.setName(au.getName());
		this.setSurname(au.getSurname());
		this.setPictures(au.getPictures().stream().map(imgUrl -> ImageManager.loadAsDataURI(imgUrl)).toList());
	}
}
