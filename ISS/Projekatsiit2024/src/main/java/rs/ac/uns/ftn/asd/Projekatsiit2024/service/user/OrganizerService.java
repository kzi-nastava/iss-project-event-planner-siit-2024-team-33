package rs.ac.uns.ftn.asd.Projekatsiit2024.service.user;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.RegisterUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.UpdateUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.OrganizerValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.auth.RoleRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.OrganizerRepository;

@Service
public class OrganizerService {
	@Autowired
    private AuthentifiedUserRepository userRepository;
	
	@Autowired
    private OrganizerRepository organizerRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	@Transactional(propagation = Propagation.REQUIRED)
    public Organizer createOrganizer(RegisterUser registerUser) 
    		throws OrganizerValidationException {
        
		Organizer organizer = new Organizer();
        
        //creating organizer
        organizer.setEmail(registerUser.getEmail());
        organizer.setPassword(registerUser.getPassword());
        organizer.setName(registerUser.getName());
        organizer.setSurname(registerUser.getSurname());
        organizer.setPicture(registerUser.getPicture());
        organizer.setResidency(registerUser.getResidency());
        organizer.setPhoneNumber(registerUser.getPhoneNumber());
        organizer.setIsDeleted(false);
        organizer.setSuspensionEndDate(null);
        organizer.setLastPasswordResetDate(null);
        organizer.setRole(roleRepository.findByName("ORGANIZER_ROLE"));

        isDataCorrect(organizer, false);
        
        //encoding password before storing it
        organizer.setPassword(this.encoder.encode(organizer.getPassword()));
        
        return organizerRepository.save(organizer);
    }
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Organizer updateOrganizer(AuthentifiedUser user, UpdateUser updateUser) 
			throws OrganizerValidationException {

		Organizer organizer = (Organizer) user;
		
		//updating organizer
        organizer.setName(updateUser.getName());
        organizer.setSurname(updateUser.getSurname());
        organizer.setPicture(updateUser.getPicture());
        organizer.setResidency(updateUser.getResidency());
        organizer.setPhoneNumber(updateUser.getPhoneNumber());
		
		isDataCorrect(organizer, true);
		
		return organizerRepository.save(organizer);
	}
	
	private boolean isDataCorrect(Organizer organizer, boolean isUpdate) throws OrganizerValidationException {
		
		if (!Pattern.matches("^(?=.{1,254}$)(?=.{1,64}@)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", organizer.getEmail())) {
			throw new OrganizerValidationException("Email is not of valid format.");
		}
		if(!isUpdate) {
			if (userRepository.findByEmail(organizer.getEmail()) != null) {
	            throw new OrganizerValidationException("That email is already taken.");
	        }
			if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$", organizer.getPassword())) {
				throw new OrganizerValidationException("Password is not of valid format.");
			}
		}
		if (!Pattern.matches("^[a-zA-Z]{1,50}$", organizer.getName())) {
			throw new OrganizerValidationException("Name is not of valid format.");
		}
		if (!Pattern.matches("^[a-zA-Z]{1,50}$", organizer.getSurname())) {
			throw new OrganizerValidationException("Surname is not of valid format.");
		}
		if (!Pattern.matches("^[A-Za-z][A-Za-z\\-\\' ]*[A-Za-z], [A-Za-z][A-Za-z\\-\\' ]*[A-Za-z]$", organizer.getResidency()) || organizer.getResidency().length() > 150)
    	    throw new EventValidationException("Residency must be in the format 'City, Country' with no leading/trailing spaces and only letters, spaces, hyphens, or apostrophes.");
		
		
		if (!Pattern.matches("^\\+?[0-9\\s()-]{7,15}$", organizer.getPhoneNumber())) {
			throw new OrganizerValidationException("Phone number is not of valid format.");
		}
		
		return true;
	}
}