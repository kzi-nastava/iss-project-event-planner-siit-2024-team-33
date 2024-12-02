package rs.ac.uns.ftn.asd.Projekatsiit2024.Model;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import ch.qos.logback.core.util.Duration;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Notification
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
	
    public String content;
    public Date timeOfSending;
    public Boolean isRead;
    
    @ManyToOne
    public AuthentifiedUser receiver;
    
    public Notification(String content, Date timeOfSending, AuthentifiedUser receiver) {
        this.content = content;
        this.timeOfSending = timeOfSending;
        this.receiver = receiver;
    }

    public Notification() {
    }
}
