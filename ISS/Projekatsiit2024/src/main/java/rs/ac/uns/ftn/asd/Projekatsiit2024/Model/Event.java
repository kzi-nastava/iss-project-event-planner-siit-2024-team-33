package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.sql.Date;
import java.util.List;

public class Event
{
    public String Name ;
    public String Description ;
    public int NumOfAttendees ;
    public Boolean IsPrivate ;
    public String Place ;
    public Date DateOfEvent ;
    public EventType EventType ;
    public List<OfferReservation> OfferReservations;
    public int Budget;
    //Budget ili ovde  ili moze i kao zasebna klasa
}