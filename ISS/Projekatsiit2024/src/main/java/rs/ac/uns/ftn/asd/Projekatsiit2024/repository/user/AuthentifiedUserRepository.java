package rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;

public interface AuthentifiedUserRepository extends JpaRepository<AuthentifiedUser, Integer> {
	@Query("SELECT u FROM AuthentifiedUser u WHERE u.email = :email")
    AuthentifiedUser findByEmail(@Param("email") String email);
	

	@Query("SELECT u FROM AuthentifiedUser u WHERE u.email = :email")
    Optional<AuthentifiedUser> findByEmailOptional(@Param("email") String email);
	
	@Query("SELECT u.lastPasswordResetDate FROM AuthentifiedUser u WHERE u.email = :email")
	Timestamp findLastPasswordResetDateByEmail(@Param("email") String email);
}
