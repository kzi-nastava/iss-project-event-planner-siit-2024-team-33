package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Invitation
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer ID;
    public String Text;
    public Date Date;
    
    @ManyToOne
    public AuthentifiedUser Inviter;
    @ManyToOne
    public Event Event;
}

