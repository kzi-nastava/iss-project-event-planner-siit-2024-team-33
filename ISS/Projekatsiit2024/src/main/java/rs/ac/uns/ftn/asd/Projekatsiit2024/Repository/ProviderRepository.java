package rs.ac.uns.ftn.asd.Projekatsiit2024.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Provider;

@Repository
public interface ProviderRepository extends JpaRepository<Provider,Integer>{

}
