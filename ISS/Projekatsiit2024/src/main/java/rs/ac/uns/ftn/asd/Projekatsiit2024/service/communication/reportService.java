package rs.ac.uns.ftn.asd.Projekatsiit2024.service.communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.asd.Projekatsiit2024.model.communication.Report;
import rs.ac.uns.ftn.asd.Projekatsiit2024.model.user.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.communication.ReportRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.repository.user.AuthentifiedUserRepository;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class reportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private AuthentifiedUserRepository userRepository;

    public Report createReport(String content, Integer authorId, Integer receiverId) {
        AuthentifiedUser author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author not found."));
        AuthentifiedUser receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found."));

        Report report = new Report();
        report.content = content;
        report.dateOfSending = Date.valueOf(LocalDate.now());
        report.reporter = author;
        report.reported = receiver;

        return reportRepository.save(report);
    }
    
    public Page<Report> getAllReports(Pageable pageable) {
        return reportRepository.findAll(pageable);
    }
    public Report getReportById(int reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found."));
    }

    public void suspendUser(Integer userId) {
        AuthentifiedUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        user.setSuspensionEndDate(LocalDateTime.now().plusDays(3));
        userRepository.save(user);
    }
    
    public void unbanUser(Integer userId) {
        AuthentifiedUser user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found."));

        user.setSuspensionEndDate(null);
        userRepository.save(user);
    	
    }

    public long getSuspensionTimeRemaining(Integer userId) {
        AuthentifiedUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        if (user.getSuspensionEndDate() == null) return 0;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime suspensionEnd = user.getSuspensionEndDate();

        if (suspensionEnd.isBefore(now)) return 0;

        return Duration.between(now, suspensionEnd).toMillis();
    }


    
    
}
