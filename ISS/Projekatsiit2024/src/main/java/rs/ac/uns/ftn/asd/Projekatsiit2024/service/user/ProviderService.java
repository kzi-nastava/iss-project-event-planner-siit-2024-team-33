package rs.ac.uns.ftn.asd.Projekatsiit2024.service.user;

import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.RegisterUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.UpdateUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.ProviderValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
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
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	public Provider get(Integer id) {
		Optional<Provider> p = providerRepository.findById(id);
		if(p.isEmpty())
			throw new EntityNotFoundException("Provider with that ID doesn't exist");
		
		return p.get();
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
    public Provider createProvider(RegisterUser registerUser) throws ProviderValidationException {
        
		Provider provider = new Provider();
        
        //creating provider
        provider.setEmail(registerUser.getEmail());
        provider.setPassword(registerUser.getPassword());
        provider.setName(registerUser.getName());
        provider.setSurname(registerUser.getSurname());
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
        
        isDataCorrect(provider, false);
        
        //encoding password before storing it
        provider.setPassword(this.encoder.encode(provider.getPassword()));
        
        return providerRepository.save(provider);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Provider updateProvider(AuthentifiedUser user, UpdateUser updateUser) 
			throws ProviderValidationException{
		
		Provider provider = (Provider) user;
		
		//updating provider
        provider.setName(updateUser.getName());
        provider.setSurname(updateUser.getSurname());
        provider.setPicture(updateUser.getPicture());
        provider.setPictures(updateUser.getPictures());
        provider.setDescription(updateUser.getDescription());
        provider.setPhoneNumber(updateUser.getPhoneNumber());
        provider.setProviderName(updateUser.getProviderName());
        provider.setResidency(updateUser.getResidency());
		
		isDataCorrect(provider, true);
		
		return providerRepository.save(provider);
	}
	
	private boolean isDataCorrect(Provider provider, boolean isUpdate) throws ProviderValidationException {
		if (!Pattern.matches("^(?=.{1,254}$)(?=.{1,64}@)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", provider.getEmail())) {
			throw new ProviderValidationException("Email is not of valid format.");
		}
		if(!isUpdate) {
			if (userRepository.findByEmail(provider.getEmail()) != null) {
	            throw new ProviderValidationException("That email is already taken.");
	        }
			if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$", provider.getPassword())) {
				throw new ProviderValidationException("Password is not of valid format.");
			}
		}
		if (!Pattern.matches("^[a-zA-Z]{1,50}$", provider.getName())) {
			throw new ProviderValidationException("Name is not of valid format.");
		}
		if (!Pattern.matches("^[a-zA-Z]{1,50}$", provider.getSurname())) {
			throw new ProviderValidationException("Surname is not of valid format.");
		}
		if (!Pattern.matches("^[A-Za-z][A-Za-z\\-\\' ]*[A-Za-z], [A-Za-z][A-Za-z\\-\\' ]*[A-Za-z]$", provider.getResidency()) || provider.getResidency().length() > 150)
    	    throw new EventValidationException("Residency must be in the format 'City, Country' with no leading/trailing spaces and only letters, spaces, hyphens, or apostrophes.");
		
		if (!Pattern.matches("^\\+?[0-9\\s()-]{7,15}$", provider.getPhoneNumber())) {
			throw new ProviderValidationException("Phone number is not of valid format.");
		}
		if (!Pattern.matches("^.{20,}$", provider.getDescription())) {
			throw new ProviderValidationException("Description is not of valid format.");
		}
		if (!Pattern.matches("^[a-zA-Z0-9]{0,20}$", provider.getProviderName())) {
			throw new ProviderValidationException("Provider name is not of valid format.");
		}
		
		return true;
	}
}
