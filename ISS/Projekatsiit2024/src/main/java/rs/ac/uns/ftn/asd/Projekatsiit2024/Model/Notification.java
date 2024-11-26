package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.sql.Date;

import ch.qos.logback.core.util.Duration;
import jakarta.persistence.ManyToOne;

public class Notification
{
    public String Content;
    public Date DateOfSending;
    public Duration Time;
    
    @ManyToOne
    public AuthentifiedUser Receiver;
}
