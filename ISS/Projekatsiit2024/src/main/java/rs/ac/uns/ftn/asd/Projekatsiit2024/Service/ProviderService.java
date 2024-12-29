package rs.ac.uns.ftn.asd.Projekatsiit2024.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Exception.UserCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.ProviderRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.CreateOrganizerDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.CreateProviderDTO;

@Service
public class ProviderService {
	@Autowired
    private AuthentifiedUserRepository userRepository;
	
	@Autowired
    private ProviderRepository providerRepository;
	
	public Provider get(Integer id) {
		Optional<Provider> p = providerRepository.findById(id);
		if(p.isEmpty())
			throw new EntityNotFoundException("Provider with that ID doesn't exist");
		
		return p.get();
	}
	
	@Transactional
    public Provider createProvider(CreateProviderDTO providerDTO) throws UserCreationException {
        
		Provider provider = new Provider();
        
        isDataCorrect(providerDTO);
        
        //creating provider
        provider.setEmail(providerDTO.getEmail());
        provider.setPassword(providerDTO.getPassword());
        provider.setName(providerDTO.getName());
        provider.setSurname(providerDTO.getSurname());
        provider.setPictures(providerDTO.getPictures());
        provider.setDescription(providerDTO.getDescription());
        provider.setPhoneNumber(providerDTO.getPhoneNumber());
        provider.setProviderName(providerDTO.getProviderName());
        provider.setResidency(providerDTO.getResidency());
        
        return providerRepository.saveAndFlush(provider);
    }
	
	
	//TODO: validation of data
	private boolean isDataCorrect(CreateProviderDTO providerDTO) throws UserCreationException {
		if (userRepository.findByEmail(providerDTO.getEmail()).isPresent()) {
            throw new UserCreationException("That email is already taken.");
        }
		
		return true;
	}
}
