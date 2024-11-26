package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import jakarta.persistence.Entity;

@Entity
public class Service extends Offer
{
    public int ReservationInHours;
    public int CancellationInHours;
    public Boolean IsAutomatic;
    public int MinLengthInMins;
    public int MaxLengthInMins;
}