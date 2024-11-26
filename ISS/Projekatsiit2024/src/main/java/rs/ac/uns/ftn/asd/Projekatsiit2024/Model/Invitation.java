package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.time.LocalDateTime;

import jakarta.persistence.ManyToOne;

public class Invitation
{
    public String Text;
    public LocalDateTime Date;
    
    @ManyToOne
    public AuthentifiedUser Inviter;
    @ManyToOne
    public Event Event;
}

