package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.util.List;

public class AuthentifiedUser
{
    public String Email;
    public String Password;
    public String Name;
    public String Surname;
    public List<String> Pictures;

    public List<Offer> FavoriteOffers;
    public List<Event> FavoriteEvents;
    public List<Chat> Chats;
    public List<AuthentifiedUser> BlockedUsers;
    public List<Notification> Notifications;
}
