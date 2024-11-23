package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.util.List;

public class AuthentifiedUser
{
    public String Email;
    public String Password;
    public String Name;
    public String Surname;

    public List<FavoriteOffer> FavoriteOffers;
    public List<FavoriteEvent> FavoriteEvents;
    public List<Chat> Chats;
}
