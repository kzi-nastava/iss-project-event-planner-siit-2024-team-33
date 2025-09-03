package rs.ac.uns.ftn.asd.Projekatsiit2024.model.communication;

import java.sql.Date;

import ch.qos.logback.core.util.Duration;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;

@Entity
public class Report
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
	
    public String content;
    public Date dateOfSending;

    @ManyToOne
    public AuthentifiedUser reporter;
    @ManyToOne
    public AuthentifiedUser reported;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDateOfSending() {
		return dateOfSending;
	}
	public void setDateOfSending(Date dateOfSending) {
		this.dateOfSending = dateOfSending;
	}
	public AuthentifiedUser getReporter() {
		return reporter;
	}
	public void setReporter(AuthentifiedUser reporter) {
		this.reporter = reporter;
	}
	public AuthentifiedUser getReported() {
		return reported;
	}
	public void setReported(AuthentifiedUser reported) {
		this.reported = reported;
	}
    
    
}
