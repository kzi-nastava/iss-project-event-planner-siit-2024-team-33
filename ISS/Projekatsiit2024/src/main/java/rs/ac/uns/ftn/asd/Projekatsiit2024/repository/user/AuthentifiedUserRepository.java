package rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user;

import java.sql.Timestamp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.event.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;

public interface AuthentifiedUserRepository extends JpaRepository<AuthentifiedUser, Integer> {
	@Query("SELECT u FROM AuthentifiedUser u WHERE u.email = :email")
    AuthentifiedUser findByEmail(@Param("email") String email);
	
	@Query("SELECT u.lastPasswordResetDate FROM AuthentifiedUser u WHERE u.email = :email")
	Timestamp findLastPasswordResetDateByEmail(@Param("email") String email);
	
	@Query("SELECT e FROM AuthentifiedUser u JOIN u.favoriteEvents e WHERE u.id = :userId")
    Page<Event> findFavoriteEventsByUserId(@Param("userId") Integer userId, Pageable pageable);
	
	@Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
	           "FROM AuthentifiedUser u JOIN u.favoriteEvents e " +
	           "WHERE u.id = :userId AND e.id = :eventId")
	 boolean existsEventInFavorites(@Param("userId") Integer userId, 
			 @Param("eventId") Integer eventId);
}
