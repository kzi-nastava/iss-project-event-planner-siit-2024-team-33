package rs.ac.uns.ftn.asd.Projekatsiit2024.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Exception.UserCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.OrganizerRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.CreateOrganizerDTO;

@Service
public class OrganizerService {
	@Autowired
    private AuthentifiedUserRepository userRepository;
	
	@Autowired
    private OrganizerRepository organizerRepository;
	
	@Transactional
    public Organizer createOrganizer(CreateOrganizerDTO organizerDTO) throws UserCreationException {
        
		Organizer organizer = new Organizer();
        
        isDataCorrect(organizerDTO);
        
        //creating organizer
        organizer.setEmail(organizerDTO.getEmail());
        organizer.setPassword(organizerDTO.getPassword());
        organizer.setName(organizerDTO.getName());
        organizer.setSurname(organizerDTO.getSurname());
        organizer.setPicture(organizerDTO.getPicture());
        organizer.setResidency(organizerDTO.getResidency());
        organizer.setPhoneNumber(organizerDTO.getPhoneNumber());
        
        return organizerRepository.saveAndFlush(organizer);
    }
	
	
	//TODO: validation of data
	private boolean isDataCorrect(CreateOrganizerDTO organizerDTO) throws UserCreationException {
		if (userRepository.findByEmail(organizerDTO.getEmail()).isPresent()) {
            throw new UserCreationException("That email is already taken.");
        }
		
		return true;
	}

}
