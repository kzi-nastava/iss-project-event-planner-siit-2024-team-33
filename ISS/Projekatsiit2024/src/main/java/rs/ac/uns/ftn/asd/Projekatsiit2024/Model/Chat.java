package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.util.List;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

public class Chat
{
	@OneToMany(mappedBy = "Chat")
    public List<Message> Messages;
    @ManyToOne
    public AuthentifiedUser Participant1;
    @ManyToOne
    public AuthentifiedUser Participant2;
}

