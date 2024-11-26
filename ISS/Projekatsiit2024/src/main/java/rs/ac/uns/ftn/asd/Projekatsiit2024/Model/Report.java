package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.sql.Date;

import ch.qos.logback.core.util.Duration;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Report
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer ID;
	
    public String Content;
    public Date DateOfSending;

    @ManyToOne
    public AuthentifiedUser Author;
    @ManyToOne
    public AuthentifiedUser Receiver;
}
