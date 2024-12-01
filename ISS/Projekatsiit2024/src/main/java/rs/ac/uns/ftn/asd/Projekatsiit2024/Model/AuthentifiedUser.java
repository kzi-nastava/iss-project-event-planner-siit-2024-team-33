package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.sql.Date;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class AuthentifiedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String email;

    private String password;
    private String name;
    private String surname;

    @ElementCollection
    private List<String> pictures;
    //Added for reports
    public Date suspensionEndDate;
    
    private Boolean isDeleted;

    @ManyToMany
    private List<Offer> favoriteOffers;

    @ManyToMany
    private List<Event> favoriteEvents;

    @ManyToMany
    private List<AuthentifiedUser> blockedUsers;

    @OneToMany(mappedBy = "receiver")
    private List<Notification> notifications;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<String> getPictures() {
		return pictures;
	}

	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public List<Offer> getFavoriteOffers() {
		return favoriteOffers;
	}

	public void setFavoriteOffers(List<Offer> favoriteOffers) {
		this.favoriteOffers = favoriteOffers;
	}

	public List<Event> getFavoriteEvents() {
		return favoriteEvents;
	}

	public void setFavoriteEvents(List<Event> favoriteEvents) {
		this.favoriteEvents = favoriteEvents;
	}

	public List<AuthentifiedUser> getBlockedUsers() {
		return blockedUsers;
	}

	public void setBlockedUsers(List<AuthentifiedUser> blockedUsers) {
		this.blockedUsers = blockedUsers;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public Date getSuspensionEndDate() {
	    return suspensionEndDate;
	}

	public void setSuspensionEndDate(Date suspensionEndDate) {
	    this.suspensionEndDate = suspensionEndDate;
	}

    public boolean isSuspended() {
        return suspensionEndDate != null && suspensionEndDate.after(new Date(System.currentTimeMillis()));
    }
    
    
}
