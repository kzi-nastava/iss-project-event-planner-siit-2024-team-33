package rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.UnverifiedUserUpgrade;

public interface UnverifiedUserUpgradeRepository extends JpaRepository<UnverifiedUserUpgrade,Integer>{	
	@Query("SELECT u FROM UnverifiedUserUpgrade u WHERE u.aUser = :user")
	UnverifiedUserUpgrade findByUser(@Param("user") AuthentifiedUser user);
}
