package rs.ac.uns.ftn.asd.Projekatsiit2024.Service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer.UserDetailsBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.AuthentifiedUserRepository;


public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private AuthentifiedUserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<AuthentifiedUser> user = userRepo.findByEmail(username);
		if(user.isEmpty())
			throw new UsernameNotFoundException("User with email doesn't exist");
		
		return new User(user.get().getEmail(), user.get().getPassword(), new ArrayList<GrantedAuthority>());
	}
}
