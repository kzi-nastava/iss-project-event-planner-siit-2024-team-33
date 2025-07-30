package rs.ac.uns.ftn.asd.Projekatsiit2024.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;

@Service
public class AuthenticationService implements UserDetailsService {
	
	@Autowired
	private AuthentifiedUserRepository aUserRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AuthentifiedUser user = aUserRepository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user with email '%s' found.", username));
		}
		else {
			return new UserPrincipal(user);
		}
	}

	public Boolean isEmailAvailable(String email) {
		AuthentifiedUser user = aUserRepository.findByEmail(email);
		if (user == null || user.getIsVerified() == false) {
			return false;
		}
		else {
			return true;
		}
	}
}
