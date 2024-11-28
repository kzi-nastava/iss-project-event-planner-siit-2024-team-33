package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.report;

import java.sql.Date;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Report;

public class GetReportDTO {
    private int reportId;
    private int reportedUserId;
    private String ReportedUserName;
    private int reporterId;
    private String ReporterName;
    private String Content;
    private Date DateOfSending;
    
    public GetReportDTO(Report r) {
    	
    }
}
