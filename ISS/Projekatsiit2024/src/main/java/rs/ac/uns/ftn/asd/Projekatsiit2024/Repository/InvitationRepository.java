package rs.ac.uns.ftn.asd.Projekatsiit2024.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Event;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Invitation;

public interface InvitationRepository extends JpaRepository<Invitation, Integer> {
    List<Invitation> findByInviter(AuthentifiedUser inviter);

    List<Invitation> findByEvent(Event event);
    
    //List<Invitation> findByRecipientEmail(String recipientEmail);
    
    int countByEvent(Event event);
}
