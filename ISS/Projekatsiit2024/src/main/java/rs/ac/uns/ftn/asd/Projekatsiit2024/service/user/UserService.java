package rs.ac.uns.ftn.asd.Projekatsiit2024.service.user;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.RegisterUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.UpdatePassword;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.UpdateUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.UpdatedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.AuthentifiedUserValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.InvalidPasswordException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.InvalidPasswordFormatException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.OrganizerValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.ProviderValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.UserCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.UserUpdateException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;

@Service
public class UserService {
	
	@Autowired
	private AuthentifiedUserService authentifiedUserService;
	
	@Autowired
	private OrganizerService organizerService;
	
	@Autowired
	private ProviderService providerService;
	
	@Autowired
	private AuthentifiedUserRepository userRepo;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	//creates user of which ever role wanted
	@Transactional(propagation = Propagation.REQUIRED)
	public AuthentifiedUser registerUser(RegisterUser registerUser) throws Exception, 
		UserCreationException, OrganizerValidationException, ProviderValidationException, AuthentifiedUserValidationException {
		
		AuthentifiedUser registeredUser = null;
		//what type of user is it
		if (registerUser.getRole() == null) {
			throw new UserCreationException("The user with such role doesn't exist.");
		}
		
		switch(registerUser.getRole()) {
		
			case "AUSER_ROLE":
				registeredUser = authentifiedUserService.createAuthentifiedUser(registerUser);
				break;
		
			case "ORGANIZER_ROLE":
				registeredUser = organizerService.createOrganizer(registerUser);
				break;
				
			case "PROVIDER_ROLE":
				registeredUser = providerService.createProvider(registerUser);
				break;
			
			default:
					throw new UserCreationException("The user with such role doesn't exist.");
		}
		
		return registeredUser;
	}
	
	
	//updates user of which role it is
	@Transactional(propagation = Propagation.REQUIRED)
	public UpdatedUser updateUser(AuthentifiedUser user, UpdateUser updateUser) throws  
	UserUpdateException, AuthentifiedUserValidationException, OrganizerValidationException, 
	ProviderValidationException {
		UpdatedUser updatedUser = null;
		
		if (user.getRole() == null) {
			throw new UserUpdateException("The user with such role doesn't exist.");
		}
		
		switch(user.getRole().toString()) {
		case "AUSER_ROLE":
			AuthentifiedUser aUser = authentifiedUserService.updateAuthentifiedUser(user, updateUser);
			updatedUser = new UpdatedUser(aUser);
			break;

		case "ORGANIZER_ROLE":
			Organizer organizer = organizerService.updateOrganizer(user, updateUser);
			updatedUser = new UpdatedUser(organizer);
			break;

		case "PROVIDER_ROLE":
			Provider provider = providerService.updateProvider(user, updateUser);
			updatedUser = new UpdatedUser(provider);
			break;

		default:
			throw new UserUpdateException("The user with such role doesn't exist.");
		}
		
		return updatedUser;
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public AuthentifiedUser updatePassword(AuthentifiedUser user, UpdatePassword updatePassword) throws UserUpdateException, InvalidPasswordException, InvalidPasswordFormatException {
		AuthentifiedUser updatedUser = null;
		
		if (user.getRole() == null) {
			throw new UserUpdateException("The user with such role doesn't exist.");
		}
		
		if (!encoder.matches(updatePassword.getOldPassword(), user.getPassword())) {
			throw new InvalidPasswordException("Old password doesn't match.");
		}
		
		if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$", updatePassword.getNewPassword())) {
			throw new InvalidPasswordFormatException("Password is not of valid format.");
		}
		
		switch(user.getRole().toString()) {
		case "AUSER_ROLE":
			AuthentifiedUser aUser = user;
			aUser.setPassword(encoder.encode(updatePassword.getNewPassword()));
			aUser.setLastPasswordResetDate(new Timestamp(System.currentTimeMillis()));
			updatedUser = userRepo.saveAndFlush(aUser);
			break;

		case "ORGANIZER_ROLE":
			Organizer organizer = (Organizer) user;
			organizer.setPassword(encoder.encode(updatePassword.getNewPassword()));
			organizer.setLastPasswordResetDate(new Timestamp(System.currentTimeMillis()));
			updatedUser = userRepo.saveAndFlush(organizer);
			break;

		case "PROVIDER_ROLE":
			Provider provider = (Provider) user;
			provider.setPassword(encoder.encode(updatePassword.getNewPassword()));
			provider.setLastPasswordResetDate(new Timestamp(System.currentTimeMillis()));
			updatedUser = userRepo.saveAndFlush(provider);
			break;

		default:
			throw new UserUpdateException("The user with such role doesn't exist.");
		}
		
		return updatedUser;
	}
	
	public Timestamp getUpdatedPasswordDate(AuthentifiedUser user) {
		return userRepo.findLastPasswordResetDateByEmail(user.getEmail());
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public AuthentifiedUser terminateUser(AuthentifiedUser user) throws UserUpdateException {
		AuthentifiedUser updatedUser = null;
		
		if (user.getRole() == null) {
			throw new UserUpdateException("The user with such role doesn't exist.");
		}
		
		switch(user.getRole().toString()) {
		case "AUSER_ROLE":
			AuthentifiedUser aUser = user;
			aUser.setIsDeleted(true);
			updatedUser = userRepo.save(aUser);
			break;

		case "ORGANIZER_ROLE":
			Organizer organizer = (Organizer) user;
			organizer.setIsDeleted(true);
			updatedUser = userRepo.save(organizer);
			break;

		case "PROVIDER_ROLE":
			Provider provider = (Provider) user;
			provider.setIsDeleted(true);
			updatedUser = userRepo.save(provider);
			break;

		default:
			throw new UserUpdateException("The user with such role doesn't exist.");
		}
		
		return updatedUser;
	}
	
		
	public void blockAUser(Integer blockerID, Integer blockedID) {
	    Optional<AuthentifiedUser> blockerOptional = userRepo.findById(blockerID);
	    Optional<AuthentifiedUser> blockedOptional = userRepo.findById(blockedID);

	    if (blockerOptional.isPresent() && blockedOptional.isPresent()) {
	        AuthentifiedUser blocker = blockerOptional.get();
	        AuthentifiedUser blocked = blockedOptional.get();
	        
	        List<AuthentifiedUser> blockedUsers = blocker.getBlockedUsers();
	        if(!blockedUsers.contains(blocked)) {
	        	blockedUsers.add(blocked);
	        	blocker.setBlockedUsers(blockedUsers);
	        	userRepo.save(blocker);
	        }else {
	        	throw new IllegalArgumentException("One of two users not found.");
	        }
	    }
	}
	
	public void unblockAUser(Integer blockerID, Integer blockedID) {
	    Optional<AuthentifiedUser> blockerOptional = userRepo.findById(blockerID);
	    Optional<AuthentifiedUser> blockedOptional = userRepo.findById(blockedID);

	    if (blockerOptional.isPresent() && blockedOptional.isPresent()) {
	        AuthentifiedUser blocker = blockerOptional.get();
	        AuthentifiedUser blocked = blockedOptional.get();
	        
	        List<AuthentifiedUser> blockedUsers = blocker.getBlockedUsers();
	        if(blockedUsers.contains(blocked)) {
	            blockedUsers.remove(blocked);
	            blocker.setBlockedUsers(blockedUsers);
	            userRepo.save(blocker);
	        }else {
	        	throw new IllegalArgumentException("One of two users not found.");
	        }
	    }
	}
}
