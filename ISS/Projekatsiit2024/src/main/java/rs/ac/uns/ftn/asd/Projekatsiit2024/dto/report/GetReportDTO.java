package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.report;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Report;

public class GetReportDTO {
    private Integer id;
    private String content;
    private String author;
    private String receiver;
    private String dateOfSending;
    private Integer receiverId;
    

    public GetReportDTO(Report report) {
        this.id = report.id;
        this.content = report.content;
        this.author = report.author.getName();
        this.receiver = report.receiver.getName();
        this.dateOfSending = report.dateOfSending.toString();
        this.receiverId = report.receiver.getId();
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


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public String getReceiver() {
		return receiver;
	}


	public void setReceiver(String receiver) {
		this.receiver = receiver;
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
