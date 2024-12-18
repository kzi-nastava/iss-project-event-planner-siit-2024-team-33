package rs.ac.uns.ftn.asd.Projekatsiit2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	@Query("SELECT user FROM User user WHERE user.email = :email")
	User findByUsername(@Param("email") String email);
}
