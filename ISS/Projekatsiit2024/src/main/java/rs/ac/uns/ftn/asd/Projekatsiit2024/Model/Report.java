package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.sql.Date;

import ch.qos.logback.core.util.Duration;

public class Report
{
    public String Content;
    public Date DateOfSending;
    //TODO: What is duration lol
    public Duration Time;
    
    public AuthentifiedUser Author;
    public AuthentifiedUser Receiver;

}
