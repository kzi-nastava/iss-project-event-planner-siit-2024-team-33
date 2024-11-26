package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.sql.Date;

import jakarta.persistence.ManyToOne;

public class OfferReservation
{
    public Date DateOfReservation;

    @ManyToOne
    public Offer Offer;
    @ManyToOne
    public Event Event;
}

