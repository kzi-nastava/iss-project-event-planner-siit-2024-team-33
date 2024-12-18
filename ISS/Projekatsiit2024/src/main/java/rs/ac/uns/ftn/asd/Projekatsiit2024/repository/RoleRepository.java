package rs.ac.uns.ftn.asd.Projekatsiit2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	Role findByName(String name);
}
