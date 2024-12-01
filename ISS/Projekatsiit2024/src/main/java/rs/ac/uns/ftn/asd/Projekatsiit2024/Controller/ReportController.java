package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Report;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.reportService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.report.GetReportDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.report.PostReportDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private reportService reportService;

    @PostMapping
    public ResponseEntity<String> submitReport(@RequestBody PostReportDTO postReportDTO) {
        if (postReportDTO == null || postReportDTO.getContent() == null 
                || postReportDTO.getReporterId() == null || postReportDTO.getReportedUserId() == null) {
            return ResponseEntity.badRequest().body("Invalid report data provided.");
        }

        try {
            reportService.createReport(
                postReportDTO.getContent(),
                postReportDTO.getReporterId(),
                postReportDTO.getReportedUserId()
            );
            return ResponseEntity.ok("Report submitted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to submit the report.");
        }
    }

    @GetMapping
    public ResponseEntity<List<GetReportDTO>> getReports() {
        try {
            List<Report> reports = reportService.getAllReports();
            List<GetReportDTO> reportDTOs = reports.stream()
                                                   .map(GetReportDTO::new)
                                                   .collect(Collectors.toList());
            return ResponseEntity.ok(reportDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<GetReportDTO> getReport(@PathVariable int reportId) {
        try {
            Report report = reportService.getReportById(reportId);
            if (report == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(new GetReportDTO(report));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
