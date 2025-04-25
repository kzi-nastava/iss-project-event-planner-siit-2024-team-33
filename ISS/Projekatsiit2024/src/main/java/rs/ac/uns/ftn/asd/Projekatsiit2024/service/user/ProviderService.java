package rs.ac.uns.ftn.asd.Projekatsiit2024.service.user;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.ProviderCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.ProviderRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.auth.RegisterUser;

@Service
public class ProviderService {
	
	@Autowired
    private AuthentifiedUserRepository userRepository;
	
	@Autowired
    private ProviderRepository providerRepository;
	
	
	public ProviderService() {
	}
	
	public Provider get(Integer id) {
		Optional<Provider> p = providerRepository.findById(id);
		if(p.isEmpty())
			throw new EntityNotFoundException("Provider with that ID doesn't exist");
		
		return p.get();
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
    public Provider createProvider(RegisterUser registerUser) throws ProviderCreationException {
        
		Provider provider = new Provider();
        
        isDataCorrect(registerUser);
        
        //creating provider
        provider.setEmail(registerUser.getEmail());
        provider.setPassword(registerUser.getPassword());
        provider.setName(registerUser.getName());
        provider.setSurname(registerUser.getSurname());
        provider.setPictures(registerUser.getPictures());
        provider.setDescription(registerUser.getDescription());
        provider.setPhoneNumber(registerUser.getPhoneNumber());
        provider.setProviderName(registerUser.getProviderName());
        provider.setResidency(registerUser.getResidency());
        
        return providerRepository.save(provider);
	}
	
	
	//TODO: validation of data
	private boolean isDataCorrect(RegisterUser registerUser) throws ProviderCreationException {
		if (userRepository.findByEmail(registerUser.getEmail()).isPresent()) {
	        throw new ProviderCreationException("That email is already taken.");
	    }
		
		return true;
	}
}
