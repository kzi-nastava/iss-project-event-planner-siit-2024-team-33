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
    public Double Latitude;
    public Double Longitude;
    public Date DateOfEvent;
    
    public List<EventType> EventTypes ;
    public List<OfferReservation> Reservations;
    public List<BudgetItem> BudgetItems;
}