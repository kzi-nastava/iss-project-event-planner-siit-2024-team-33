package rs.ac.uns.ftn.asd.Projekatsiit2024.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.auth.RegisterUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.AuthentifiedUserCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.OrganizerCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.ProviderCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.UserCreationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.User;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.RoleRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.UserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.user.AuthentifiedUserService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.user.OrganizerService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.user.ProviderService;

@Service
public class AuthenticationService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private OrganizerService organizerService;
	
	@Autowired
	private ProviderService providerService;
	
	@Autowired
	private AuthentifiedUserService authentifiedUserService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		}
		else {
			return user;
		}
	}
	
	//creates both user with user details(authentication and authorization)
	//and specific user entity with all his data
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
				this.createUser(registerUser);
				break;
		
			case "ORGANIZER_ROLE":
				
				registeredUser = organizerService.createOrganizer(registerUser);
				this.createUser(registerUser);
				break;
				
			case "PROVIDER_ROLE":
				
				registeredUser = providerService.createProvider(registerUser);
				this.createUser(registerUser);
				break;
			
			default:
					throw new UserCreationException("The user with such role doesn't exist.");
		}
		
		return registeredUser;
	}
	
	
	//creates user with user details for easy authentication and authorization
	@Transactional(propagation = Propagation.REQUIRED)
	private UserDetails createUser(RegisterUser registerUser) {
		User user = new User();
		
		user.setUsername(registerUser.getEmail());
		//before putting password into atribute we need to hash it so that we would have hashed
		//password in data base(use the same password encoder bean used for authentication manager)
		user.setPassword(passwordEncoder.encode(registerUser.getPassword()));
		//role of the user
		user.setRole(roleRepository.findByName(registerUser.getRole()));
		
		userRepository.save(user);
		
		return user;
	
	}
}
