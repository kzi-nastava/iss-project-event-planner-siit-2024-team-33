package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.report.GetReportDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.report.PostReportDTO;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @PostMapping
    public ResponseEntity<String> submitReport(@RequestBody PostReportDTO postReportDTO) {
        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<List<GetReportDTO>> getAllReports(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<GetReportDTO> getReportById(@RequestAttribute() int reportId) {
        return ResponseEntity.ok(null);
    }

}
