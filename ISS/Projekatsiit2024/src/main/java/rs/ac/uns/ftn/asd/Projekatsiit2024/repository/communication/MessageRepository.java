package rs.ac.uns.ftn.asd.Projekatsiit2024.repository.communication;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.chat.ChatContactDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.communication.Message;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;

public interface MessageRepository extends JpaRepository<Message,Integer>{
	@Query("SELECT m FROM Message m WHERE " +
	           "(m.Sender = :user1 AND m.Recipient = :user2) OR " +
	           "(m.Sender = :user2 AND m.Recipient = :user1) " +
	           "ORDER BY m.TimeOfSending ASC")
	List<Message> findConversation(@Param("user1") AuthentifiedUser user1,
                               @Param("user2") AuthentifiedUser user2);
	
	
	@Query("SELECT DISTINCT m.Recipient FROM Message m WHERE m.Sender = :user")
	List<AuthentifiedUser> findRecipients(@Param("user") AuthentifiedUser user);

	@Query("SELECT DISTINCT m.Sender FROM Message m WHERE m.Recipient = :user")
	List<AuthentifiedUser> findSenders(@Param("user") AuthentifiedUser user);
}
