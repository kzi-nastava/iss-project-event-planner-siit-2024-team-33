package rs.ac.uns.ftn.asd.Projekatsiit2024.service.user;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.RegisterUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.UpdateUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.OrganizerValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.UserDeletionException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.auth.RoleRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.event.EventRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.OrganizerRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.utils.ImageManager;

@Service
public class OrganizerService {
	@Autowired
    private AuthentifiedUserRepository userRepository;
	
	@Autowired
    private OrganizerRepository organizerRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private EventRepository eventRepository;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	
    public Organizer createOrganizer(RegisterUser registerUser) 
    		throws OrganizerValidationException {
    	
    	if (registerUser.getEmail() == null)
    		throw new OrganizerValidationException("Email is not of valid format.");
		
		LocalDateTime thresholdDate = LocalDateTime.now().minusHours(24);

        // Check for existing email usage (verified or unverified within 24h)
        if (userRepository.findIfEmailForRegistrationExists(registerUser.getEmail(), thresholdDate) != null) {
            throw new OrganizerValidationException("That email is already taken.");
        }

        // Remove old unverified user (any subclass)
        AuthentifiedUser oldUnverified = userRepository.findOldUnverifiedUserByEmail(registerUser.getEmail(), thresholdDate);
        if (oldUnverified != null) {
            userRepository.deleteById(oldUnverified.getId());
        }
        
		Organizer organizer = new Organizer();
        
        //creating organizer
        organizer.setEmail(registerUser.getEmail());
        organizer.setPassword(registerUser.getPassword());
        organizer.setName(registerUser.getName());
        organizer.setSurname(registerUser.getSurname());
        organizer.setResidency(registerUser.getResidency());
        organizer.setPhoneNumber(registerUser.getPhoneNumber());
        organizer.setIsDeleted(false);
        organizer.setIsVerified(false);
        organizer.setSuspensionEndDate(null);
        organizer.setLastPasswordResetDate(null);
        organizer.setDateOfCreation(LocalDateTime.now());
        organizer.setRole(roleRepository.findByName("ORGANIZER_ROLE"));

        isDataCorrect(organizer, false);
        
        //encoding password before storing it
        organizer.setPassword(this.encoder.encode(organizer.getPassword()));
        
        //it's either null or uploaded filename
        organizer.setPicture(ImageManager.saveAsFile(registerUser.getPicture()));
        
        return organizerRepository.save(organizer);
    }
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Organizer updateOrganizer(AuthentifiedUser user, UpdateUser updateUser) 
			throws OrganizerValidationException {

		Organizer organizer = (Organizer) user;
		
		//updating organizer
        organizer.setName(updateUser.getName());
        organizer.setSurname(updateUser.getSurname());
        organizer.setResidency(updateUser.getResidency());
        organizer.setPhoneNumber(updateUser.getPhoneNumber());
		
		isDataCorrect(organizer, true);
		
        organizer.setPicture(ImageManager.saveAsFile(updateUser.getPicture()));
		
		return organizerRepository.save(organizer);
	}
	
	private boolean isDataCorrect(Organizer organizer, boolean isUpdate) throws OrganizerValidationException {
		
		if (organizer.getEmail() == null || !Pattern.matches("^(?=.{1,254}$)(?=.{1,64}@)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", organizer.getEmail())) {
			throw new OrganizerValidationException("Email is not of valid format.");
		}
		
		if(!isUpdate) {
			if (organizer.getPassword() == null || !Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$", organizer.getPassword())) {
				throw new OrganizerValidationException("Password is not of valid format.");
			}
		}
		
		if (organizer.getName() == null || !Pattern.matches("^[a-zA-Z]{1,50}$", organizer.getName())) {
			throw new OrganizerValidationException("Name is not of valid format.");
		}
		if (organizer.getSurname() == null || !Pattern.matches("^[a-zA-Z]{1,50}$", organizer.getSurname())) {
			throw new OrganizerValidationException("Surname is not of valid format.");
		}
		if (organizer.getResidency() == null || !Pattern.matches("^[A-Za-z][A-Za-z\\-\\' ]*[A-Za-z], [A-Za-z][A-Za-z\\-\\' ]*[A-Za-z]$", organizer.getResidency()) || organizer.getResidency().length() > 150)
    	    throw new OrganizerValidationException("Residency must be in the format 'City, Country' with no leading/trailing spaces and only letters, spaces, hyphens, or apostrophes.");
		
		
		if (organizer.getPhoneNumber() == null || !Pattern.matches("^\\+?[0-9\\s()-]{7,15}$", organizer.getPhoneNumber())) {
			throw new OrganizerValidationException("Phone number is not of valid format.");
		}
		
		return true;
	}
	
	
	
	@Transactional
	public void deleteOrganizer(Integer userId) {
		Optional<Organizer> optionalOrganizer = organizerRepository.findById(userId);
		
		if (optionalOrganizer.isEmpty())
			throw new UserDeletionException("There is no such organizer to delete.");
		Organizer organizer = optionalOrganizer.get();
		
		//does organizer have any organized events in the future
		if (eventRepository.existsFutureOrOngoingEventsByOrganizerId(userId)) {
	        throw new UserDeletionException("Cannot delete organizer with ongoing or future events.");
	    }
		
		organizer.setIsDeleted(true);
	    organizerRepository.save(organizer);
	}
}