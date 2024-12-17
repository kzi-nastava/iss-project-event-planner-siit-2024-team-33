package rs.ac.uns.ftn.asd.Projekatsiit2024.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUsername(String username);
}
