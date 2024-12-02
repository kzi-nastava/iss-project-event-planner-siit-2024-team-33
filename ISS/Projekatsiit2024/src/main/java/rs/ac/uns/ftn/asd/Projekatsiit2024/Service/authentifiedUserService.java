package rs.ac.uns.ftn.asd.Projekatsiit2024.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.transaction.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.CreatedUserDTO;

public class authentifiedUserService {

	@Autowired
	AuthentifiedUserRepository UserRepo;
	
	@Transactional
    public AuthentifiedUser createAuthentifiedUser(String email,
    		String password,
    		String name,
    		String surname,
    		List<String> pictures) {
        AuthentifiedUser user = new AuthentifiedUser();
        if (UserRepo.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("That email is already taken.");
        }
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        user.setPictures(pictures);


        return UserRepo.saveAndFlush(user);
    }
	
	public void blockAUser(Integer blockerID, Integer blockedID) {
	    Optional<AuthentifiedUser> blockerOptional = UserRepo.findById(blockerID);
	    Optional<AuthentifiedUser> blockedOptional = UserRepo.findById(blockedID);

	    if (blockerOptional.isPresent() && blockedOptional.isPresent()) {
	        AuthentifiedUser blocker = blockerOptional.get();
	        AuthentifiedUser blocked = blockedOptional.get();
	        
	        List<AuthentifiedUser> blockedUsers = blocker.getBlockedUsers();
	        if(!blockedUsers.contains(blocked)) {
	        	blockedUsers.add(blocked);
	        	blocker.setBlockedUsers(blockedUsers);
	        	UserRepo.save(blocker);
	        }else {
	        	throw new IllegalArgumentException("One of two users not found.");
	        }
	    }
		
		
		
	}
	
	
	
	
	
}
