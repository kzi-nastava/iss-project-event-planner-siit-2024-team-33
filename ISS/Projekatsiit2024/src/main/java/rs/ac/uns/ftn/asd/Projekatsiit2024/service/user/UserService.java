package rs.ac.uns.ftn.asd.Projekatsiit2024.service.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.RegisterUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.AuthentifiedUserCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.OrganizerCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.ProviderCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.UserCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
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
	
	//creates user of which ever role wanted
	@Transactional(propagation = Propagation.REQUIRED)
	public AuthentifiedUser registerUser(RegisterUser registerUser) throws Exception, 
		UserCreationException, OrganizerCreationException, ProviderCreationException, AuthentifiedUserCreationException {
		
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
