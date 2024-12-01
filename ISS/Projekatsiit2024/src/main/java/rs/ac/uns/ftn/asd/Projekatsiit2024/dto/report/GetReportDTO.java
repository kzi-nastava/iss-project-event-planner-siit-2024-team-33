package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.report;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Report;

public class GetReportDTO {
    private Integer id;
    private String content;
    private String author;
    private String receiver;
    private String dateOfSending;

    public GetReportDTO(Report report) {
        this.id = report.id;
        this.content = report.content;
        this.author = report.author.getName();
        this.receiver = report.receiver.getName();
        this.dateOfSending = report.dateOfSending.toString();
    }

}
