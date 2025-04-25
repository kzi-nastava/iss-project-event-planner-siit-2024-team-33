package rs.ac.uns.ftn.asd.Projekatsiit2024.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.auth.RegisterUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.OrganizerCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OrganizerRepository;

@Service
public class OrganizerService {
	@Autowired
    private AuthentifiedUserRepository userRepository;
	
	@Autowired
    private OrganizerRepository organizerRepository;
	
	@Transactional(propagation = Propagation.REQUIRED)
    public Organizer createOrganizer(RegisterUser registerUser) throws OrganizerCreationException {
        
		Organizer organizer = new Organizer();
        
        isDataCorrect(registerUser);
        
        //creating organizer
        organizer.setEmail(registerUser.getEmail());
        organizer.setPassword(registerUser.getPassword());
        organizer.setName(registerUser.getName());
        organizer.setSurname(registerUser.getSurname());
        organizer.setPicture(registerUser.getPicture());
        organizer.setResidency(registerUser.getResidency());
        organizer.setPhoneNumber(registerUser.getPhoneNumber());
        
        return organizerRepository.save(organizer);
    }
	
	
	//TODO: validation of data
	private boolean isDataCorrect(RegisterUser registerUser) throws OrganizerCreationException {
		if (userRepository.findByEmail(registerUser.getEmail()).isPresent()) {
            throw new OrganizerCreationException("That email is already taken.");
        }
		
		return true;
	}

}
