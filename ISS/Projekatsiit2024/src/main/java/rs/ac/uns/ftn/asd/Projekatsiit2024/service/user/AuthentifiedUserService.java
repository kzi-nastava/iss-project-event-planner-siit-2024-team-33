package rs.ac.uns.ftn.asd.Projekatsiit2024.service.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.auth.RegisterUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.AuthentifiedUserCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.AuthentifiedUserRepository;


@Service
public class AuthentifiedUserService {

	@Autowired
	AuthentifiedUserRepository userRepo;
	
	@Transactional(propagation = Propagation.REQUIRED)
    public AuthentifiedUser createAuthentifiedUser(RegisterUser registerUser) throws AuthentifiedUserCreationException {
        AuthentifiedUser user = new AuthentifiedUser();
        if (userRepo.findByEmail(registerUser.getEmail()).isPresent()) {
            throw new AuthentifiedUserCreationException("That email is already taken.");
        }
        user.setEmail(registerUser.getEmail());
        user.setPassword(registerUser.getPassword());
        user.setName(registerUser.getName());
        user.setSurname(registerUser.getSurname());
        user.setPictures(registerUser.getPictures());

        userRepo.save(user);
        return user;
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
