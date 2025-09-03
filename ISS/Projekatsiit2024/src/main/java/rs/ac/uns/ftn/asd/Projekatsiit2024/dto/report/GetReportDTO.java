package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.report;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.communication.Report;

public class GetReportDTO {
    private Integer id;
    private String content;
    private String reporter;
    private String reported;
    private String dateOfSending;
    private Integer receiverId;
    

    public GetReportDTO(Report report) {
        this.id = report.id;
        this.content = report.content;
        this.reporter = report.reporter.getName();
        this.reported = report.reported.getName();
        this.dateOfSending = report.dateOfSending.toString();
        this.receiverId = report.reported.getId();
    }


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


	public String getReporter() {
		return reporter;
	}


	public void setReporter(String reporter) {
		this.reporter = reporter;
	}


	public String getReported() {
		return reported;
	}


	public void setReported(String reported) {
		this.reported = reporter;
	}


	public String getDateOfSending() {
		return dateOfSending;
	}


	public void setDateOfSending(String dateOfSending) {
		this.dateOfSending = dateOfSending;
	}


	public Integer getReceiverId() {
		return receiverId;
	}


	public void setReceiverId(Integer receiverId) {
		this.receiverId = receiverId;
	}

    
}
