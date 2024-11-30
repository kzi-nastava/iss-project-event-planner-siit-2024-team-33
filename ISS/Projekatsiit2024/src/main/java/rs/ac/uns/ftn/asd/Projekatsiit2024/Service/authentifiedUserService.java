package rs.ac.uns.ftn.asd.Projekatsiit2024.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.transaction.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.CreatedUserDTO;

public class authentifiedUserService {

	@Autowired
	AuthentifiedUserRepository authentifiedUserRepo;
	
	@Transactional
    public AuthentifiedUser createAuthentifiedUser(String email,
    		String password,
    		String name,
    		String surname,
    		List<String> pictures) {
        AuthentifiedUser user = new AuthentifiedUser();

        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        user.setPictures(pictures);


        return authentifiedUserRepo.saveAndFlush(user);
    }
	
}
