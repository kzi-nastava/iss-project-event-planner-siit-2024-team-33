package rs.ac.uns.ftn.asd.Projekatsiit2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.report.GetReportDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.report.PostReportDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Report;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.reportService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private reportService reportService;

    @PostMapping
    public ResponseEntity<String> submitReport(@RequestBody PostReportDTO postReportDTO) {
        reportService.createReport(
                postReportDTO.getContent(),
                postReportDTO.getReporterId(),
                postReportDTO.getReportedUserId());
        return ResponseEntity.ok("");
    }

    @GetMapping("/reports")
    public ResponseEntity<List<GetReportDTO>> getReports() {
        List<Report> reports = reportService.getAllReports();
        List<GetReportDTO> reportDTOs = reports.stream()
                .map(GetReportDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reportDTOs);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<GetReportDTO> getReport(@PathVariable int reportId) {
        Report report = reportService.getReportById(reportId);
        return ResponseEntity.ok(new GetReportDTO(report));
    }
    
    @PostMapping("/suspend/{userId}")
    public ResponseEntity<String> suspendUser(@PathVariable Integer userId) {
            reportService.suspendUser(userId);
            return ResponseEntity.ok("");
    }
    
    @GetMapping("/suspension-time/{userId}")
    public ResponseEntity<Long> getSuspensionTimeRemaining(@PathVariable Integer userId) {
            long suspensionTimeRemaining = reportService.getSuspensionTimeRemaining(userId);
            return ResponseEntity.ok(suspensionTimeRemaining);
    }
    
    
}