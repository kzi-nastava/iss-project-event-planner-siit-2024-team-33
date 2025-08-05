package rs.ac.uns.ftn.asd.Projekatsiit2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.report.GetReportDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.report.PostReportDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.exception.event.EventValidationException;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.Report;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.auth.UserPrincipal;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.ReportRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.reportService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private reportService reportService;
    @Autowired
    private AuthentifiedUserRepository userRepo;
    @Autowired
    private ReportRepository reportRepo;
    
    @PostMapping
    public ResponseEntity<GetReportDTO> submitReport(@RequestBody PostReportDTO postReportDTO, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        AuthentifiedUser user = userPrincipal.getUser();
        int userId = user.getId();

        Report report = reportService.createReport(
                postReportDTO.getContent(),
                userId,
                postReportDTO.getReportedUserId());

        return ResponseEntity.ok(new GetReportDTO(report));
    }


    @GetMapping
    public ResponseEntity<Page<GetReportDTO>> getReports(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<Report> reports = reportService.getAllReports(pageable);
        Page<GetReportDTO> reportDTOs = reports.map(GetReportDTO::new);
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
            return ResponseEntity.ok("Banned");
    }
    
    @PostMapping("/unsuspend/{userId}")
    public ResponseEntity<String> unbanUser(@PathVariable Integer userId) {
            reportService.unbanUser(userId);
            return ResponseEntity.ok("Unbanned");
    }
    
    @GetMapping("/suspension-time/{userId}")
    public ResponseEntity<Long> getSuspensionTimeRemaining(@PathVariable Integer userId) {
            long suspensionTimeRemaining = reportService.getSuspensionTimeRemaining(userId);
            return ResponseEntity.ok(suspensionTimeRemaining);
    }
    
    
}