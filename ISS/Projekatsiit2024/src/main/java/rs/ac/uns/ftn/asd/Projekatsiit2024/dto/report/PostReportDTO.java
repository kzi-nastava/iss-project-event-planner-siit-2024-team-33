package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.report;

public class PostReportDTO {
    private Integer reportedUserId;
    private Integer reporterId;
    private String content;

    public Integer getReportedUserId() {
        return reportedUserId;
    }

    public void setReportedUserId(Integer reportedUserId) {
        this.reportedUserId = reportedUserId;
    }

    public Integer getReporterId() {
        return reporterId;
    }

    public void setReporterId(Integer reporterId) {
        this.reporterId = reporterId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
