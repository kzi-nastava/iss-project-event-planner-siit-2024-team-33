package rs.ac.uns.ftn.asd.Projekatsiit2024.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;

@Entity
public class Chat
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer ID;
	
	@OneToMany(mappedBy = "Chat")
    public List<Message> Messages;
    @ManyToOne
    public AuthentifiedUser Participant1;
    @ManyToOne
    public AuthentifiedUser Participant2;
}

