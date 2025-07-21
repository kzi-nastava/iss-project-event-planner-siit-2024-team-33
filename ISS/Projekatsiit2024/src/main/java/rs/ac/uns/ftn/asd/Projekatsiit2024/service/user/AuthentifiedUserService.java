package rs.ac.uns.ftn.asd.Projekatsiit2024.service.user;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.RegisterUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.UpdateUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.AuthentifiedUserValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.auth.RoleRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;


@Service
public class AuthentifiedUserService {

	@Autowired
	AuthentifiedUserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	@Transactional(propagation = Propagation.REQUIRED)
    public AuthentifiedUser createAuthentifiedUser(RegisterUser registerUser) throws AuthentifiedUserValidationException {
        
		AuthentifiedUser aUser = new AuthentifiedUser();
		
		//creating authentified user
		aUser.setEmail(registerUser.getEmail());
        aUser.setPassword(registerUser.getPassword());
        aUser.setName(registerUser.getName());
        aUser.setSurname(registerUser.getSurname());
        aUser.setPicture(registerUser.getPicture());
        aUser.setIsDeleted(false);
        aUser.setIsVerified(false);
        aUser.setSuspensionEndDate(null);
        aUser.setLastPasswordResetDate(null);
        aUser.setRole(roleRepository.findByName("AUSER_ROLE"));
        
        isDataCorrect(aUser, false);
        
        //encoding password before storing it
        aUser.setPassword(this.encoder.encode(aUser.getPassword()));
        
        return userRepository.save(aUser);
    }

	public AuthentifiedUser updateAuthentifiedUser(AuthentifiedUser user, UpdateUser updateUser) 
			throws AuthentifiedUserValidationException {
		
		AuthentifiedUser aUser = user;
		
		//updating organizer
        aUser.setName(updateUser.getName());
        aUser.setSurname(updateUser.getSurname());
        aUser.setPicture(updateUser.getPicture());
		
		isDataCorrect(aUser, true);
		
		return userRepository.save(aUser);
	}
	
	private boolean isDataCorrect(AuthentifiedUser aUser, boolean isUpdate) throws AuthentifiedUserValidationException {
		
		if (!Pattern.matches("^(?=.{1,254}$)(?=.{1,64}@)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", aUser.getEmail())) {
			throw new AuthentifiedUserValidationException("Email is not of valid format.");
		}
		if(!isUpdate) {
			if (userRepository.findByEmail(aUser.getEmail()) != null) {
		        throw new AuthentifiedUserValidationException("That email is already taken.");
		    }
			if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$", aUser.getPassword())) {
				throw new AuthentifiedUserValidationException("Password is not of valid format.");
			}
		}
		if (!Pattern.matches("^[a-zA-Z]{1,50}$", aUser.getName())) {
			throw new AuthentifiedUserValidationException("Name is not of valid format.");
		}
		if (!Pattern.matches("^[a-zA-Z]{1,50}$", aUser.getSurname())) {
			throw new AuthentifiedUserValidationException("Surname is not of valid format.");
		}
		
		return true;
	}
}
