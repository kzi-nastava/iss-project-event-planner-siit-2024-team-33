package rs.ac.uns.ftn.asd.Projekatsiit2024.service.user;

import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.RegisterUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.ProviderCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.auth.RoleRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.ProviderRepository;

@Service
public class ProviderService {
	
	@Autowired
    private AuthentifiedUserRepository userRepository;
	
	@Autowired
    private ProviderRepository providerRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
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
        provider.setCity(registerUser.getResidency().split(" ")[0]);
        provider.setPicture(registerUser.getPicture());
        provider.setPictures(registerUser.getPictures());
        provider.setDescription(registerUser.getDescription());
        provider.setPhoneNumber(registerUser.getPhoneNumber());
        provider.setProviderName(registerUser.getProviderName());
        provider.setResidency(registerUser.getResidency());
        provider.setIsDeleted(false);
        provider.setSuspensionEndDate(null);
        provider.setLastPasswordResetDate(null);
        provider.setRole(roleRepository.findByName("PROVIDER_ROLE"));
        
        return providerRepository.save(provider);
	}
	
	private boolean isDataCorrect(RegisterUser registerUser) throws ProviderCreationException {
		if (!Pattern.matches("^(?=.{1,254}$)(?=.{1,64}@)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", registerUser.getEmail())) {
			throw new ProviderCreationException("Email is not of valid format.");
		}
		if (userRepository.findByEmail(registerUser.getEmail()) != null) {
            throw new ProviderCreationException("That email is already taken.");
        }
		if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$", registerUser.getPassword())) {
			throw new ProviderCreationException("Password is not of valid format.");
		}
		if (!Pattern.matches("^[a-zA-Z]{1,50}$", registerUser.getName())) {
			throw new ProviderCreationException("Name is not of valid format.");
		}
		if (!Pattern.matches("^[a-zA-Z]{1,50}$", registerUser.getSurname())) {
			throw new ProviderCreationException("Surname is not of valid format.");
		}
		if (!Pattern.matches("^[A-Za-z\\s,]{1,50}$", registerUser.getResidency())) {
			throw new ProviderCreationException("Residency is not of valid format.");
		}
		if (!Pattern.matches("^\\+?[0-9\\s()-]{7,15}$", registerUser.getPhoneNumber())) {
			throw new ProviderCreationException("Phone number is not of valid format.");
		}
		if (!Pattern.matches("^.{20,}$", registerUser.getDescription())) {
			throw new ProviderCreationException("Description is not of valid format.");
		}
		if (!Pattern.matches("^[a-zA-Z0-9]{0,20}$", registerUser.getProviderName())) {
			throw new ProviderCreationException("Provider name is not of valid format.");
		}
		
		return true;
	}
}
