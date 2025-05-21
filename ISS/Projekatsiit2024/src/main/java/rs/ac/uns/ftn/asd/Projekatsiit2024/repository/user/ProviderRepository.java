package rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.Provider;

@Repository
public interface ProviderRepository extends JpaRepository<Provider,Integer>{

}
