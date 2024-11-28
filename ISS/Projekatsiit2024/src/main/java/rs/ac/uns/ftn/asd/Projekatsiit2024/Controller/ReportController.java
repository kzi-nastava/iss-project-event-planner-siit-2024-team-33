package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.Report;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.report.GetReportDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.report.PostReportDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @PostMapping
    public ResponseEntity<String> submitReport(@RequestBody PostReportDTO postReportDTO) {
        if (postReportDTO == null) {
            return ResponseEntity.badRequest().body("Invalid report data provided.");
        }

        // TODO: Add logic to save the report to the database
        boolean saveSuccess = true;

        if (!saveSuccess) {
            return ResponseEntity.status(500).body("Failed to submit the report.");
        }

        return ResponseEntity.ok("Report submitted successfully.");
    }

    @GetMapping
    public ResponseEntity<List<GetReportDTO>> GetReports(@RequestParam(required = false) String status) {
        List<Report> reports = new ArrayList<>();

        if (status != null) {
            // TODO: Apply filtering logic based on status
        }

        List<GetReportDTO> reportDTOs = new ArrayList<>();
        for (Report report : reports) {
            reportDTOs.add(new GetReportDTO(report));
        }

        return ResponseEntity.ok(reportDTOs);
        
   }

    @GetMapping("/{reportId}")
    public ResponseEntity<GetReportDTO> GetReport(@PathVariable int reportId) {
        Report report = null;

        if (report == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new GetReportDTO(report));
    }

}
