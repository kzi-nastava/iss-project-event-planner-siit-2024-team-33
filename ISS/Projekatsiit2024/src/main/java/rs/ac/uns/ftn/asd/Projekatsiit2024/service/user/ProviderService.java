package rs.ac.uns.ftn.asd.Projekatsiit2024.service.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.ProviderValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.UserDeletionException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.OfferReservationRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.auth.RoleRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.ProviderRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.utils.ImageManager;

@Service
public class ProviderService {
	
	@Autowired
    private AuthentifiedUserRepository userRepository;
	
	@Autowired
    private ProviderRepository providerRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private OfferReservationRepository offerReservationRepository;
	
	@Autowired
	private OfferRepository offerRepository;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	public Provider get(Integer id) {
		Optional<Provider> p = providerRepository.findById(id);
		if(p.isEmpty())
			throw new EntityNotFoundException("Provider with that ID doesn't exist");
		
		return p.get();
	}
	
	
    public Provider createProvider(RegisterUser registerUser) throws ProviderValidationException {
        
		LocalDateTime thresholdDate = LocalDateTime.now().minusHours(24);

        // Check for existing email usage
        if (userRepository.findIfEmailForRegistrationExists(registerUser.getEmail(), thresholdDate) != null) {
            throw new ProviderValidationException("That email is already taken.");
        }

        // Remove old unverified user (any subclass)
        AuthentifiedUser oldUnverified = userRepository.findOldUnverifiedUserByEmail(registerUser.getEmail(), thresholdDate);
        if (oldUnverified != null) {
            userRepository.deleteById(oldUnverified.getId());
        }
		
		Provider provider = new Provider();
        
        //creating provider
        provider.setEmail(registerUser.getEmail());
        provider.setPassword(registerUser.getPassword());
        provider.setName(registerUser.getName());
        provider.setSurname(registerUser.getSurname());
        provider.setDescription(registerUser.getDescription());
        provider.setPhoneNumber(registerUser.getPhoneNumber());
        provider.setProviderName(registerUser.getProviderName());
        provider.setResidency(registerUser.getResidency());
        provider.setIsDeleted(false);
        provider.setIsVerified(false);
        provider.setSuspensionEndDate(null);
        provider.setLastPasswordResetDate(null);
        provider.setDateOfCreation(LocalDateTime.now());
        provider.setRole(roleRepository.findByName("PROVIDER_ROLE"));
        
        isDataCorrect(provider, false);
        
        //encoding password before storing it
        provider.setPassword(this.encoder.encode(provider.getPassword()));
        
        //it's either null or uploaded filename
        provider.setPicture(ImageManager.saveAsFile(registerUser.getPicture()));
        //it's either going to be empty list of filenames or some or maybe all filenames
        List<String> pictures = new ArrayList<>();
        if (registerUser.getPictures() != null) {
	        for (String base64Img: registerUser.getPictures()) {
	        	String fileName = ImageManager.saveAsFile(base64Img);
	        	if (fileName != null)
	        		pictures.add(fileName);
	        }
        }
        provider.setPictures(pictures);
        
        return providerRepository.save(provider);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Provider updateProvider(AuthentifiedUser user, UpdateUser updateUser) 
			throws ProviderValidationException{
		
		Provider provider = (Provider) user;
		
		//updating provider
        provider.setName(updateUser.getName());
        provider.setSurname(updateUser.getSurname());
        provider.setDescription(updateUser.getDescription());
        provider.setPhoneNumber(updateUser.getPhoneNumber());
        provider.setProviderName(updateUser.getProviderName());
        provider.setResidency(updateUser.getResidency());
		
		isDataCorrect(provider, true);
		
        provider.setPicture(ImageManager.saveAsFile(updateUser.getPicture()));
        List<String> newPictures = new ArrayList<>();
        if (updateUser.getPictures() != null) {
            for (String base64Img : updateUser.getPictures()) {
                String fileName = ImageManager.saveAsFile(base64Img);
                if (fileName != null)
                    newPictures.add(fileName);
            }
        }
        provider.setPictures(newPictures);
		
		return providerRepository.save(provider);
	}
	
	private boolean isDataCorrect(Provider provider, boolean isUpdate) throws ProviderValidationException {
		if (!Pattern.matches("^(?=.{1,254}$)(?=.{1,64}@)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", provider.getEmail())) {
			throw new ProviderValidationException("Email is not of valid format.");
		}
		
		if(!isUpdate) {
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
    	    throw new ProviderValidationException("Residency must be in the format 'City, Country' with no leading/trailing spaces and only letters, spaces, hyphens, or apostrophes.");
		
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
	
	@Transactional
	public void deleteProvider(Integer userId) {
		Optional<Provider> optionalProvider = providerRepository.findById(userId);
		
		if (optionalProvider.isEmpty())
			throw new UserDeletionException("There is no such provider to delete.");
		Provider provider = optionalProvider.get();
		
		//does provider have any services in the future
		if (offerReservationRepository.existsUpcomingServiceReservationsByProviderId(userId)) {
	        throw new UserDeletionException("Cannot delete provider with upcoming or ongoing service reservations.");
	    }
	    
	    provider.setIsDeleted(true);
	    providerRepository.save(provider);

	    for (Offer offer : provider.getOffers()) {
	        offer.setIsDeleted(true);
	        offerRepository.save(offer);
	    }
	}
}