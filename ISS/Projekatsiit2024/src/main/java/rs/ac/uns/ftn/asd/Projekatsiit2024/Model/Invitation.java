package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.time.LocalDateTime;

public class Invitation
{
    public String Text;
    public LocalDateTime Date;
    
    public AuthentifiedUser Inviter;
    public Event Event;
}

