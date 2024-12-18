package rs.ac.uns.ftn.asd.Projekatsiit2024.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Invitation;

public interface InvitationRepository extends JpaRepository<Invitation, Integer> {
    List<Invitation> findByInviter(AuthentifiedUser inviter);

    List<Invitation> findByEvent(Event event);
    
    //List<Invitation> findByRecipientEmail(String recipientEmail);
    
    int countByEvent(Event event);
}
