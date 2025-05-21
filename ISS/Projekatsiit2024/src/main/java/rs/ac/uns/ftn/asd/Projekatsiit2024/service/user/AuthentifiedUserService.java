package rs.ac.uns.ftn.asd.Projekatsiit2024.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.RegisterUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.AuthentifiedUserCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;


@Service
public class AuthentifiedUserService {

	@Autowired
	AuthentifiedUserRepository userRepo;
	
	@Transactional(propagation = Propagation.REQUIRED)
    public AuthentifiedUser createAuthentifiedUser(RegisterUser registerUser) throws AuthentifiedUserCreationException {
        AuthentifiedUser user = new AuthentifiedUser();
        if (userRepo.findByEmail(registerUser.getEmail()) == null) {
            throw new AuthentifiedUserCreationException("That email is already taken.");
        }
        user.setEmail(registerUser.getEmail());
        user.setPassword(registerUser.getPassword());
        user.setName(registerUser.getName());
        user.setSurname(registerUser.getSurname());
        user.setPicture(registerUser.getPicture());

        userRepo.save(user);
        return user;
    }
}
