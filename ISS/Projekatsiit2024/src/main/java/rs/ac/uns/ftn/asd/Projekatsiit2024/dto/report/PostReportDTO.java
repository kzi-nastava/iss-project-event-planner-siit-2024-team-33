package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.report;

public class PostReportDTO {
    private Integer reportedUserId;
    private String content;

    public Integer getReportedUserId() {
        return reportedUserId;
    }

    public void setReportedUserId(Integer reportedUserId) {
        this.reportedUserId = reportedUserId;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
