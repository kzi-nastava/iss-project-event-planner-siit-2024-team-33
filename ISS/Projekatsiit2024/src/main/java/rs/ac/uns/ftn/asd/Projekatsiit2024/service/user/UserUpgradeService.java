package rs.ac.uns.ftn.asd.Projekatsiit2024.service.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.UpgradeUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.user.UserUpgradeException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.offer.Offer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Organizer;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.UnverifiedUserUpgrade;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.auth.RoleRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.OrganizerRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.ProviderRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.UnverifiedUserUpgradeRepository;

@Service
public class UserUpgradeService {
	
	@Autowired
	AuthentifiedUserRepository userRepository;
	
	@Autowired
	UnverifiedUserUpgradeRepository uuuRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	OrganizerRepository organizerRepository;
	
	@Autowired
	ProviderRepository providerRepository;
	
	
	//creates upgrade user request which waits for verification
	public UnverifiedUserUpgrade createUpgradeUserRequest(UserPrincipal userPrincipal, UpgradeUser upgradeUser) 
			throws UserUpgradeException {
		//does user exist
		Integer userId = userPrincipal.getUser().getId();
		Optional<AuthentifiedUser> optionalUser = userRepository.findById(userId);
		if (optionalUser.isEmpty())
			throw new UserUpgradeException("No such user available for update.");
		AuthentifiedUser currentUser = optionalUser.get();
		
		
		
		//if upgrade request that is older than 24 hours exist delete it
		UnverifiedUserUpgrade oldUuu = uuuRepository.findByUser(currentUser);
		if (oldUuu != null && 
				oldUuu.getDateOfCreation().isBefore(LocalDateTime.now().minusHours(24)))
			uuuRepository.delete(oldUuu);
		
		//if upgrade request that is not older than 24 hours exist throw exception
		if (oldUuu != null 
				&& (oldUuu.getDateOfCreation().isAfter(LocalDateTime.now().minusHours(24))
				|| oldUuu.getDateOfCreation().isEqual(LocalDateTime.now().minusHours(24))))
			throw new UserUpgradeException("You already requested update.");
		
		
		//creating update request
		UnverifiedUserUpgrade uuu = new UnverifiedUserUpgrade();
		uuu.setDateOfCreation(LocalDateTime.now());
		uuu.setAUser(currentUser);
		uuu.setResidency(upgradeUser.getResidency());
		uuu.setPhoneNumber(upgradeUser.getPhoneNumber());
		uuu.setProviderName(upgradeUser.getProviderName());
		uuu.setDescription(upgradeUser.getDescription());
		
		if (upgradeUser.getRole().equals("ORGANIZER ROLE")) {
			uuu.setRole(roleRepository.findByName("ORGANIZER_ROLE"));
		}
		else if (upgradeUser.getRole().equals("PROVIDER_ROLE")) {
			uuu.setRole(roleRepository.findByName("PROVIDER_ROLE"));
		}
		else {
			throw new UserUpgradeException("You cannot upgrade to such role.");
		}
		
		return uuuRepository.save(uuu);
	}

	
	
	
	
	//called after opening verification link
	public void upgradeUserFinal(AuthentifiedUser userForUpgrade) {
		UnverifiedUserUpgrade uuu = uuuRepository.findByUser(userForUpgrade);
		if (uuu == null)
			throw new UserUpgradeException("There was no request for upgrade of your account.");
		
		String role = uuu.getRole().getName();
		
		if (role.equals("ORGANIZER_ROLE")) {
			this.upgradeToOrganizer(userForUpgrade, uuu);
		}
		else if (role.equals("PROVIDER_ROLE")) {
			this.upgradeToProvider(userForUpgrade, uuu);
		}
		else {
			throw new UserUpgradeException("You cannot upgrade to such role.");
		}
	}




	private Organizer upgradeToOrganizer(AuthentifiedUser userForUpgrade, UnverifiedUserUpgrade uuu) {
		Set<Offer> favoriteOffers = userForUpgrade.getFavoriteOffers();
		Set<Event> favoriteEvents = userForUpgrade.getFavoriteEvents();
		List<AuthentifiedUser> blockedUsers = userForUpgrade.getBlockedUsers();
		
		userRepository.delete(userForUpgrade); // marked for deletion

        Organizer organizer = new Organizer();
        organizer.setId(userForUpgrade.getId()); // reuse ID
        organizer.setEmail(userForUpgrade.getEmail());
        organizer.setPassword(userForUpgrade.getPassword());
        organizer.setName(userForUpgrade.getName());
        organizer.setSurname(userForUpgrade.getSurname());
        organizer.setDateOfCreation(userForUpgrade.getDateOfCreation());
        organizer.setRole(roleRepository.findByName("ORGANIZER_ROLE"));
        organizer.setResidency(uuu.getResidency());
        organizer.setPhoneNumber(uuu.getPhoneNumber());
        organizer.setIsVerified(true);
        organizer.setIsDeleted(false);
        organizer.setFavoriteEvents(favoriteEvents);
        organizer.setFavoriteOffers(favoriteOffers);
        organizer.setBlockedUsers(blockedUsers);

        return organizerRepository.save(organizer); // save new version
	}
	

	private Provider upgradeToProvider(AuthentifiedUser userForUpgrade, UnverifiedUserUpgrade uuu) {
		Set<Offer> favoriteOffers = userForUpgrade.getFavoriteOffers();
		Set<Event> favoriteEvents = userForUpgrade.getFavoriteEvents();
		List<AuthentifiedUser> blockedUsers = userForUpgrade.getBlockedUsers();
		
		userRepository.delete(userForUpgrade); // marked for deletion

        Provider provider = new Provider();
        provider.setId(userForUpgrade.getId()); // reuse ID
        provider.setEmail(userForUpgrade.getEmail());
        provider.setPassword(userForUpgrade.getPassword());
        provider.setName(userForUpgrade.getName());
        provider.setSurname(userForUpgrade.getSurname());
        provider.setDateOfCreation(userForUpgrade.getDateOfCreation());
        provider.setRole(roleRepository.findByName("PROVIDER_ROLE"));
        provider.setResidency(uuu.getResidency());
        provider.setPhoneNumber(uuu.getPhoneNumber());
        provider.setProviderName(uuu.getProviderName());
        provider.setDescription(uuu.getDescription());
        provider.setIsVerified(true);
        provider.setIsDeleted(false);
        provider.setFavoriteEvents(favoriteEvents);
        provider.setFavoriteOffers(favoriteOffers);
        provider.setBlockedUsers(blockedUsers);

        return providerRepository.save(provider); // save new version
		
	}
}
