package rs.ac.uns.ftn.asd.Projekatsiit2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.AuthentifiedUser;

import java.util.Optional;

public interface AuthentifiedUserRepository extends JpaRepository<AuthentifiedUser, Integer> {
	@Query("SELECT u FROM AuthentifiedUser u WHERE u.email = :email")
    Optional<AuthentifiedUser> findByEmail(@Param("email") String email);
}
