package rs.ac.uns.ftn.asd.Projekatsiit2024.model.communication;


import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;

@Entity
public class Message
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer ID;
	
    public String Content;
    public Date TimeOfSending;

    @ManyToOne
    public AuthentifiedUser Sender;
    @ManyToOne
    public AuthentifiedUser Recipient;
}