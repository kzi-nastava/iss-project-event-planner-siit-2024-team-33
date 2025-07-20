package rs.ac.uns.ftn.asd.Projekatsiit2024.model.event;

import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;

@Entity
@Getter
@Setter
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String text;
    private Date date;
    
    @Enumerated(EnumType.STRING)
    private InvitationStatus status = InvitationStatus.PENDING;
    
    @ManyToOne
    private AuthentifiedUser inviter;
    @ManyToOne
    private Event event;
    private String invitedUser;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invitation)) return false;
        Invitation invitation = (Invitation) o;
        return this.getId() == invitation.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
    
}
